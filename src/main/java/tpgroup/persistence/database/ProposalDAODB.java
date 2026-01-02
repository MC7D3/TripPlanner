package tpgroup.persistence.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
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
			"updateevent_start, updateevent_end, proposal_type, likes" +
			") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String SAVE_QUERY = "INSERT INTO proposal_tbl (" +
			"creator_fk, trip_fk, creation_time, node_id, event_poi_coord_lat_fk, event_poi_coord_lon_fk, " +
			"event_start, event_end, updateevent_poi_coord_lat, updateevent_poi_coord_lon, " +
			"updateevent_start, updateevent_end, proposal_type, likes" +
			") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
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
			"likes = VALUES(likes), " +
			"node_id = VALUES(node_id)";

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
				int likes = rs.getInt("likes");
				UUID id = UUID.fromString(rs.getString("node_name"));
				EventsNode node = graph.getAllNodes().stream().filter(n -> n.getId().equals(id)).findFirst().get();

				proposals.add(new Proposal(proposalType, node, event, Optional.ofNullable(updateEvent), creator,
						likes, creationTime));
			}

			return proposals;
		} catch (SQLException | NoSuchElementException e) {
			throw new IllegalStateException("Error retriving room proposals:" + e.getMessage(), e);
		}
	}

	public boolean addRoomProposals(Room room) {
		int count = executeProposalEdit(room, ADD_QUERY);
		return count == room.getTrip().getProposals().size();
	}

	public void saveRoomProposals(Room room) {
		executeProposalEdit(room, SAVE_QUERY);
	}

	private int executeProposalEdit(Room room, String query) {
		int count = 0;

		Set<Proposal> proposals = room.getTrip().getProposals();

		for (Proposal proposal : proposals) {
			try (PreparedStatement stmt = connection.prepareStatement(query)) {
				stmt.setString(1, proposal.getCreator().getEmail());
				stmt.setString(2, room.getCode());
				stmt.setTimestamp(3, Timestamp.valueOf(proposal.getCreationTime()));
				stmt.setDouble(4, proposal.getEvent().getInfo().getCoordinates().getLatitude());
				stmt.setDouble(5, proposal.getEvent().getInfo().getCoordinates().getLongitude());
				stmt.setTimestamp(6, Timestamp.valueOf(proposal.getEvent().getStart()));
				stmt.setTimestamp(7, Timestamp.valueOf(proposal.getEvent().getEnd()));
				Double lat2 = proposal.getUpdateEvent().map(event -> event.getInfo().getCoordinates().getLatitude())
						.orElse(null);
				if (lat2 == null) {
					stmt.setNull(8, Types.DOUBLE);
				} else {
					stmt.setDouble(8, lat2);
				}
				Double lon2 = proposal.getUpdateEvent()
						.map(event -> event.getInfo().getCoordinates().getLongitude()).orElse(null);
				if (lon2 == null) {
					stmt.setNull(9, Types.DOUBLE);
				} else {
					stmt.setDouble(9, lon2);
				}
				stmt.setTimestamp(10,
						proposal.getUpdateEvent().map(event -> Timestamp.valueOf(event.getStart())).orElse(null));
				stmt.setTimestamp(11,
						proposal.getUpdateEvent().map(event -> Timestamp.valueOf(event.getStart())).orElse(null));
				stmt.setString(12, proposal.getProposalType().name());
				stmt.setInt(13, proposal.getLikes());
				stmt.setString(14, proposal.getNodeName().getId().toString());

				count += stmt.executeUpdate();
			} catch (SQLException e) {
				throw new IllegalStateException("Error while saving the proposals: " + e.getMessage(), e);
			}
		}
		return count;
	}
}
