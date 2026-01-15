package tpgroup.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tpgroup.model.Event;
import tpgroup.model.EventsGraph;
import tpgroup.model.EventsNode;
import tpgroup.model.Session;
import tpgroup.model.bean.BranchBean;
import tpgroup.model.bean.EventBean;
import tpgroup.model.bean.IntervalBean;
import tpgroup.model.bean.POIBean;
import tpgroup.model.bean.ProposalBean;
import tpgroup.model.bean.StagingBranchBean;
import tpgroup.model.bean.TripBean;
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
import tpgroup.model.exception.RecordNotFoundException;
import tpgroup.persistence.DAO;
import tpgroup.persistence.factory.DAOFactory;

public class TripController {

	private TripController() {
		super();
	}

	private static Room getEnteredRoom(){
		try {
			DAO<Room> roomDao = DAOFactory.getInstance().getDAO(Room.class);
			Session.getInstance().setEnteredRoom(roomDao.get(Session.getInstance().getEnteredRoom()));
			return Session.getInstance().getEnteredRoom();
		} catch (RecordNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}

	private static PointOfInterest extractPOI(POIBean bean) {
		return new PointOfInterest(bean.getName(), bean.getDescription(), bean.getCity(), bean.getCountry(),
				bean.getCoordinates(), bean.getRating(), bean.getTags());
	}

	private static Event extractEvent(EventBean bean) {
		return new Event(extractPOI(bean.getInfo()), bean.getStart(), bean.getEnd());
	}

	private static EventsNode extractNode(BranchBean bean) {
		EventsGraph graph = getEnteredRoom().getTrip().getTripGraph();
		return graph.getAllNodes().stream().filter(node -> node.getId().equals(bean.getId())).findFirst()
				.orElseThrow(() -> new IllegalStateException("node not found in graph"));
	}

	private static User extractUser(UserBean bean) {
		return new User(bean.getEmail(), bean.getPassword());
	}

	private static Proposal extractProposal(ProposalBean bean) {
		Trip trip = getEnteredRoom().getTrip();

		return trip.getProposals().stream()
				.filter(p -> p.getCreator().equals(extractUser(bean.getCreator())) &&
						p.getCreationTime().equals(bean.getCreationTime()) &&
						p.getEvent().equals(extractEvent(bean.getEvent())))
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("Proposal not found in trip"));
	}

	public static List<ProposalBean> getAllProposals() {
		return getEnteredRoom().getTrip().getProposals().stream()
				.map(proposal -> new ProposalBean(proposal)).toList();
	}

	public static List<ProposalBean> getLoggedUserProposals() {
		return getEnteredRoom().getTrip().getProposals().stream()
				.filter(proposal -> proposal.getCreator().equals(Session.getInstance().getLogged()))
				.map(proposal -> new ProposalBean(proposal)).toList();
	}

	public static List<StagingBranchBean> getStagingBranches(){
		return getEnteredRoom().getTrip().getStagingBranches()
				.stream().map(stageBranch -> new StagingBranchBean(stageBranch)).toList();
	}

	public static List<BranchBean> getBranches() {
		List<BranchBean> allBranches = getEnteredRoom().getTrip().getAllBranches().stream()
				.map(eventsNode -> new BranchBean(eventsNode)).collect(Collectors.toList());
		allBranches.addAll(getStagingBranches());
		return allBranches;
	}

	public static TripBean getTrip(){
		return new TripBean(getEnteredRoom().getTrip());
	}

	public static boolean acceptProposal(ProposalBean proposal) {
		boolean res = getEnteredRoom().getTrip().acceptProposal(extractProposal(proposal));
		saveChanges();
		return res;
	}

	public static boolean createAddProposal(BranchBean whereBean, POIBean poi, IntervalBean interval) {
		boolean res = false;
		Trip curTrip = getEnteredRoom().getTrip();
		res = curTrip.addProposal(new Proposal(ProposalType.ADD, extractNode(whereBean),
				new Event(extractPOI(poi), interval.getStartTime(), interval.getEndTime()),
				Session.getInstance().getLogged()));
		saveChanges();
		return res;
	}

	public static boolean createRemoveProposal(BranchBean whereBean, EventBean toRemove) {
		Trip curTrip = getEnteredRoom().getTrip();
		boolean res = curTrip.addProposal(
				new Proposal(ProposalType.REMOVE, extractNode(whereBean), extractEvent(toRemove),
						Session.getInstance().getLogged()));
		saveChanges();
		return res;
	}

	public static boolean createUpdateProposal(BranchBean whereBean, EventBean toUpdateBean, IntervalBean newData) {
		Event toUpdate = extractEvent(toUpdateBean);
		boolean res = getEnteredRoom().getTrip()
				.addProposal(new Proposal(ProposalType.UPDATE, extractNode(whereBean), toUpdate,
						new Event(toUpdate.getInfo(), newData.getStartTime(), newData.getEndTime()),
						Session.getInstance().getLogged()));
		saveChanges();
		return res;
	}

	public static boolean undoProposal(Proposal chosen) {
		boolean res = getEnteredRoom().getTrip().getProposals().remove(chosen);
		saveChanges();
		return res;
	}

	public static boolean likeProposal(ProposalBean chosen) {
		Proposal proposal = extractProposal(chosen);
		boolean res = getEnteredRoom().getTrip().likeProposal(Session.getInstance().getLogged(),
				proposal);
		if (!res) {
			getEnteredRoom().getTrip().unlikeProposal(Session.getInstance().getLogged(),
					proposal);
		}
		saveChanges();
		return res;
	}

	public static void removeProposal(ProposalBean proposalBean) {
		getEnteredRoom().getTrip().removeProposal(extractProposal(proposalBean));
		saveChanges();
	}

	public static void createBranch() throws NodeConflictException {
		getEnteredRoom().getTrip().createBranch();
		saveChanges();
	}

	public static void removeBranch(BranchBean toDeleteBean) throws NodeConflictException {
		getEnteredRoom().getTrip().removeNode(extractNode(toDeleteBean));
		saveChanges();
	}

	public static void connectBranches(BranchBean branchFromBean, BranchBean branchToBean)
			throws BranchConnectionException {
		try {
			Trip curTrip = getEnteredRoom().getTrip();
			curTrip.connectBranches(extractNode(branchFromBean), extractNode(branchToBean));
			saveChanges();
		} catch (NodeConnectionException e) {
			throw new BranchConnectionException(e);
		}
	}

	public static void disconnectBranches(BranchBean fromBean, BranchBean toBean) {
		getEnteredRoom().getTrip().disconnectBranches(extractNode(fromBean),
				extractNode(toBean));
		saveChanges();
	}

	public static List<BranchBean> getConnectedBranches(BranchBean branchBean) {
		List<EventsNode> connectedBranches = getEnteredRoom().getTrip().getConnectedBranches(extractNode(branchBean));
		if(connectedBranches == null){
			return List.of();
		}else{
			return connectedBranches.stream().map(eventsNode -> new BranchBean(eventsNode)).toList();
		}
	}

	public static List<BranchBean> getDeletionCandidates(BranchBean of) {
		List<BranchBean> candidates = new ArrayList<>(getConnectedBranches(of)).stream()
				.filter(branch -> getEnteredRoom().getTrip()
						.connCount(extractNode(branch)) == 0)
				.toList();
		candidates.addAll(getStagingBranches());
		return candidates;
	}

	private static void saveChanges() {
		DAO<Room> roomDao = DAOFactory.getInstance().getDAO(Room.class);
		roomDao.save(getEnteredRoom());
	}

	public static boolean splitBranch(BranchBean toSplitBean, EventBean pivotBean) {
		EventsNode toSplit = extractNode(toSplitBean);
		Event pivot = extractEvent(pivotBean);
		boolean res = toSplit.split(pivot);
		if (res) {
			saveChanges();
		}
		return res;
	}
}
