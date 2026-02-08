package tpgroup.controller.graphical.cli;

import java.util.ArrayList;
import java.util.List;

import tpgroup.controller.POIController;
import tpgroup.controller.RoomController;
import tpgroup.controller.TripController;
import tpgroup.model.bean.BranchBean;
import tpgroup.model.bean.EventBean;
import tpgroup.model.bean.IntervalBean;
import tpgroup.model.bean.POIBean;
import tpgroup.model.bean.POIFilterBean;
import tpgroup.model.bean.ProposalBean;
import tpgroup.model.bean.TripBean;
import tpgroup.model.exception.BranchConnectionException;
import tpgroup.model.exception.InvalidBeanParamException;
import tpgroup.model.exception.NodeConflictException;
import tpgroup.view.cli.AcceptProposalFormState;
import tpgroup.view.cli.ConnectBranchesFormState;
import tpgroup.view.cli.DeleteBranchFromState;
import tpgroup.view.cli.DeleteRoomConfirmationState;
import tpgroup.view.cli.DisconnectBranchesFormState;
import tpgroup.view.cli.ListAndLikeProposalsState;
import tpgroup.view.cli.LoggedMenuState;
import tpgroup.view.cli.ProposeAddForm;
import tpgroup.view.cli.ProposeRemovalForm;
import tpgroup.view.cli.ProposeUpdateForm;
import tpgroup.view.cli.RemoveProposalForm;
import tpgroup.view.cli.RoomAdminMenuState;
import tpgroup.view.cli.RoomMemberMenuState;
import tpgroup.view.cli.SplitBranchMenuState;
import tpgroup.view.cli.TripStatusState;
import tpgroup.view.cli.statemachine.CliViewState;

public class RoomGController {
	private static final String PROPOSAL_SUCCESS = "proposal inserted succesfully!";
	private static final String PROPOSAL_INVALID = "proposal invalid or malformed";
	private static final String ERROR_PROMPT = "ERROR: ";
	
	private final TripController tripCtrl = new TripController();
	private final RoomController roomCtrl = new RoomController();
	private final POIController poiCtrl = new POIController();

	public RoomGController() {
		super();
	}

	public CliViewState process(String choice) {
		switch (choice) {
			case "propose new Event":
				return new ProposeAddForm();
			case "propose event removal":
				return new ProposeRemovalForm();
			case "list other proposals":
				return new ListAndLikeProposalsState();
			case "show trip status":
				return new TripStatusState();
			case "propose event update":
				return new ProposeUpdateForm();
			case "undo proposal":
				return new RemoveProposalForm();
			default:
				return new LoggedMenuState();
		}
	}

	public CliViewState processAdmin(String chosen) {
		switch (chosen) {
			case "accept proposal":
				return new AcceptProposalFormState();
			case "create alternative branch":
				return createBranch();
			case "split branch":
				return new SplitBranchMenuState();
			case "connect branches":
				return new ConnectBranchesFormState();
			case "disconnect branches":
				return new DisconnectBranchesFormState();
			case "delete branch":
				return new DeleteBranchFromState();
			case "delete room":
				return new DeleteRoomConfirmationState();
			default:
				return process(chosen);
		}
	}

	public TripBean getTrip() {
		return tripCtrl.getTrip();
	}

	public List<BranchBean> getBranches() {
		return tripCtrl.getAllBranches();
	}

	public List<ProposalBean> getLoggedUserProposals() {
		return tripCtrl.getLoggedUserProposals();
	}

	public List<ProposalBean> getProposalsSortedByLike() {
		List<ProposalBean> proposal = new ArrayList<>(tripCtrl.getAllProposals());
		proposal.sort((p1, p2) -> Integer.compare(p2.getLikes(), p1.getLikes()));
		return proposal;
	}

	public List<BranchBean> getDeletionCandidates() {
		return tripCtrl.getDeletionCandidates();

	}

	public List<BranchBean> getConnectedBranches(BranchBean of){
		return tripCtrl.getConnectedBranches(of);
	}

	public List<POIBean> getFilteredPOIs(String minRatingTxt, String maxRatingTxt,
			List<String> chosenTags) {
		try {
			if (minRatingTxt.isEmpty() && maxRatingTxt.isEmpty() && chosenTags.isEmpty()) {
				return poiCtrl.getAllPOI();
			}
			POIFilterBean filters = new POIFilterBean(minRatingTxt, maxRatingTxt, chosenTags);
			return poiCtrl.getPOIFiltered(filters);
		} catch (InvalidBeanParamException e) {
			return List.of();
		}

	}

	public CliViewState createAddProposal(POIBean poi,
			BranchBean chosenBranch, String startTimeTxt, String endTimeTxt) {
		CliViewState ret = roomCtrl.amIAdmin() ? new RoomAdminMenuState() : new RoomMemberMenuState();
		try {
			if (poi == null && chosenBranch == null && startTimeTxt.isEmpty() && endTimeTxt.isEmpty()) {
				return ret;
			}
			if (poi == null) {
				ret.setOutLogTxt(ERROR_PROMPT + "invalid poi filter values provided");
				return ret;
			}
			if (tripCtrl.createAddProposal(chosenBranch, poi,
					new IntervalBean(startTimeTxt, endTimeTxt))) {
				ret.setOutLogTxt(PROPOSAL_SUCCESS);
			} else {
				ret.setOutLogTxt(PROPOSAL_INVALID);
			}
		} catch (InvalidBeanParamException e) {
			ret.setOutLogTxt(ERROR_PROMPT + e.getMessage());
		}
		return ret;
	}

	public CliViewState createRemoveProposal(BranchBean chosenBranch, EventBean chosenEvent) {
		CliViewState ret = roomCtrl.amIAdmin() ? new RoomAdminMenuState() : new RoomMemberMenuState();
		if (chosenEvent == null) {
			return ret;
		}
		if (tripCtrl.createRemoveProposal(chosenBranch, chosenEvent)) {
			ret.setOutLogTxt(PROPOSAL_SUCCESS);
		} else {
			ret.setOutLogTxt(PROPOSAL_INVALID);
		}
		return ret;
	}

	public CliViewState likeProposal(ProposalBean proposal) {
		if (proposal != null) {
			CliViewState ret = new ListAndLikeProposalsState();
			if (!tripCtrl.likeProposal(proposal)) {
				ret.setOutLogTxt("proposal like removed");
			} else {
				ret.setOutLogTxt("proposal liked!");
			}
			return ret;
		}
		return roomCtrl.amIAdmin() ? new RoomAdminMenuState() : new RoomMemberMenuState();
	}

	public CliViewState createUpdateProposal(BranchBean chosenNode, EventBean chosenEvent,
			String startTimeTxt, String endTimeTxt) {
		CliViewState ret = roomCtrl.amIAdmin() ? new RoomAdminMenuState() : new RoomMemberMenuState();
		try {
			if (tripCtrl.createUpdateProposal(chosenNode, chosenEvent,
					new IntervalBean(startTimeTxt, endTimeTxt))) {
				ret.setOutLogTxt(PROPOSAL_SUCCESS);
			} else {
				ret.setOutLogTxt(PROPOSAL_INVALID);
			}
		} catch (InvalidBeanParamException e) {
			ret.setOutLogTxt(ERROR_PROMPT + e.getMessage());
		}
		return ret;
	}

	public CliViewState removeProposal(ProposalBean proposal) {
		CliViewState ret = roomCtrl.amIAdmin() ? new RoomAdminMenuState() : new RoomMemberMenuState();
		tripCtrl.removeProposal(proposal);
		ret.setOutLogTxt("proposal removed");
		return ret;
	}

	public CliViewState acceptProposal(ProposalBean accepted) {
		CliViewState ret = new RoomAdminMenuState();
		if (accepted != null) {
			if (tripCtrl.acceptProposal(accepted)) {
				ret.setOutLogTxt("proposal accepted!");
			} else {
				ret.setOutLogTxt("failed to accept proposal");
			}
		}
		return ret;
	}

	public CliViewState createBranch() {
		CliViewState ret = new RoomAdminMenuState();
		try {
			tripCtrl.createBranch();
			ret.setOutLogTxt("branch created succesfully!");
		} catch (NodeConflictException e) {
			ret.setOutLogTxt(ERROR_PROMPT + e.getMessage());
		}
		return ret;
	}

	public CliViewState connectBranches(BranchBean parent, BranchBean child) {
		CliViewState ret = new RoomAdminMenuState();
		if (child == null) {
			return ret;
		}
		try {
			tripCtrl.connectBranches(parent, child);
			ret.setOutLogTxt("branches connected succesfully");
		} catch (BranchConnectionException e) {
			ret.setOutLogTxt(ERROR_PROMPT + e.getMessage());
		}
		return ret;
	}

	public CliViewState disconnectBranches(BranchBean parent, BranchBean child) {
		CliViewState ret = new RoomAdminMenuState();
		if (child == null) {
			return ret;
		}
		tripCtrl.disconnectBranches(parent, child);
		ret.setOutLogTxt("branches disconnected succesfully");
		return ret;
	}

	public CliViewState deleteBranch(BranchBean chosenBranch, boolean deleteConf) {
		CliViewState ret = new RoomAdminMenuState();
		if (deleteConf) {
			tripCtrl.removeBranch(chosenBranch);
			ret.setOutLogTxt("branch deleted succesfully");
		}
		return ret;
	}

	public CliViewState deleteRoom(boolean confirmation) {
		CliViewState ret = new RoomAdminMenuState();
		if (confirmation) {
			roomCtrl.deleteRoom();
			ret = new LoggedMenuState();
			ret.setOutLogTxt("room deleted successfully!");
		}
		return ret;
	}

	public CliViewState splitBranch(BranchBean toSplit, EventBean pivot) {
		CliViewState ret = new RoomAdminMenuState();
		if (toSplit == null || pivot == null) {
			return ret;
		}
		if (tripCtrl.splitBranch(toSplit, pivot)) {
			ret.setOutLogTxt("branch splitted succesfully!");
		} else {
			ret.setOutLogTxt("failed to split branch");
		}
		return ret;
	}

	public CliViewState showTripStatus() {
		return roomCtrl.amIAdmin() ? new RoomAdminMenuState() : new RoomMemberMenuState();
	}
}
