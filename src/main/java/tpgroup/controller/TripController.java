package tpgroup.controller;

import java.util.ArrayList;
import java.util.List;

import tpgroup.model.BranchNameBean;
import tpgroup.model.Event;
import tpgroup.model.EventsNode;
import tpgroup.model.IntervalBean;
import tpgroup.model.Session;
import tpgroup.model.domain.PointOfInterest;
import tpgroup.model.domain.Proposal;
import tpgroup.model.domain.ProposalType;
import tpgroup.model.domain.Room;
import tpgroup.model.exception.BranchConnectionException;
import tpgroup.model.exception.NodeConflictException;
import tpgroup.model.exception.NodeConnectionException;
import tpgroup.persistence.DAO;
import tpgroup.persistence.factory.DAOFactory;

public class TripController {

	public static List<Proposal> getProposals() {
		return Session.getInstance().getEnteredRoom().getTrip().getProposals();
	}

	public static List<EventsNode> getBranches() {
		return Session.getInstance().getEnteredRoom().getTrip().getEventsNodes();
	}

	public static boolean acceptProposal(Proposal proposal) {
		boolean res = proposal.getNode().addEvent(proposal.getEvent());
		Session.getInstance().getEnteredRoom().getTrip().removeProposal(proposal);
		saveChanges();
		return res;
	}

	public static boolean createAddProposal(EventsNode where, PointOfInterest poi, IntervalBean interval) {
		boolean res = Session.getInstance().getEnteredRoom().getTrip().addProposal(new Proposal(ProposalType.ADD, where,
				new Event(poi, interval.getStartTime(), interval.getEndTime()), Session.getInstance().getLogged()));
		saveChanges();
		return res;
	}

	public static boolean createRemoveProposal(EventsNode where, Event toRemove) {
		if (!where.getEvents().contains(toRemove)) {
			return false;
		}
		boolean res = Session.getInstance().getEnteredRoom().getTrip()
				.addProposal(new Proposal(ProposalType.REMOVE, where, toRemove, Session.getInstance().getLogged()));
		saveChanges();
		return res;
	}

	public static boolean createUpdateProposal(EventsNode where, Event toUpdate, IntervalBean newData) {
		if (!where.getEvents().contains(toUpdate)) {
			return false;
		}
		boolean res = Session.getInstance().getEnteredRoom().getTrip()
				.addProposal(new Proposal(ProposalType.UPDATE, where, toUpdate,
						new Event(toUpdate.getInfo(), newData.getStartTime(), newData.getEndTime()),
						Session.getInstance().getLogged()));
		saveChanges();
		return res;
	}

	public static List<Event> getEvents(EventsNode from) {
		return from.getEvents().stream().toList();
	}

	public static List<Proposal> getLoggedProposals() {
		return Session.getInstance().getEnteredRoom().getTrip().getProposals().stream()
				.filter(proposal -> proposal.getCreator().equals(Session.getInstance().getLogged())).toList();
	}

	public static boolean undoProposal(Proposal chosen) {
		boolean res = Session.getInstance().getEnteredRoom().getTrip().getProposals().remove(chosen);
		saveChanges();
		return res;
	}

	public static void likeProposal(Proposal chosen) {
		chosen.like();
		saveChanges();
	}

	public static void createBranch(BranchNameBean newBranch, EventsNode parent) throws NodeConflictException {
		Session.getInstance().getEnteredRoom().getTrip().createBranch(parent, newBranch.getName());
		saveChanges();
	}

	public static void removeBranch(EventsNode toDelete){
		Session.getInstance().getEnteredRoom().getTrip().removeNode(toDelete);
	}

	public static void connectBranches(EventsNode from, EventsNode to) throws BranchConnectionException {
		try {
			Session.getInstance().getEnteredRoom().getTrip().connectBranches(from, to);
			saveChanges();
		} catch (NodeConnectionException e) {
			throw new BranchConnectionException(e);
		}
	}

	public static void disconnectBranches(EventsNode from, EventsNode to) {
		Session.getInstance().getEnteredRoom().getTrip().disconnectBranches(from, to);
		saveChanges();
	}

	public static List<EventsNode> getConnectedBranches(EventsNode of) {
		return Session.getInstance().getEnteredRoom().getTrip().getConnectedBranches(of);
	}

	public static List<EventsNode> getDeletionCandidates(EventsNode of) {
		return new ArrayList<>(getConnectedBranches(of)).stream()
				.filter(branch -> Session.getInstance().getEnteredRoom().getTrip().connCount(branch) == 0).toList();
	}

	public static void removeNode(EventsNode toDelete) {
		Session.getInstance().getEnteredRoom().getTrip().removeNode(toDelete);
	}

	private static void saveChanges() {
		DAO<Room> roomDao = DAOFactory.getInstance().getDAO(Room.class);
		roomDao.save(Session.getInstance().getEnteredRoom());
	}

}
