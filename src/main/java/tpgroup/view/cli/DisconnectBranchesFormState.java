package tpgroup.view.cli;

import tpgroup.controller.graphical.cli.RoomGController;
import tpgroup.model.EventsNode;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.stateMachine.CliViewState;

public class DisconnectBranchesFormState extends CliViewState {

	public DisconnectBranchesFormState() {
		super();
	}

	@Override
	public void present() {
		try {
			EventsNode parent = FormFieldFactory.getInstance()
					.newSelectItem("select select the first end of the connection to undo:", RoomGController.getBranches()).get();
			EventsNode child = FormFieldFactory.getInstance().newSelectItem(
					"select the second end:", RoomGController.getDeletionCandidates(parent)).get();
			CliViewState next = RoomGController.disconnectBranches(parent, child);
			this.machine.setState(next);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
}

}
