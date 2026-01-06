package tpgroup.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import tpgroup.model.Event;
import tpgroup.model.EventsNode;
import tpgroup.model.Session;
import tpgroup.model.bean.BranchBean;
import tpgroup.model.bean.EventBean;
import tpgroup.model.bean.IntervalBean;
import tpgroup.model.bean.POIBean;
import tpgroup.model.bean.ProposalBean;
import tpgroup.model.bean.UserBean;
import tpgroup.model.domain.PointOfInterest;
import tpgroup.model.domain.Proposal;
import tpgroup.model.domain.ProposalType;
import tpgroup.model.domain.Room;
import tpgroup.model.domain.Trip;
import tpgroup.model.domain.User;
import tpgroup.model.exception.BranchConnectionException;
import tpgroup.model.exception.NodeConflictException;
import tpgroup.model.exception.NodeConnectionException;
import tpgroup.persistence.DAO;
import tpgroup.persistence.factory.DAOFactory;

public class TripController {

	private TripController() {
		super();
	}

	private static PointOfInterest extractPOI(POIBean bean) {
		return new PointOfInterest(bean.getName(), bean.getDescription(), bean.getCity(), bean.getCountry(),
				bean.getCoordinates(), bean.getRating(), bean.getTags());
	}

	private static Event extractEvent(EventBean bean) {
		return new Event(extractPOI(bean.getInfo()), bean.getStart(), bean.getEnd());
	}

	private static NavigableSet<Event> extractEvents(Set<EventBean> beans) {
		return beans.stream().map(evBean -> extractEvent(evBean))
				.collect(Collectors.toCollection(() -> new TreeSet<Event>()));
	}

	private static EventsNode extractNode(BranchBean bean) {
		return new EventsNode(bean.getId(), extractEvents(bean.getEvents()),
				Session.getInstance().getEnteredRoom().getTrip().getTripGraph());
	}

	private static User extractUser(UserBean bean) {
		return new User(bean.getEmail(), bean.getPassword());
	}

	private static Proposal extractProposal(ProposalBean bean) {
		return new Proposal(bean.getProposalType(), extractNode(bean.getNode()), extractEvent(bean.getEvent()),
				extractUser(bean.getCreator()));
	}

	public static List<ProposalBean> getAllProposals() {
		return Session.getInstance().getEnteredRoom().getTrip().getProposals().stream()
				.map(proposal -> new ProposalBean(proposal)).toList();
	}

	public static List<ProposalBean> getLoggedUserProposals() {
		return Session.getInstance().getEnteredRoom().getTrip().getProposals().stream()
				.filter(proposal -> proposal.getCreator().equals(Session.getInstance().getLogged()))
				.map(proposal -> new ProposalBean(proposal)).toList();
	}

	public static List<BranchBean> getBranches() {
		return Session.getInstance().getEnteredRoom().getTrip().getAllBranches().stream()
				.map(eventsNode -> new BranchBean(eventsNode)).toList();
	}

	public static boolean acceptProposal(ProposalBean proposal) {
		boolean res = Session.getInstance().getEnteredRoom().getTrip().acceptProposal(extractProposal(proposal));
		saveChanges();
		return res;
	}

	public static boolean createAddProposal(BranchBean whereBean, POIBean poi, IntervalBean interval) {
		boolean res = false;
		Trip curTrip = Session.getInstance().getEnteredRoom().getTrip();
		res = curTrip.addProposal(new Proposal(ProposalType.ADD, extractNode(whereBean),
				new Event(extractPOI(poi), interval.getStartTime(), interval.getEndTime()),
				Session.getInstance().getLogged()));
		saveChanges();
		return res;
	}

	public static boolean createRemoveProposal(BranchBean whereBean, EventBean toRemove) {
		Trip curTrip = Session.getInstance().getEnteredRoom().getTrip();
		boolean res = curTrip.addProposal(
				new Proposal(ProposalType.REMOVE, extractNode(whereBean), extractEvent(toRemove),
						Session.getInstance().getLogged()));
		saveChanges();
		return res;
	}

	public static boolean createUpdateProposal(BranchBean whereBean, EventBean toUpdateBean, IntervalBean newData) {
		Event toUpdate = extractEvent(toUpdateBean);
		boolean res = Session.getInstance().getEnteredRoom().getTrip()
				.addProposal(new Proposal(ProposalType.UPDATE, extractNode(whereBean), toUpdate,
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
		Session.getInstance().getEnteredRoom().getTrip().likeProposal(extractProposal(chosen));
		saveChanges();
	}

	public static void removeProposal(ProposalBean proposalBean) {
		Session.getInstance().getEnteredRoom().getTrip().removeProposal(extractProposal(proposalBean));
		saveChanges();
	}

	public static void createBranch(BranchBean parentBean) throws NodeConflictException {
		Session.getInstance().getEnteredRoom().getTrip().createBranch(extractNode(parentBean));
		saveChanges();
	}

	public static void removeBranch(BranchBean toDeleteBean) {
		Session.getInstance().getEnteredRoom().getTrip().removeNode(extractNode(toDeleteBean));
		saveChanges();
	}

	public static void connectBranches(BranchBean branchFromBean, BranchBean branchToBean)
			throws BranchConnectionException {
		try {
			Trip curTrip = Session.getInstance().getEnteredRoom().getTrip();
			curTrip.connectBranches(extractNode(branchFromBean), extractNode(branchToBean));
			saveChanges();
		} catch (NodeConnectionException e) {
			throw new BranchConnectionException(e);
		}
	}

	public static void disconnectBranches(BranchBean fromBean, BranchBean toBean) {
		Session.getInstance().getEnteredRoom().getTrip().disconnectBranches(extractNode(fromBean),
				extractNode(toBean));
		saveChanges();
	}

	public static List<BranchBean> getConnectedBranches(BranchBean branchBean) {
		return Session.getInstance().getEnteredRoom().getTrip().getConnectedBranches(extractNode(branchBean))
				.stream().map(eventsNode -> new BranchBean(eventsNode)).toList();
	}

	public static List<BranchBean> getDeletionCandidates(BranchBean of) {
		return new ArrayList<>(getConnectedBranches(of)).stream()
				.filter(branch -> Session.getInstance().getEnteredRoom().getTrip()
						.connCount(extractNode(branch)) == 0)
				.toList();
	}

	private static void saveChanges() {
		DAO<Room> roomDao = DAOFactory.getInstance().getDAO(Room.class);
		roomDao.save(Session.getInstance().getEnteredRoom());
	}

}
