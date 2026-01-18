package tpgroup.persistence.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import tpgroup.model.Event;
import tpgroup.model.EventsGraph;
import tpgroup.model.EventsNode;
import tpgroup.model.domain.Coordinates;
import tpgroup.model.domain.PointOfInterest;
import tpgroup.model.domain.Proposal;
import tpgroup.model.domain.ProposalType;
import tpgroup.model.domain.Rating;
import tpgroup.model.domain.Room;
import tpgroup.model.domain.Tag;
import tpgroup.model.domain.User;

public class ProposalDAODB {

	private final Connection connection;
	private final Gson gson;

	public ProposalDAODB(Connection connection) {
		this.connection = connection;
		this.gson = new Gson();
	}

	private static final String ADD_QUERY = "INSERT INTO proposal_tbl (" +
			"creator_fk, trip_fk, creation_time, node_id, event_poi_coord_lat_fk, event_poi_coord_lon_fk, " +
			"event_start, event_end, updateevent_poi_coord_lat, updateevent_poi_coord_lon, " +
			"updateevent_start, updateevent_end, proposal_type" +
			") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String SAVE_QUERY = "INSERT INTO proposal_tbl (" +
			"creator_fk, trip_fk, creation_time, node_id, event_poi_coord_lat_fk, event_poi_coord_lon_fk, " +
			"event_start, event_end, updateevent_poi_coord_lat, updateevent_poi_coord_lon, " +
			"updateevent_start, updateevent_end, proposal_type" +
			") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
			"ON DUPLICATE KEY UPDATE " +
			"node_id = VALUES(node_id), " +
			"event_poi_coord_lat_fk = VALUES(event_poi_coord_lat_fk), " +
			"event_poi_coord_lon_fk = VALUES(event_poi_coord_lon_fk), " +
			"event_start = VALUES(event_start), " +
			"event_end = VALUES(event_end), " +
			"updateevent_poi_coord_lat = VALUES(updateevent_poi_coord_lat), " +
			"updateevent_poi_coord_lon = VALUES(updateevent_poi_coord_lon), " +
			"updateevent_start = VALUES(updateevent_start), " +
			"updateevent_end = VALUES(updateevent_end), " +
			"proposal_type = VALUES(proposal_type)";

	private static final String GET_QUERY = "SELECT " +
			"p.*, u.*, " +
			"poi1.name AS name1, poi1.description AS desc1, poi1.city AS city1, " +
			"poi1.coordinates_latitude AS lat1, poi1.coordinates_longitude AS lon1, " +
			"poi1.country AS country1, poi1.rating AS rating1, poi1.tags AS tags1, " +
			"poi2.name AS name2, poi2.description AS desc2, poi2.city AS city2, " +
			"poi2.coordinates_latitude AS lat2, poi2.coordinates_longitude AS lon2, " +
			"poi2.country AS country2, poi2.rating AS rating2, poi2.tags AS tags2 " +
			"FROM proposal_tbl p " +
			"JOIN user_tbl u ON p.creator_fk = u.email " +
			"JOIN poi_tbl poi1 ON p.event_poi_coord_lat_fk = poi1.coordinates_latitude " +
			"AND p.event_poi_coord_lon_fk = poi1.coordinates_longitude " +
			"LEFT JOIN poi_tbl poi2 ON p.updateevent_poi_coord_lat = poi2.coordinates_latitude " +
			"AND p.updateevent_poi_coord_lon = poi2.coordinates_longitude " +
			"WHERE p.trip_fk = ?";

	private static final String DELETE_QUERY = "DELETE FROM proposal_tbl WHERE creator_fk = ? AND trip_fk = ? AND creation_time = ?";

	private static final String GET_LIKES_QUERY = "SELECT u.email, u.password FROM likes_tbl l " +
			"JOIN user_tbl u ON l.user_fk = u.email " +
			"WHERE l.creator_prop_fk = ? AND l.trip_prop_fk = ? AND l.creation_time_prop_fk = ?";

	private static final String DELETE_LIKES_QUERY = "DELETE FROM likes_tbl WHERE creator_prop_fk = ? AND trip_prop_fk = ? AND creation_time_prop_fk = ?";

	private static final String INSERT_LIKES_QUERY = "INSERT INTO likes_tbl (user_fk, creator_prop_fk, trip_prop_fk, creation_time_prop_fk) VALUES (?, ?, ?, ?)";

	public Set<Proposal> getTripProposals(String roomCode, EventsGraph graph) {
		Set<Proposal> proposals = new HashSet<>();

		try (PreparedStatement stmt = connection.prepareStatement(GET_QUERY)) {
			stmt.setString(1, roomCode);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					proposals.add(buildProposalFromResultSet(rs, roomCode, graph));
				}
			}

			return proposals;
		} catch (SQLException e) {
			throw new IllegalStateException("Error retrieving room proposals: " + e.getMessage(), e);
		}
	}

	private Proposal buildProposalFromResultSet(ResultSet rs, String roomCode, EventsGraph graph) throws SQLException {
		User creator = new User(rs.getString("email"), rs.getString("password"));
		LocalDateTime creationTime = rs.getTimestamp("creation_time").toLocalDateTime();

		PointOfInterest poi1 = buildPOIFromResultSet(rs, "1");
		Event event = new Event(poi1,
				rs.getTimestamp("event_start").toLocalDateTime().truncatedTo(ChronoUnit.MINUTES),
				rs.getTimestamp("event_end").toLocalDateTime().truncatedTo(ChronoUnit.MINUTES));

		Event updateEvent = null;
		if (rs.getString("name2") != null && rs.getTimestamp("updateevent_start") != null) {
			PointOfInterest poi2 = buildPOIFromResultSet(rs, "2");
			updateEvent = new Event(poi2,
					rs.getTimestamp("updateevent_start").toLocalDateTime().truncatedTo(ChronoUnit.MINUTES),
					rs.getTimestamp("updateevent_end").toLocalDateTime().truncatedTo(ChronoUnit.MINUTES));
		}

		ProposalType proposalType = ProposalType.valueOf(rs.getString("proposal_type"));
		Set<User> likes = getLikesForProposal(creator.getEmail(), roomCode, creationTime);

		UUID nodeId = UUID.fromString(rs.getString("node_id"));
		EventsNode node = graph.getAllNodes().stream()
				.filter(n -> n.getId().equals(nodeId))
				.findFirst()
				.orElseThrow(() -> new NoSuchElementException("Node not found with id: " + nodeId));

		return new Proposal(proposalType, node, event, Optional.ofNullable(updateEvent), creator, likes, creationTime);
	}

	private PointOfInterest buildPOIFromResultSet(ResultSet rs, String suffix) throws SQLException {
		return new PointOfInterest(
				rs.getString("name" + suffix),
				rs.getString("desc" + suffix),
				rs.getString("city" + suffix),
				rs.getString("country" + suffix),
				new Coordinates(
						rs.getDouble("lat" + suffix),
						rs.getDouble("lon" + suffix)),
				Rating.getRatingFromName(rs.getString("rating" + suffix)),
				gson.fromJson(rs.getString("tags" + suffix), new TypeToken<List<Tag>>() {}.getType()));
	}

	private Set<User> getLikesForProposal(String creatorEmail, String tripCode, LocalDateTime creationTime) {
		Set<User> likes = new HashSet<>();

		try (PreparedStatement stmt = connection.prepareStatement(GET_LIKES_QUERY)) {
			stmt.setString(1, creatorEmail);
			stmt.setString(2, tripCode);
			stmt.setTimestamp(3, Timestamp.valueOf(creationTime));

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					likes.add(new User(rs.getString("email"), rs.getString("password")));
				}
			}
		} catch (SQLException e) {
			throw new IllegalStateException("Error retrieving proposal likes: " + e.getMessage(), e);
		}

		return likes;
	}

	//transazione gestita da RoomDAODB
	public boolean addRoomProposals(Room room) throws SQLException {
		Set<Proposal> proposals = room.getTrip().getProposals();
		
		if (proposals.isEmpty()) {
			return true;
		}

		int proposalCount = executeProposalBatch(room, ADD_QUERY);

		for (Proposal proposal : proposals) {
			insertLikesForProposal(proposal, room.getCode());
		}

		return proposalCount == proposals.size();
	}

	//transazione gestita dal RoomDAODB
	public void saveRoomProposals(Room room) throws SQLException {
		executeProposalBatch(room, SAVE_QUERY);

		for (Proposal proposal : room.getTrip().getProposals()) {
			saveLikesForProposal(proposal, room.getCode());
		}

		deleteRemovedProposals(room);
	}

	//transazione getstita da saveRoomProposals e addRoomProposals
	private int executeProposalBatch(Room room, String query) throws SQLException {
		Set<Proposal> proposals = room.getTrip().getProposals();

		if (proposals.isEmpty()) {
			return 0;
		}

		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			for (Proposal proposal : proposals) {
				setProposalParameters(stmt, proposal, room.getCode());
				stmt.addBatch();
			}

			int[] batchResults = stmt.executeBatch();
			return Arrays.stream(batchResults).sum();
		}
	}

	private void setProposalParameters(PreparedStatement stmt, Proposal proposal, String roomCode) throws SQLException {
		int paramIndex = 1;

		stmt.setString(paramIndex++, proposal.getCreator().getEmail());
		stmt.setString(paramIndex++, roomCode);
		stmt.setTimestamp(paramIndex++, Timestamp.valueOf(proposal.getCreationTime()));
		stmt.setString(paramIndex++, proposal.getNodeName().getId().toString());
		stmt.setDouble(paramIndex++, proposal.getEvent().getInfo().getCoordinates().getLatitude());
		stmt.setDouble(paramIndex++, proposal.getEvent().getInfo().getCoordinates().getLongitude());
		stmt.setTimestamp(paramIndex++, Timestamp.valueOf(proposal.getEvent().getStart()));
		stmt.setTimestamp(paramIndex++, Timestamp.valueOf(proposal.getEvent().getEnd()));

		Double lat2 = proposal.getUpdateEvent()
				.map(event -> event.getInfo().getCoordinates().getLatitude())
				.orElse(null);
		Double lon2 = proposal.getUpdateEvent()
				.map(event -> event.getInfo().getCoordinates().getLongitude())
				.orElse(null);
		setCoordinates(stmt, paramIndex++, lat2, paramIndex++, lon2);

		Timestamp updateStart = proposal.getUpdateEvent()
				.map(event -> Timestamp.valueOf(event.getStart()))
				.orElse(null);
		Timestamp updateEnd = proposal.getUpdateEvent()
				.map(event -> Timestamp.valueOf(event.getEnd()))
				.orElse(null);

		if (updateStart != null) {
			stmt.setTimestamp(paramIndex++, updateStart);
		} else {
			stmt.setNull(paramIndex++, Types.TIMESTAMP);
		}

		if (updateEnd != null) {
			stmt.setTimestamp(paramIndex++, updateEnd);
		} else {
			stmt.setNull(paramIndex++, Types.TIMESTAMP);
		}

		stmt.setString(paramIndex++, proposal.getProposalType().name());
	}

	private void setCoordinates(PreparedStatement stmt, int latIndex, Double lat, int lonIndex, Double lon)
			throws SQLException {
		if (lat == null) {
			stmt.setNull(latIndex, Types.DOUBLE);
		} else {
			stmt.setDouble(latIndex, lat);
		}

		if (lon == null) {
			stmt.setNull(lonIndex, Types.DOUBLE);
		} else {
			stmt.setDouble(lonIndex, lon);
		}
	}

	//transazione gestita da addRoomProposals e delegata dentro saveLikesForProposal 
	private void insertLikesForProposal(Proposal proposal, String roomCode) throws SQLException {
		if (proposal.getLikesList() == null || proposal.getLikesList().isEmpty()) {
			return;
		}

		try (PreparedStatement stmt = connection.prepareStatement(INSERT_LIKES_QUERY)) {
			for (User user : proposal.getLikesList()) {
				stmt.setString(1, user.getEmail());
				stmt.setString(2, proposal.getCreator().getEmail());
				stmt.setString(3, roomCode);
				stmt.setTimestamp(4, Timestamp.valueOf(proposal.getCreationTime()));
				stmt.addBatch();
			}
			stmt.executeBatch();
		}
	}

	//transazione gestita da saveRoomProposals
	private void saveLikesForProposal(Proposal proposal, String roomCode) throws SQLException {
		try (PreparedStatement deleteStmt = connection.prepareStatement(DELETE_LIKES_QUERY)) {
			deleteStmt.setString(1, proposal.getCreator().getEmail());
			deleteStmt.setString(2, roomCode);
			deleteStmt.setTimestamp(3, Timestamp.valueOf(proposal.getCreationTime()));
			deleteStmt.executeUpdate();
		}

		insertLikesForProposal(proposal, roomCode);
	}

	private void deleteRemovedProposals(Room room) throws SQLException {
		Set<Proposal> dbProposals = getTripProposals(room.getCode(), room.getTrip().getTripGraph());
		dbProposals.removeAll(room.getTrip().getProposals());

		if (dbProposals.isEmpty()) {
			return;
		}

		try (PreparedStatement stmt = connection.prepareStatement(DELETE_QUERY)) {
			for (Proposal proposal : dbProposals) {
				stmt.setString(1, proposal.getCreator().getEmail());
				stmt.setString(2, room.getCode());
				stmt.setTimestamp(3, Timestamp.valueOf(proposal.getCreationTime()));
				stmt.addBatch();
			}
			stmt.executeBatch();
		}
	}
}
