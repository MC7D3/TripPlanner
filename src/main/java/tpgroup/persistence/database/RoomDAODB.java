package tpgroup.persistence.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import javax.swing.text.html.HTML.Tag;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import tpgroup.model.Event;
import tpgroup.model.EventsGraph;
import tpgroup.model.EventsNode;
import tpgroup.model.domain.Proposal;
import tpgroup.model.domain.Rating;
import tpgroup.model.domain.Room;
import tpgroup.model.domain.Trip;
import tpgroup.model.domain.User;
import tpgroup.model.exception.RecordNotFoundException;
import tpgroup.persistence.DAO;
import tpgroup.persistence.EventsGraphJSONTypeAdapter;
import tpgroup.persistence.LocalDateTimeJSONTypeAdaper;

public class RoomDAODB implements DAO<Room> {
	Connection conn;
	private final Gson gson;

	public RoomDAODB(Connection conn) {
		this.conn = conn;
		this.gson = new GsonBuilder()
				.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeJSONTypeAdaper())
				.registerTypeAdapter(EventsGraph.class, new EventsGraphJSONTypeAdapter())
				.create();
	}

	private void populateGraphPOI(EventsGraph graph) throws SQLException {
		final String poiQuery = "SELECT * FROM pointofinterest_tbl WHERE name = ? AND location = ?";
		for (EventsNode node : graph.getAllNodes()) {
			for (Event event : node.allEvents()) {
				PreparedStatement poiStmt = conn.prepareStatement(poiQuery);
				poiStmt.setString(1, event.getInfo().getName());
				poiStmt.setString(2, event.getInfo().getLocation());
				ResultSet poiRs = poiStmt.executeQuery();
				event.getInfo().setDescription(poiRs.getString("description"));
				event.getInfo().setRating(Rating.valueOf(poiRs.getString("rating")));
				event.getInfo().setTags(gson.fromJson(poiRs.getString("tags"), new TypeToken<List<Tag>>() {
				}.getType()));
			}
		}
	}

	@Override
	public Room get(Room room) throws RecordNotFoundException {
		final String roomQuery = "SELECT * FROM room_tbl WHERE code = ?";
		final String membersQuery = "SELECT user_email FROM member_of_tbl WHERE room_code = ?";

		try {
			PreparedStatement roomStmt = conn.prepareStatement(roomQuery);
			PreparedStatement membersStmt = conn.prepareStatement(membersQuery);

			roomStmt.setString(1, room.getCode());
			ResultSet rs = roomStmt.executeQuery();

			if (!rs.next()) {
				throw new RecordNotFoundException();
			}

			EventsGraph tripGraph = gson.fromJson(rs.getString("tripgraph"), EventsGraph.class);
			populateGraphPOI(tripGraph);

			List<Proposal> proposals = gson.fromJson(
					rs.getString("proposals"),
					new TypeToken<List<Proposal>>() {
					}.getType());

			Set<User> members = new HashSet<>();
			membersStmt.setString(1, room.getCode());
			ResultSet membersRs = membersStmt.executeQuery();
			while (membersRs.next()) {
				members.add(new User(membersRs.getString("user_email")));
			}

			return new Room(
					rs.getString("name"),
					new User(rs.getString("admin_email")),
					members,
					new Trip(rs.getString("destination"), proposals, tripGraph));

		} catch (SQLException e) {
			throw new RuntimeException("Database error during get()", e);
		}
	}

	@Override
	public void save(Room room) {
		final String updateRoom = "UPDATE room_tbl SET name = ?, admin_email = ?, destination = ?, "
				+ "proposals = ?, tripgraph = ? WHERE code = ?";

		try {
			PreparedStatement stmt = conn.prepareStatement(updateRoom);
			String proposalsJson = gson.toJson(room.getTrip().getProposals());
			String tripGraphJson = gson.toJson(room.getTrip());

			stmt.setString(1, room.getName());
			stmt.setString(2, room.getAdmin().getEmail());
			stmt.setString(3, room.getTrip().getDestination());
			stmt.setString(4, proposalsJson);
			stmt.setString(5, tripGraphJson);
			stmt.setString(6, room.getCode());

			int affected = stmt.executeUpdate();
			if (affected == 0) {
				add(room);
			}
			updateMembers(room);

		} catch (SQLException e) {
			throw new RuntimeException("Database error during save()", e);
		}
	}

	private int updateMembers(Room room) throws SQLException {
		final String selectMembers = "SELECT user_email FROM member_of_tbl WHERE room_code = ?";
		final String insertMember = "INSERT INTO member_of_tbl (room_code, user_email) VALUES (?, ?)";

		PreparedStatement deleteStmt = conn.prepareStatement(selectMembers);
		PreparedStatement insertStmt = conn.prepareStatement(insertMember);

		deleteStmt.setString(1, room.getCode());
		ResultSet rs = deleteStmt.executeQuery();
		List<User> newMembers = new ArrayList<>();
		while (rs.next()) {
			User member = new User(rs.getString("user_email"));
			if (!room.getMembers().contains(member)) {
				newMembers.add(member);
			}
		}

		for (User member : newMembers) {
			insertStmt.setString(1, room.getCode());
			insertStmt.setString(2, member.getEmail());
			insertStmt.addBatch();
		}

		return Arrays.stream(insertStmt.executeBatch()).sum();
	}

	@Override
	public boolean add(Room room) {
		final String insertRoom = "INSERT INTO room_tbl (code, name, admin_email, destination, proposals, tripgraph) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";

		try {
			try (PreparedStatement stmt = conn.prepareStatement(insertRoom)) {
				String proposalsJson = gson.toJson(room.getTrip().getProposals());
				String tripGraphJson = gson.toJson(room.getTrip().getTripGraph());

				stmt.setString(1, room.getCode());
				stmt.setString(2, room.getName());
				stmt.setString(3, room.getAdmin().getEmail());
				stmt.setString(4, room.getTrip().getDestination());
				stmt.setString(5, proposalsJson);
				stmt.setString(6, tripGraphJson);

				if (stmt.executeUpdate() == 0) {
					return false;
				}

				updateMembers(room);
				return true;

			} catch (SQLException e) {
				conn.rollback();
				if (e.getErrorCode() == 1062) { // Duplicate entry
					return false;
				}
				throw e;
			}
		} catch (SQLException e) {
			throw new RuntimeException("Database error during add()", e);
		}
	}

	@Override
	public void delete(Room room) throws RecordNotFoundException {
		final String deleteRoom = "DELETE FROM room_tbl WHERE code = ?";

		try {
			try {
				PreparedStatement stmt = conn.prepareStatement(deleteRoom);
				stmt.setString(1, room.getCode());

				if (stmt.executeUpdate() == 0) {
					throw new RecordNotFoundException();
				}

				PreparedStatement deleteMembers = conn.prepareStatement(
						"DELETE FROM member_of_tbl WHERE room_code = ?");
				deleteMembers.setString(1, room.getCode());
				deleteMembers.executeUpdate();
			} catch (SQLException e) {
				throw e;
			}
		} catch (SQLException e) {
			throw new RuntimeException("Database error during delete()", e);
		}
	}

	@Override
	public List<Room> getAll() {
		final String query = "SELECT code FROM room_tbl";
		List<Room> rooms = new ArrayList<>();

		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				rooms.add(get(new Room(rs.getString("code"))));
			}
		} catch (SQLException | RecordNotFoundException e) {
			throw new RuntimeException("Database error during getAll()", e);
		}
		return rooms;
	}

	@Override
	public List<Room> getFiltered(Predicate<Room> filter) {
		return getAll().stream()
				.filter(filter)
				.toList();
	}

}
