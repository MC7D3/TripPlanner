package tpgroup.controller;

import java.util.ArrayList;
import java.util.List;

import tpgroup.model.BranchBean;
import tpgroup.model.Event;
import tpgroup.model.EventBean;
import tpgroup.model.EventsNode;
import tpgroup.model.IntervalBean;
import tpgroup.model.POIBean;
import tpgroup.model.ProposalBean;
import tpgroup.model.Session;
import tpgroup.model.domain.Proposal;
import tpgroup.model.domain.ProposalType;
import tpgroup.model.domain.Room;
import tpgroup.model.domain.Trip;
import tpgroup.model.exception.BranchConnectionException;
import tpgroup.model.exception.NodeConflictException;
import tpgroup.model.exception.NodeConnectionException;
import tpgroup.persistence.DAO;
import tpgroup.persistence.factory.DAOFactory;

public class TripController {

	private TripController() {
		super();
	}

	public static List<Proposal> getAllProposals() {
		return Session.getInstance().getEnteredRoom().getTrip().getProposals().stream().toList();
	}

	public static List<Proposal> getLoggedUserProposals() {
		return Session.getInstance().getEnteredRoom().getTrip().getProposals().stream()
				.filter(proposal -> proposal.getCreator().equals(Session.getInstance().getLogged())).toList();
	}

	public static List<EventsNode> getBranches() {
		return Session.getInstance().getEnteredRoom().getTrip().getAllBranches();
	}

	public static boolean acceptProposal(ProposalBean proposal) {
		boolean res = Session.getInstance().getEnteredRoom().getTrip().acceptProposal(proposal.getProposal());
		saveChanges();
		return res;
	}

	public static boolean createAddProposal(BranchBean whereBean, POIBean poi, IntervalBean interval) {
		boolean res = false;
		Trip curTrip = Session.getInstance().getEnteredRoom().getTrip();
		res = curTrip.addProposal(new Proposal(ProposalType.ADD, whereBean.getEventsNode(),
				new Event(poi.getPoi(), interval.getStartTime(), interval.getEndTime()),
				Session.getInstance().getLogged()));
		saveChanges();
		return res;
	}

	public static boolean createRemoveProposal(BranchBean whereBean, EventBean toRemove) {
		Trip curTrip = Session.getInstance().getEnteredRoom().getTrip();
		boolean res = curTrip.addProposal(
				new Proposal(ProposalType.REMOVE, whereBean.getEventsNode(), toRemove.getEvent(),
						Session.getInstance().getLogged()));
		saveChanges();
		return res;
	}

	public static boolean createUpdateProposal(BranchBean whereBean, Event toUpdate, IntervalBean newData) {
		boolean res = Session.getInstance().getEnteredRoom().getTrip()
				.addProposal(new Proposal(ProposalType.UPDATE, whereBean.getEventsNode(), toUpdate,
						new Event(toUpdate.getInfo(), newData.getStartTime(), newData.getEndTime()),
						Session.getInstance().getLogged()));
		saveChanges();
		return res;
	}

	public static boolean undoProposal(Proposal chosen) {
		boolean res = Session.getInstance().getEnteredRoom().getTrip().getProposals().remove(chosen);
		saveChanges();
		return res;
	}

	public static void likeProposal(ProposalBean chosen) {
		chosen.getProposal().like();
		saveChanges();
	}

	public static void removeProposal(ProposalBean proposalBean){
		Session.getInstance().getEnteredRoom().getTrip().removeProposal(proposalBean.getProposal());
		saveChanges();
	}

	public static void createBranch(BranchBean parentBean) throws NodeConflictException {
		Session.getInstance().getEnteredRoom().getTrip().createBranch(parentBean.getEventsNode());
		saveChanges();
	}

	public static void removeBranch(BranchBean toDeleteBean) {
		Session.getInstance().getEnteredRoom().getTrip().removeNode(toDeleteBean.getEventsNode());
		saveChanges();
	}

	public static void connectBranches(BranchBean branchFromBean, BranchBean branchToBean)
			throws BranchConnectionException {
		try {
			Trip curTrip = Session.getInstance().getEnteredRoom().getTrip();
			curTrip.connectBranches(branchFromBean.getEventsNode(), branchToBean.getEventsNode());
			saveChanges();
		} catch (NodeConnectionException e) {
			throw new BranchConnectionException(e);
		}
	}

	public static void disconnectBranches(BranchBean fromBean, BranchBean toBean) {
		Session.getInstance().getEnteredRoom().getTrip().disconnectBranches(fromBean.getEventsNode(),
				toBean.getEventsNode());
		saveChanges();
	}

	public static List<EventsNode> getConnectedBranches(BranchBean branchBean) {
		return Session.getInstance().getEnteredRoom().getTrip().getConnectedBranches(branchBean.getEventsNode());
	}

	public static List<EventsNode> getDeletionCandidates(BranchBean of) {
		return new ArrayList<>(getConnectedBranches(of)).stream()
				.filter(branch -> Session.getInstance().getEnteredRoom().getTrip().connCount(branch) == 0).toList();
	}

	private static void saveChanges() {
		DAO<Room> roomDao = DAOFactory.getInstance().getDAO(Room.class);
		roomDao.save(Session.getInstance().getEnteredRoom());
	}

}
