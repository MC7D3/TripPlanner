package tpgroup.view.cli;

import tpgroup.controller.graphical.cli.RoomGController;
import tpgroup.model.bean.ProposalBean;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.statemachine.CliViewState;

public class RemoveProposalForm extends CliViewState {

	private final RoomGController roomGCtrl = new RoomGController();

	public RemoveProposalForm() {
		super();
	}

	@Override
	public void present() {
		try {
			ProposalBean toRemove = FormFieldFactory.getInstance().newSelectItem(
					"select the proposal u want to remove:", roomGCtrl.getLoggedUserProposals()).get();
			CliViewState next = roomGCtrl.removeProposal(toRemove);
			this.machine.setState(next);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

}
