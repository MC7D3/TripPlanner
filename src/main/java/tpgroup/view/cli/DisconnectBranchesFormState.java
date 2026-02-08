package tpgroup.view.cli;

import tpgroup.controller.graphical.cli.RoomGController;
import tpgroup.model.bean.BranchBean;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.statemachine.CliViewState;

public class DisconnectBranchesFormState extends CliViewState {

	private final RoomGController roomGCtrl = new RoomGController();

	public DisconnectBranchesFormState() {
		super();
	}

	@Override
	public void present() {
		try {
			System.out.println(
					"NOTE: to go back without performing the connection, select no choice on the second node selection");
			BranchBean parent = FormFieldFactory.getInstance()
					.newSelectItem("select select the first end of the connection to undo:",
							roomGCtrl.getBranches())
					.get();
			BranchBean child = FormFieldFactory.getInstance().newSelectItem(
					"select the second end:", roomGCtrl.getConnectedBranches(parent), true).get();
			CliViewState next = roomGCtrl.disconnectBranches(parent, child);
			this.machine.setState(next);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

}
