package tpgroup.view.cli;

import tpgroup.controller.graphical.cli.RoomGController;
import tpgroup.model.bean.ProposalBean;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.statemachine.CliViewState;

public class AcceptProposalFormState extends CliViewState {

	private final RoomGController roomGCtrl = new RoomGController();

	public AcceptProposalFormState() {
		super();
	}

	@Override
	public void present() {
		try {
			ProposalBean accepted = FormFieldFactory.getInstance().newSelectItem(
					"select the proposal you intend to accept:\n", roomGCtrl.getProposalsSortedByLike(), true).get();
			CliViewState next = roomGCtrl.acceptProposal(accepted);
			this.machine.setState(next);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

}
