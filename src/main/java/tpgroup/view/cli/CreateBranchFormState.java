package tpgroup.view.cli;

import tpgroup.controller.TripController;
import tpgroup.controller.graphical.cli.RoomGController;
import tpgroup.model.EventsNode;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.stateMachine.CliViewState;

public class CreateBranchFormState extends CliViewState {

	public CreateBranchFormState() {
		super();
	}

	@Override
	public void present() {
		try {
			EventsNode parent = FormFieldFactory.getInstance().newSelectItem("select the branch parent", TripController.getBranches()).get();
			CliViewState next = RoomGController.createBranch(parent);
			this.machine.setState(next);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

}
