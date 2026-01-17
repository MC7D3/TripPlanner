package tpgroup.persistence.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
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

	private final String ADD_QUERY = "INSERT INTO proposal_tbl (" +
			"creator_fk, trip_fk, creation_time, node_id, event_poi_coord_lat_fk, event_poi_coord_lon_fk, " +
			"event_start, event_end, updateevent_poi_coord_lat, updateevent_poi_coord_lon, " +
			"updateevent_start, updateevent_end, proposal_type" +
			") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private final String SAVE_QUERY = "INSERT INTO proposal_tbl (" +
			"creator_fk, trip_fk, creation_time, node_id, event_poi_coord_lat_fk, event_poi_coord_lon_fk, " +
			"event_start, event_end, updateevent_poi_coord_lat, updateevent_poi_coord_lon, " +
			"updateevent_start, updateevent_end, proposal_type" +
			") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
			"ON DUPLICATE KEY UPDATE " +
			"event_poi_coord_lat_fk = VALUES(event_poi_coord_lat_fk), " +
			"event_poi_coord_lon_fk = VALUES(event_poi_coord_lon_fk), " +
			"event_start = VALUES(event_start), " +
			"event_end = VALUES(event_end), " +
			"updateevent_poi_coord_lat = VALUES(updateevent_poi_coord_lat), " +
			"updateevent_poi_coord_lon = VALUES(updateevent_poi_coord_lon), " +
			"updateevent_start = VALUES(updateevent_start), " +
			"updateevent_end = VALUES(updateevent_end), " +
			"proposal_type = VALUES(proposal_type), " +
			"node_id = VALUES(node_id)";

	private final String GET_QUERY = "SELECT " +
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

	private final String GET_LIKES_QUERY = "SELECT u.email, u.password FROM likes_tbl l " +
			"JOIN user_tbl u ON l.user_fk = u.email " +
			"WHERE l.creator_prop_fk = ? AND l.trip_prop_fk = ? AND l.creation_time_prop_fk = ?";

	private final String DELETE_LIKES_QUERY = "DELETE FROM likes_tbl WHERE creator_prop_fk = ? AND trip_prop_fk = ? AND creation_time_prop_fk = ?";

	private final String INSERT_LIKES_QUERY = "INSERT INTO likes_tbl (user_fk, creator_prop_fk, trip_prop_fk, creation_time_prop_fk) VALUES (?, ?, ?, ?)";

	public Set<Proposal> getTripProposals(String roomCode, EventsGraph graph) {

		Set<Proposal> proposals = new HashSet<>();

		try (PreparedStatement stmt = connection.prepareStatement(GET_QUERY)) {
			stmt.setString(1, roomCode);

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				User creator = new User(rs.getString("email"), rs.getString("password"));
				LocalDateTime creationTime = rs.getTimestamp("creation_time").toLocalDateTime();
				PointOfInterest poi1 = new PointOfInterest(
						rs.getString("name1"),
						rs.getString("desc1"),
						rs.getString("city1"),
						rs.getString("country1"),
						new Coordinates(
								rs.getDouble("lat1"),
								rs.getDouble("lon1")),
						Rating.getRatingFromName(rs.getString("rating1")),
						gson.fromJson(rs.getString("tags1"), new TypeToken<List<Tag>>() {
						}.getType()));

				Event event = new Event(poi1, rs.getTimestamp("event_start").toLocalDateTime(),
						rs.getTimestamp("event_end").toLocalDateTime());
				Event updateEvent = null;

				if (rs.getString("name2") != null) {
					PointOfInterest poi2 = new PointOfInterest(
							rs.getString("name2"),
							rs.getString("desc2"),
							rs.getString("city2"),
							rs.getString("country2"),
							new Coordinates(
									rs.getDouble("lat2"),
									rs.getDouble("lon2")),
							Rating.getRatingFromName(rs.getString("rating2")),
							gson.fromJson(rs.getString("tags1"), new TypeToken<List<Tag>>() {
							}.getType()));

					updateEvent = new Event(poi2, rs.getTimestamp("updateevent_start").toLocalDateTime(),
							rs.getTimestamp("updateevent_end").toLocalDateTime());
				}

				ProposalType proposalType = ProposalType.valueOf(rs.getString("proposal_type"));
				Set<User> likes = getLikesForProposal(creator.getEmail(), roomCode, creationTime);

				UUID id = UUID.fromString(rs.getString("node_id"));
				EventsNode node = graph.getGraphNodes().stream().filter(n -> n.getId().equals(id)).findFirst().get();

				proposals.add(new Proposal(proposalType, node, event, Optional.ofNullable(updateEvent), creator,
						likes, creationTime));
			}

			return proposals;
		} catch (SQLException | NoSuchElementException e) {
			throw new IllegalStateException("Error retriving room proposals:" + e.getMessage(), e);
		}
	}

	private Set<User> getLikesForProposal(String creatorEmail, String tripCode, LocalDateTime creationTime) {
		Set<User> likes = new HashSet<>();

		try (PreparedStatement stmt = connection.prepareStatement(GET_LIKES_QUERY)) {
			stmt.setString(1, creatorEmail);
			stmt.setString(2, tripCode);
			stmt.setTimestamp(3, Timestamp.valueOf(creationTime));

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				User user = new User(rs.getString("email"), rs.getString("password"));
				likes.add(user);
			}
		} catch (SQLException e) {
			throw new IllegalStateException("Error retrieving likes for proposal: " + e.getMessage(), e);
		}

		return likes;
	}

	public boolean addRoomProposals(Room room) {
		int count = executeProposalEdit(room, ADD_QUERY);

		for (Proposal proposal : room.getTrip().getProposals()) {
			saveLikesForProposal(proposal, room.getCode());
		}

		return count == room.getTrip().getProposals().size();
	}

	public void saveRoomProposals(Room room) {
		executeProposalEdit(room, SAVE_QUERY);

        for (Proposal proposal : room.getTrip().getProposals()) {
            saveLikesForProposal(proposal, room.getCode());
        }
	}

    private void saveLikesForProposal(Proposal proposal, String roomCode) {
        try {
            connection.setAutoCommit(false);
            
            try (PreparedStatement deleteStmt = connection.prepareStatement(DELETE_LIKES_QUERY)) {
                deleteStmt.setString(1, proposal.getCreator().getEmail());
                deleteStmt.setString(2, roomCode);
                deleteStmt.setTimestamp(3, Timestamp.valueOf(proposal.getCreationTime()));
                deleteStmt.executeUpdate();
            }
            
            if (proposal.getLikesList() != null && !proposal.getLikesList().isEmpty()) {
                try (PreparedStatement insertStmt = connection.prepareStatement(INSERT_LIKES_QUERY)) {
                    for (User user : proposal.getLikesList()) {
                        insertStmt.setString(1, user.getEmail());
                        insertStmt.setString(2, proposal.getCreator().getEmail());
                        insertStmt.setString(3, roomCode);
                        insertStmt.setTimestamp(4, Timestamp.valueOf(proposal.getCreationTime()));
                        insertStmt.addBatch();
                    }
                    insertStmt.executeBatch();
                }
            }
            
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                // no action needed
            }
            throw new IllegalStateException("Error saving likes for proposal: " + e.getMessage(), e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                // no action needed
            }
        }
    }

	private void setCoordinates(PreparedStatement stmt, int latInd, Double lat, int lonInd, Double lon)
			throws SQLException {
		if (lat == null) {
			stmt.setNull(latInd, Types.DOUBLE);
		} else {
			stmt.setDouble(latInd, lat);
		}
		if (lon == null) {
			stmt.setNull(lonInd, Types.DOUBLE);
		} else {
			stmt.setDouble(lonInd, lon);
		}
	}

	private int executeProposalEdit(Room room, String query) {
		int count = 0;

		Set<Proposal> proposals = room.getTrip().getProposals();

		try {
			connection.setAutoCommit(false);
			try (PreparedStatement stmt = connection.prepareStatement(query)) {
				for (Proposal proposal : proposals) {
					int paramIndex = 1;
					stmt.setString(paramIndex++, proposal.getCreator().getEmail());
					stmt.setString(paramIndex++, room.getCode());
					stmt.setTimestamp(paramIndex++, Timestamp.valueOf(proposal.getCreationTime()));
					stmt.setString(paramIndex++, proposal.getNodeName().getId().toString());
					stmt.setDouble(paramIndex++, proposal.getEvent().getInfo().getCoordinates().getLatitude());
					stmt.setDouble(paramIndex++, proposal.getEvent().getInfo().getCoordinates().getLongitude());
					stmt.setTimestamp(paramIndex++, Timestamp.valueOf(proposal.getEvent().getStart()));
					stmt.setTimestamp(paramIndex++, Timestamp.valueOf(proposal.getEvent().getEnd()));
					Double lat2 = proposal.getUpdateEvent().map(event -> event.getInfo().getCoordinates().getLatitude())
							.orElse(null);
					Double lon2 = proposal.getUpdateEvent()
							.map(event -> event.getInfo().getCoordinates().getLongitude()).orElse(null);
					setCoordinates(stmt, paramIndex++, lat2, paramIndex++, lon2);
					stmt.setTimestamp(paramIndex++,
							proposal.getUpdateEvent().map(event -> Timestamp.valueOf(event.getStart())).orElse(null));
					stmt.setTimestamp(paramIndex++,
							proposal.getUpdateEvent().map(event -> Timestamp.valueOf(event.getStart())).orElse(null));
					stmt.setString(paramIndex, proposal.getProposalType().name());
					stmt.setString(14, proposal.getNodeName().getId().toString());

					stmt.addBatch();
				}
				count = Arrays.stream(stmt.executeBatch()).sum();
			}
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException rollbackEx) {
				// no action needed
			}
			throw new IllegalStateException("Error saving proposals: " + e.getMessage(), e);
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				// no action needed
			}
		}
		return count;
	}
}
