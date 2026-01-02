package tpgroup.persistence.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import com.google.gson.Gson;

import tpgroup.model.EventsGraph;
import tpgroup.model.domain.Proposal;
import tpgroup.model.domain.Room;
import tpgroup.model.domain.Trip;
import tpgroup.model.domain.User;
import tpgroup.persistence.DAO;

public class RoomDAODB implements DAO<Room> {
	private Connection connection;
	private Gson gson = new Gson();
	private ProposalDAODB proposalDAO;

	private static final String EMAIL_VAR = "email";
	private static final String PWD_VAR = "password";

	public RoomDAODB(Connection connection) {
		this.connection = connection;
		this.proposalDAO = new ProposalDAODB(connection);
	}

	@Override
	public boolean add(Room room) {
		String sql = """
				INSERT INTO room_tbl (code, name, admin_fk, trip_country, trip_main_city, trip_graph)
				VALUES (?, ?, ?, ?, ?, ?)
				""";
		boolean res = false;
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {

			connection.setAutoCommit(false);

			stmt.setString(1, room.getCode());
			stmt.setString(2, room.getName());
			stmt.setString(3, room.getAdmin().getEmail());
			stmt.setString(4, room.getTrip().getCountry());
			stmt.setString(5, room.getTrip().getMainCity());
			stmt.setString(6, serializeTripGraph(room.getTrip().getTripGraph()));

			res = stmt.executeUpdate() > 0;

			if (!res) {
				return res;
			}

			saveRoomMembers(room);

			res = res && proposalDAO.addRoomProposals(room); // handle return value

			connection.commit();

		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException rollbackEx) {
				// no handling required
			}
			throw new IllegalStateException("Error saving room: " + e.getMessage(), e);
		}
		try {
			connection.setAutoCommit(true);
		} catch (Exception e) {
			throw new IllegalStateException("Error saving room: " + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public Room get(Room room) {
		String code = room.getCode();
		if (code == null) {
			return null;
		}
		String sql = """
				SELECT r.code, r.name, r.admin_fk, r.trip_country, r.trip_main_city, r.trip_graph,
				       u.email, u.password
				FROM room_tbl r
				LEFT JOIN user_tbl u ON r.admin_fk = u.email
				WHERE r.code = ?
				""";

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, code);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					User admin = new User(
							rs.getString(EMAIL_VAR),
							rs.getString(PWD_VAR));

					if (admin.getEmail() == null || admin.getPassword() == null) {
						throw new IllegalStateException(String.format("illegal state: room %s has no admin", code));
					}

					String tripGraphJson = rs.getString("trip_graph");
					EventsGraph tripGraph = deserializeTripGraph(tripGraphJson);

					Set<Proposal> proposals = proposalDAO.getTripProposals(code, tripGraph);

					Trip trip = new Trip(
							rs.getString("trip_country"),
							rs.getString("trip_main_city"),
							proposals,
							tripGraph);

					Set<User> members = findRoomMembers(code);

					return new Room(code, admin, members, trip);
				}
				return null;
			}

		} catch (SQLException e) {
			throw new IllegalStateException("Error finding room: " + e.getMessage(), e);
		}
	}

	@Override
	public List<Room> getAll() {
		String sql = """
				SELECT r.code, r.name, r.admin_fk, r.trip_country, r.trip_main_city, r.trip_graph,
				       u.email, u.password
				FROM room_tbl r
				LEFT JOIN user_tbl u ON r.admin_fk = u.email
				""";

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {

			List<Room> rooms = new ArrayList<>();

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					String code = rs.getString("code");

					User admin = new User(
							rs.getString(EMAIL_VAR),
							rs.getString(PWD_VAR));

					if (admin.getEmail() == null || admin.getPassword() == null) {
						throw new IllegalStateException(String.format("illegal state: room %s has no admin", code));
					}

					String tripGraphJson = rs.getString("trip_graph");
					EventsGraph tripGraph = deserializeTripGraph(tripGraphJson);

					Set<Proposal> proposals = proposalDAO.getTripProposals(code, tripGraph);

					Trip trip = new Trip(
							rs.getString("trip_country"),
							rs.getString("trip_main_city"),
							proposals,
							tripGraph);

					Set<User> members = findRoomMembers(code);

					rooms.add(new Room(rs.getString("code"), admin, members, trip));
				}
				return rooms;
			}

		} catch (SQLException e) {
			throw new IllegalStateException("Error finding room: " + e.getMessage(), e);
		}
	}

	@Override
	public void delete(Room room) {
		String code = room.getCode();
		try {
			connection.setAutoCommit(false);

			String deleteMembersSql = "DELETE FROM room_members_tbl WHERE room_fk = ?";
			try (PreparedStatement stmt = connection.prepareStatement(deleteMembersSql)) {
				stmt.setString(1, code);
				stmt.executeUpdate();
			}

			String deleteProposalsSql = "DELETE FROM proposals_tbl WHERE trip_fk = ?";
			try (PreparedStatement stmt = connection.prepareStatement(deleteProposalsSql)) {
				stmt.setString(1, code);
				stmt.executeUpdate();
			}

			String deleteRoomSql = "DELETE FROM room_tbl WHERE code = ?";
			try (PreparedStatement stmt = connection.prepareStatement(deleteRoomSql)) {
				stmt.setString(1, code);
				stmt.executeUpdate();
			}

			connection.commit();

		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException rollbackEx) {
				// no handling needed
			}
			throw new IllegalStateException("Error deleting room: " + e.getMessage(), e);
		}
		try {
			connection.setAutoCommit(true);
		} catch (Exception e) {
			throw new IllegalStateException("Error deleting room: " + e.getMessage(), e);
		}
	}

	@Override
	public void save(Room room) {
		try {
			connection.setAutoCommit(false);

			String sql = """
					INSERT INTO room_tbl (code, name, admin_fk, trip_country, trip_main_city, trip_graph)
					VALUES (?, ?, ?, ?, ?, ?)
					ON DUPLICATE KEY UPDATE
					    name = VALUES(name),
					    admin_fk = VALUES(admin_fk),
					    trip_country = VALUES(trip_country),
					    trip_main_city = VALUES(trip_main_city),
					    trip_graph = VALUES(trip_graph)
					""";

			try (PreparedStatement stmt = connection.prepareStatement(sql)) {
				stmt.setString(1, room.getCode());
				stmt.setString(2, room.getName());
				stmt.setString(3, room.getAdmin().getEmail());
				stmt.setString(4, room.getTrip().getCountry());
				stmt.setString(5, room.getTrip().getMainCity());
				stmt.setString(6, serializeTripGraph(room.getTrip().getTripGraph()));

				stmt.executeUpdate();
			}

			saveRoomMembers(room);

			proposalDAO.saveRoomProposals(room);

			connection.commit();

		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException rollbackEx) {
				// no handling needed
			}
			throw new IllegalStateException("Error updating room: " + e.getMessage(), e);
		}
		try {
			connection.setAutoCommit(true);
		} catch (Exception e) {
			throw new IllegalStateException("Error updating room: " + e.getMessage(), e);
		}
	}

	@Override
	public List<Room> getFiltered(Predicate<Room> filter) {
		return getAll().stream().filter(filter).toList();
	}

	private void saveRoomMembers(Room room) throws SQLException {

		String deleteSql = "DELETE FROM room_members_tbl WHERE room_fk = ?";
		try (PreparedStatement stmt = connection.prepareStatement(deleteSql)) {
			stmt.setString(1, room.getCode());
			stmt.executeUpdate();
		}

		String insertSql = "INSERT INTO room_members_tbl (room_fk, user_fk) VALUES (?, ?)";
		try (PreparedStatement stmt = connection.prepareStatement(insertSql)) {
			for (User member : room.getMembers()) {
				stmt.setString(1, room.getCode());
				stmt.setString(2, member.getEmail());
				stmt.addBatch();
			}
			stmt.executeBatch();
		}

	}

	private Set<User> findRoomMembers(String roomCode) {
		String sql = """
				SELECT u.email, u.password
				FROM room_members_tbl rm
				JOIN user_tbl u ON rm.user_fk = u.email
				WHERE rm.room_fk = ?
				""";

		Set<User> members = new HashSet<>();

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, roomCode);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					members.add(new User(
							rs.getString(EMAIL_VAR),
							rs.getString(PWD_VAR)));
				}
			}

		} catch (SQLException e) {
			throw new IllegalStateException("Error finding room members: " + e.getMessage(), e);
		}

		return members;
	}

	private String serializeTripGraph(EventsGraph tripGraph) {
		return gson.toJson(tripGraph);
	}

	private EventsGraph deserializeTripGraph(String tripGraphJSON) {
		return gson.fromJson(tripGraphJSON, EventsGraph.class);
	}
}
