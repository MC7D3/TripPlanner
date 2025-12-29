package tpgroup.controller.graphical.cli;

import java.util.ArrayList;
import java.util.List;

import tpgroup.controller.POIController;
import tpgroup.controller.RoomController;
import tpgroup.controller.TripController;
import tpgroup.model.BranchBean;
import tpgroup.model.Event;
import tpgroup.model.EventBean;
import tpgroup.model.EventsNode;
import tpgroup.model.IntervalBean;
import tpgroup.model.POIBean;
import tpgroup.model.POIFilterBean;
import tpgroup.model.ProposalBean;
import tpgroup.model.domain.PointOfInterest;
import tpgroup.model.domain.Proposal;
import tpgroup.model.exception.BranchConnectionException;
import tpgroup.model.exception.InvalidBeanParamException;
import tpgroup.model.exception.NodeConflictException;
import tpgroup.view.cli.AcceptProposalFormState;
import tpgroup.view.cli.ConnectBranchesFormState;
import tpgroup.view.cli.CreateBranchFormState;
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
import tpgroup.view.cli.stateMachine.CliViewState;

public class RoomGController {

	public static CliViewState process(String choice) {
		switch (choice) {
			case "propose new Event":
				return new ProposeAddForm();
			case "propose event removal":
				return new ProposeRemovalForm();
			case "list other proposals":
				return new ListAndLikeProposalsState();
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
				return new CreateBranchFormState();
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

	public static List<EventsNode> getBranches() {
		return TripController.getBranches();
	}

	public static List<Proposal> getLoggedUserProposals() {
		return TripController.getLoggedUserProposals();
	}

	public static List<Proposal> getProposalsSortedByLike() {
		List<Proposal> proposal = new ArrayList<>(TripController.getAllProposals());
		proposal.sort((p1, p2) -> Integer.compare(p2.getLikes(), p1.getLikes()));
		return proposal;
	}

	public static List<EventsNode> getDeletionCandidates(EventsNode forNode) {
		return TripController.getDeletionCandidates(new BranchBean(forNode));

	}

	public static List<PointOfInterest> getFilteredPOIs(String minRatingTxt, String maxRatingTxt,
			List<String> chosenTags) {
		try {
			return POIController.getPOIFiltered(new POIFilterBean(minRatingTxt, maxRatingTxt, chosenTags));
		} catch (InvalidBeanParamException e) {
			return List.of();
		}

	}

	public static CliViewState createAddProposal(PointOfInterest poi,
			EventsNode chosenBranch, String startTimeTxt, String endTimeTxt) {
		CliViewState ret = RoomController.amIAdmin() ? new RoomAdminMenuState() : new RoomMemberMenuState();
		try {
			if (poi == null) {
				ret.setOutLogTxt("ERROR: invalid poi filter values provided");
				return ret;
			}
			if (TripController.createAddProposal(new BranchBean(chosenBranch), new POIBean(poi),
					new IntervalBean(startTimeTxt, endTimeTxt))) {
				ret.setOutLogTxt("proposal inserted succesfully!");
			} else {
				ret.setOutLogTxt("proposal invalid or malformed");
			}
		} catch (InvalidBeanParamException e) {
			ret.setOutLogTxt("ERROR: " + e.getMessage());
		}
		return ret;
	}

	public static CliViewState createRemoveProposal(EventsNode chosenBranch, Event chosenEvent) {
		CliViewState ret = RoomController.amIAdmin() ? new RoomAdminMenuState() : new RoomMemberMenuState();
		if (TripController.createRemoveProposal(new BranchBean(chosenBranch), new EventBean(chosenEvent))) {
			ret.setOutLogTxt("proposal inserted succesfully!");
		} else {
			ret.setOutLogTxt("proposal invalid or malformed");
		}
		return ret;
	}

	public static CliViewState likeProposal(Proposal proposal) {
		if (proposal != null) {
			TripController.likeProposal(new ProposalBean(proposal));
			CliViewState ret = new ListAndLikeProposalsState();
			ret.setOutLogTxt("proposal liked!");
			return ret;
		}
		return RoomController.amIAdmin() ? new RoomAdminMenuState() : new RoomMemberMenuState();
	}

	public static CliViewState createUpdateProposal(EventsNode chosenNode, Event chosenEvent,
			String startTimeTxt, String endTimeTxt) {
		CliViewState ret = RoomController.amIAdmin() ? new RoomAdminMenuState() : new RoomMemberMenuState();
		try {
			if (TripController.createUpdateProposal(new BranchBean(chosenNode), chosenEvent,
					new IntervalBean(startTimeTxt, endTimeTxt))) {
				ret.setOutLogTxt("proposal inserted succesfully!");
			} else {
				ret.setOutLogTxt("proposal invalid or malformed");
			}
		} catch (InvalidBeanParamException e) {
			ret.setOutLogTxt("ERROR: " + e.getMessage());
		}
		return ret;
	}

	public static CliViewState removeProposal(Proposal proposal) {
		CliViewState ret = RoomController.amIAdmin() ? new RoomAdminMenuState() : new RoomMemberMenuState();
		TripController.removeProposal(new ProposalBean(proposal));
		ret.setOutLogTxt("proposal removed");
		return ret;
	}

	public static CliViewState acceptProposal(Proposal accepted) {
		CliViewState ret = new RoomAdminMenuState();
		if (TripController.acceptProposal(new ProposalBean(accepted))) {
			ret.setOutLogTxt("proposal accepted!");
		} else {
			ret.setOutLogTxt("failed to accept proposal");
		}
		return new RoomAdminMenuState();
	}

	public static CliViewState createBranch(EventsNode parent) {
		CliViewState ret = new RoomAdminMenuState();
		try {
			TripController.createBranch(new BranchBean(parent));
			ret.setOutLogTxt("branch %s created succesfully!");
		} catch (NodeConflictException e) {
			ret.setOutLogTxt("ERROR: " + e.getMessage());
		}
		return ret;
	}

	public static CliViewState connectBranches(EventsNode parent, EventsNode child) {
		CliViewState ret = new RoomAdminMenuState();
		try {
			TripController.connectBranches(new BranchBean(parent), new BranchBean(child));
			ret.setOutLogTxt("branches connected succesfully");
		} catch (BranchConnectionException e) {
			ret.setOutLogTxt("ERROR: " + e.getMessage());
		}
		return ret;
	}

	public static CliViewState disconnectBranches(EventsNode parent, EventsNode child) {
		CliViewState ret = new RoomAdminMenuState();
		TripController.disconnectBranches(new BranchBean(parent), new BranchBean(child));
		ret.setOutLogTxt("branches disconnected succesfully");
		return ret;
	}

	public static CliViewState deleteBranch(EventsNode chosenBranch, boolean deleteConf) {
		CliViewState ret = new RoomAdminMenuState();
		if (deleteConf) {
			TripController.removeBranch(new BranchBean(chosenBranch));
			ret.setOutLogTxt("branch deleted succesfully");
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
}
