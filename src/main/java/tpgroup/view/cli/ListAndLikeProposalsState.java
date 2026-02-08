package tpgroup.view.cli;

import tpgroup.controller.graphical.cli.RoomGController;
import tpgroup.model.bean.ProposalBean;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.statemachine.CliViewState;

public class ListAndLikeProposalsState extends CliViewState {

	private final RoomGController roomGCtrl = new RoomGController();

	public ListAndLikeProposalsState() {
		super();
	}

	@Override
	public void present() {
		try {
			ProposalBean chosen = null;
			System.out.println("NOTE: if u want to go back, choose the no option value");
			chosen = FormFieldFactory.getInstance().newSelectItem(roomGCtrl.getProposalsSortedByLike(), true).get();
			CliViewState next = roomGCtrl.likeProposal(chosen);
			this.machine.setState(next);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

}
