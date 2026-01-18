package tpgroup.persistence;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCrypt;

import tpgroup.model.Event;
import tpgroup.model.EventsNode;
import tpgroup.model.domain.PointOfInterest;
import tpgroup.model.domain.Proposal;
import tpgroup.model.domain.ProposalType;
import tpgroup.model.domain.Room;
import tpgroup.model.domain.Tag;
import tpgroup.model.domain.Trip;
import tpgroup.model.domain.User;
import tpgroup.model.exception.FactoryNotInitializedException;
import tpgroup.model.exception.NodeConflictException;
import tpgroup.model.exception.NodeConnectionException;
import tpgroup.model.exception.RecordNotFoundException;
import tpgroup.persistence.factory.DAOFactory;

public class MockDataLoader {

	private final DAO<User> userDAO;
	private final DAO<Room> roomDAO;
	private final DAO<PointOfInterest> poiDAO;

	private final List<User> createdUsers = new ArrayList<>();
	private final List<Room> createdRooms = new ArrayList<>();

	public MockDataLoader() throws FactoryNotInitializedException {
		this.userDAO = DAOFactory.getInstance().getDAO(User.class);
		this.roomDAO = DAOFactory.getInstance().getDAO(Room.class);
		this.poiDAO = DAOFactory.getInstance().getDAO(PointOfInterest.class);
	}

	public void loadMockData() {
		try {
			createUsers();

			List<PointOfInterest> romePOIs = getRomePOIs();
			if (romePOIs.isEmpty()) {
				throw new IllegalStateException("No POIs found in the database. Please load POIs first.");
			}

			createRomeTripRoom(romePOIs);
			createItalianExplorersRoom(romePOIs);
			createRomanticRomeRoom(romePOIs);
		} catch (Exception e) {
			throw new RuntimeException("Failed to load mock data: " + e.getMessage(), e);
		}
	}

	private void createUsers() {
		User[] users = {
				new User("mario.rossi@example.com", BCrypt.hashpw("Password.123", BCrypt.gensalt())),
				new User("laura.bianchi@example.com", BCrypt.hashpw("Password.123", BCrypt.gensalt())),
				new User("giovanni.verdi@example.com", BCrypt.hashpw("Password.123", BCrypt.gensalt())),
				new User("francesca.russo@example.com", BCrypt.hashpw("Password.123", BCrypt.gensalt())),
				new User("antonio.esposito@example.com", BCrypt.hashpw("Password.123", BCrypt.gensalt()))
		};

		for (User user : users) {
			userDAO.save(user);
			createdUsers.add(user);
		}
	}

	private List<PointOfInterest> getRomePOIs() {
		List<PointOfInterest> allPOIs = poiDAO.getAll();
		return allPOIs.stream()
				.filter(poi -> "Rome".equalsIgnoreCase(poi.getCity()))
				.collect(Collectors.toList());
	}

	private void createRomeTripRoom(List<PointOfInterest> romePOIs) {
		try {
			User admin = createdUsers.get(0);
			Room room = new Room("Rome Trip 2024", admin, "Italy", "Rome");

			for (int i = 1; i < createdUsers.size(); i++) {
				room.add(createdUsers.get(i));
			}

			buildComplexTripGraph(room.getTrip(), romePOIs);

			createSampleProposals(room.getTrip(), room);

			roomDAO.add(room);
			createdRooms.add(room);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createItalianExplorersRoom(List<PointOfInterest> romePOIs) {
		try {
			User admin = createdUsers.get(1);
			Room room = new Room("Italian Explorers", admin, "Italy", "Rome");

			room.add(createdUsers.get(2));
			room.add(createdUsers.get(3));

			buildLinearTripGraph(room.getTrip(), romePOIs);

			roomDAO.add(room);
			createdRooms.add(room);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createRomanticRomeRoom(List<PointOfInterest> romePOIs) {
		try {
			User admin = createdUsers.get(3);
			Room room = new Room("Romantic Rome Getaway", admin, "Italy", "Rome");

			room.add(createdUsers.get(4));

			buildRomanticTripGraph(room.getTrip(), romePOIs);

			roomDAO.add(room);
			createdRooms.add(room);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void buildComplexTripGraph(Trip trip, List<PointOfInterest> pois) {
		try {
			List<PointOfInterest> selectedPOIs = selectPOIs(pois, 5);

			EventsNode root = trip.getTripGraph().getRoot();
			Event rootEvent = createEvent(selectedPOIs.get(0), 1, 10, 0, 12, 0);
			root.addEvent(rootEvent);

			trip.createBranch();
			EventsNode node1 = trip.getStagingBranches().get(0);
			Event event1 = createEvent(selectedPOIs.get(1), 1, 14, 0, 16, 0);
			node1.addEvent(event1);
			trip.connectBranches(root, node1);

			trip.createBranch();
			EventsNode node2 = trip.getStagingBranches().get(0);
			Event event2 = createEvent(selectedPOIs.get(2), 2, 10, 0, 13, 0);
			node2.addEvent(event2);
			trip.connectBranches(node1, node2);

			trip.createBranch();
			EventsNode node3 = trip.getStagingBranches().get(0);
			Event event3 = createEvent(selectedPOIs.get(3), 2, 15, 0, 18, 0);
			node3.addEvent(event3);
			trip.connectBranches(node1, node3);

			trip.createBranch();
			EventsNode node4 = trip.getStagingBranches().get(0);
			Event event4 = createEvent(selectedPOIs.get(4), 3, 9, 0, 12, 0);
			node4.addEvent(event4);
			trip.connectBranches(node3, node4);

		} catch (NodeConflictException | NodeConnectionException e) {
			throw new RuntimeException(e);
		}
	}

	private void buildLinearTripGraph(Trip trip, List<PointOfInterest> pois) {
		try {
			List<PointOfInterest> selectedPOIs = selectPOIs(pois, 5);

			EventsNode root = trip.getTripGraph().getRoot();
			Event rootEvent = createEvent(selectedPOIs.get(0), 1, 9, 0, 11, 0);
			root.addEvent(rootEvent);

			EventsNode prevNode = root;

			for (int i = 1; i < 5; i++) {
				trip.createBranch();
				EventsNode currentNode = trip.getStagingBranches().get(0);

				Event event = createEvent(selectedPOIs.get(i), 1, 9 + (i * 3), 0, 11 + (i * 3), 0);
				currentNode.addEvent(event);

				trip.connectBranches(prevNode, currentNode);
				prevNode = currentNode;
			}

		} catch (NodeConflictException | NodeConnectionException e) {
			throw new RuntimeException(e);
		}
	}

	private void buildRomanticTripGraph(Trip trip, List<PointOfInterest> pois) {
		try {
			List<PointOfInterest> romanticPOIs = pois.stream()
					.filter(poi -> poi.getTags().contains(Tag.ROMANTIC))
					.limit(5)
					.collect(Collectors.toList());

			if (romanticPOIs.size() < 5) {
				romanticPOIs = selectPOIs(pois, 5);
			}

			EventsNode root = trip.getTripGraph().getRoot();
			Event rootEvent = createEvent(romanticPOIs.get(0), 1, 17, 0, 19, 0); // Evening event
			root.addEvent(rootEvent);

			trip.createBranch();
			EventsNode node1 = trip.getStagingBranches().get(0);
			Event event1 = createEvent(romanticPOIs.get(1), 2, 14, 0, 16, 0);
			node1.addEvent(event1);
			trip.connectBranches(root, node1);

			trip.createBranch();
			EventsNode node2 = trip.getStagingBranches().get(0);
			Event event2 = createEvent(romanticPOIs.get(2), 2, 18, 0, 20, 0); // Dinner
			node2.addEvent(event2);
			trip.connectBranches(node1, node2);

			trip.createBranch();
			EventsNode node3 = trip.getStagingBranches().get(0);
			Event event3 = createEvent(romanticPOIs.get(3), 2, 17, 0, 19, 0);
			node3.addEvent(event3);
			trip.connectBranches(node1, node3);

			trip.createBranch();
			EventsNode node4 = trip.getStagingBranches().get(0);
			Event event4 = createEvent(romanticPOIs.get(4), 3, 10, 0, 12, 0);
			node4.addEvent(event4);
			trip.connectBranches(node2, node4);

		} catch (NodeConflictException | NodeConnectionException e) {
			throw new RuntimeException(e);
		}
	}

	private void createSampleProposals(Trip trip, Room room) {
		try {
			List<PointOfInterest> allPOIs = poiDAO.getAll();
			List<PointOfInterest> proposalPOIs = selectPOIs(allPOIs, 3);

			List<EventsNode> nodes = trip.getAllBranches();
			if (nodes.size() > 1) {
				EventsNode targetNode = nodes.get(1);

				Event addEvent = createEvent(proposalPOIs.get(0), 4, 15, 0, 17, 0);
				Proposal addProposal = new Proposal(
						ProposalType.ADD,
						targetNode,
						addEvent,
						room.getAdmin());
				trip.addProposal(addProposal);

				if (!targetNode.getEvents().isEmpty()) {
					Event originalEvent = targetNode.getEvents().first();
					Event updatedEvent = createEvent(proposalPOIs.get(1),
							originalEvent.getStart().getDayOfMonth(),
							originalEvent.getStart().getHour(),
							originalEvent.getStart().getMinute(),
							originalEvent.getEnd().getHour() + 1,
							originalEvent.getEnd().getMinute());

					Thread.sleep(1000);
					Proposal updateProposal = new Proposal(
							ProposalType.UPDATE,
							targetNode,
							originalEvent,
							updatedEvent,
							room.getMembers().iterator().next());
					trip.addProposal(updateProposal);
				}

				for (User member : room.getMembers()) {
					if (!member.equals(room.getAdmin())) {
						trip.likeProposal(member, addProposal);
					}
				}

			}

		} catch (Exception e) {
			throw new IllegalStateException();
		}
	}

	private Event createEvent(PointOfInterest poi, int day, int startHour, int startMinute,
			int endHour, int endMinute) {
		LocalDateTime start = LocalDateTime.now()
				.plusDays(day)
				.withHour(startHour)
				.withMinute(startMinute)
				.withSecond(0);

		LocalDateTime end = LocalDateTime.now()
				.plusDays(day)
				.withHour(endHour)
				.withMinute(endMinute)
				.withSecond(0);

		return new Event(poi, start, end);
	}

	private List<PointOfInterest> selectPOIs(List<PointOfInterest> pois, int count) {
		if (pois.size() <= count) {
			return new ArrayList<>(pois);
		}

		return new ArrayList<>(pois.subList(0, count));
	}

	public void cleanupMockData() {
		for (Room room : createdRooms) {
			try {
				Room roomToDelete = roomDAO.get(room);
				roomDAO.delete(roomToDelete);
			} catch (RecordNotFoundException e) {
				// no action needed
			} catch (Exception e) {
				throw new IllegalStateException();
			}
		}

		for (User user : createdUsers) {
			try {
				User userToDelete = userDAO.get(user);
				userDAO.delete(userToDelete);
			} catch (RecordNotFoundException e) {
				// no action needed
			} catch (Exception e) {
				throw new IllegalStateException();
			}
		}

		createdRooms.clear();
		createdUsers.clear();

	}

}
