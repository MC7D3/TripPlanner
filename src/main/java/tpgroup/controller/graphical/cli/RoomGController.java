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

	private RoomGController() {}

	public static CliViewState process(String choice) {
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

	public static CliViewState processAdmin(String chosen) {
		switch (chosen) {
			case "accept proposal":
				return new AcceptProposalFormState();
			case "create alternative branch":
				return RoomGController.createBranch();
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

	public static TripBean getTrip(){
		return TripController.getTrip();
	}

	public static List<BranchBean> getBranches() {
		return TripController.getBranches();
	}
	
	public static List<ProposalBean> getLoggedUserProposals() {
		return TripController.getLoggedUserProposals();
	}

	public static List<ProposalBean> getProposalsSortedByLike() {
		List<ProposalBean> proposal = new ArrayList<>(TripController.getAllProposals());
		proposal.sort((p1, p2) -> Integer.compare(p2.getLikes(), p1.getLikes()));
		return proposal;
	}

	public static List<BranchBean> getDeletionCandidates(BranchBean forNode) {
		return TripController.getDeletionCandidates(forNode);

	}

	public static List<POIBean> getFilteredPOIs(String minRatingTxt, String maxRatingTxt,
			List<String> chosenTags) {
		try {
			if(minRatingTxt.isEmpty() && maxRatingTxt.isEmpty() && chosenTags.isEmpty()){
				return POIController.getAllPOI();
			}
			POIFilterBean filters = new POIFilterBean(minRatingTxt, maxRatingTxt, chosenTags);
			return POIController.getPOIFiltered(filters);
		} catch (InvalidBeanParamException e) {
			return List.of();
		}

	}

	public static CliViewState createAddProposal(POIBean poi,
			BranchBean chosenBranch, String startTimeTxt, String endTimeTxt) {
		CliViewState ret = RoomController.amIAdmin() ? new RoomAdminMenuState() : new RoomMemberMenuState();
		try {
			if(poi == null && chosenBranch == null && startTimeTxt.isEmpty() && endTimeTxt.isEmpty()){
				return ret;
			}
			if (poi == null) {
				ret.setOutLogTxt(ERROR_PROMPT + "invalid poi filter values provided");
				return ret;
			}
			if (TripController.createAddProposal(chosenBranch, poi,
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

	public static CliViewState createRemoveProposal(BranchBean chosenBranch, EventBean chosenEvent) {
		CliViewState ret = RoomController.amIAdmin() ? new RoomAdminMenuState() : new RoomMemberMenuState();
		if(chosenEvent == null) {
			return ret;
		}
		if (TripController.createRemoveProposal(chosenBranch, chosenEvent)) {
			ret.setOutLogTxt(PROPOSAL_SUCCESS);
		} else {
			ret.setOutLogTxt(PROPOSAL_INVALID);
		}
		return ret;
	}

	public static CliViewState likeProposal(ProposalBean proposal) {
		if (proposal != null) {
			CliViewState ret = new ListAndLikeProposalsState();
			if(!TripController.likeProposal(proposal)){
				ret.setOutLogTxt("proposal like removed");
			}else{
				ret.setOutLogTxt("proposal liked!");
			}
			return ret;
		}
		return RoomController.amIAdmin() ? new RoomAdminMenuState() : new RoomMemberMenuState();
	}

	public static CliViewState createUpdateProposal(BranchBean chosenNode, EventBean chosenEvent,
			String startTimeTxt, String endTimeTxt) {
		CliViewState ret = RoomController.amIAdmin() ? new RoomAdminMenuState() : new RoomMemberMenuState();
		try {
			if (TripController.createUpdateProposal(chosenNode, chosenEvent,
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

	public static CliViewState removeProposal(ProposalBean proposal) {
		CliViewState ret = RoomController.amIAdmin() ? new RoomAdminMenuState() : new RoomMemberMenuState();
		TripController.removeProposal(proposal);
		ret.setOutLogTxt("proposal removed");
		return ret;
	}

	public static CliViewState acceptProposal(ProposalBean accepted) {
		CliViewState ret = new RoomAdminMenuState();
		if (TripController.acceptProposal(accepted)) {
			ret.setOutLogTxt("proposal accepted!");
		} else {
			ret.setOutLogTxt("failed to accept proposal");
		}
		return ret;
	}

	public static CliViewState createBranch() {
		CliViewState ret = new RoomAdminMenuState();
		try {
			TripController.createBranch();
			ret.setOutLogTxt("branch created succesfully!");
		} catch (NodeConflictException e) {
			ret.setOutLogTxt(ERROR_PROMPT + e.getMessage());
		}
		return ret;
	}

	public static CliViewState connectBranches(BranchBean parent, BranchBean child) {
		CliViewState ret = new RoomAdminMenuState();
		if(child == null){
			return ret;
		}
		try {
			TripController.connectBranches(parent, child);
			ret.setOutLogTxt("branches connected succesfully");
		} catch (BranchConnectionException e) {
			ret.setOutLogTxt(ERROR_PROMPT + e.getMessage());
		}
		return ret;
	}

	public static CliViewState disconnectBranches(BranchBean parent, BranchBean child) {
		CliViewState ret = new RoomAdminMenuState();
		if(child == null){
			return ret;
		}
		TripController.disconnectBranches(parent, child);
		ret.setOutLogTxt("branches disconnected succesfully");
		return ret;
	}

	public static CliViewState deleteBranch(BranchBean chosenBranch, boolean deleteConf) {
		CliViewState ret = new RoomAdminMenuState();
		try {
			if (deleteConf) {
				TripController.removeBranch(chosenBranch);
				ret.setOutLogTxt("branch deleted succesfully");
			}
		} catch (NodeConflictException e) {
			ret.setOutLogTxt(ERROR_PROMPT + e.getMessage());
		}
		return ret;
	}

	public static CliViewState deleteRoom(boolean confirmation) {
		CliViewState ret = new RoomAdminMenuState();
		if (confirmation) {
			RoomController.deleteRoom();
			ret = new LoggedMenuState();
			ret.setOutLogTxt("room deleted successfully!");
		}
		return ret;
	}

	public static CliViewState splitBranch(BranchBean toSplit, EventBean pivot){
		CliViewState ret = new RoomAdminMenuState();
		if(toSplit == null || pivot == null){
			return ret;
		}
		if(TripController.splitBranch(toSplit, pivot)){
			ret.setOutLogTxt("branch splitted succesfully!");
		}else{
			ret.setOutLogTxt("failed to split branch");
		}
		return ret;
	}

	public static CliViewState showTripStatus(){
		return RoomController.amIAdmin() ? new RoomAdminMenuState() : new RoomMemberMenuState();
	}
}
