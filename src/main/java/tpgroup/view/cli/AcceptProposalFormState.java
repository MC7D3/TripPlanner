package tpgroup.view.cli;

import tpgroup.controller.graphical.cli.RoomGController;
import tpgroup.model.domain.Proposal;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.stateMachine.CliViewState;

public class AcceptProposalFormState extends CliViewState{

	public AcceptProposalFormState() {
		super();
	}

	@Override
	public void present() {
		try {
			Proposal accepted = FormFieldFactory.getInstance().newSelectItem("select the proposal you intend to accept:\n", RoomGController.getProposalsSortedByLike(), true).get();
			CliViewState next = RoomGController.acceptProposal(accepted);
			this.machine.setState(next);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}
	
}
