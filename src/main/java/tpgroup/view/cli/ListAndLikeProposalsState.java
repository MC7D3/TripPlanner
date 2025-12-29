package tpgroup.view.cli;

import tpgroup.controller.graphical.cli.RoomGController;
import tpgroup.model.domain.Proposal;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.stateMachine.CliViewState;

public class ListAndLikeProposalsState extends CliViewState{

	public ListAndLikeProposalsState() {
		super();
	}

	@Override
	public void present() {
		try {
			Proposal chosen = null;
			System.out.println("NOTE: if u want to go back, choose the no option value");
			chosen = FormFieldFactory.getInstance().newSelectItem(RoomGController.getProposalsSortedByLike(), true).get();
			RoomGController.likeProposal(chosen);
			System.out.println("proposal liked!");
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}
	
}
