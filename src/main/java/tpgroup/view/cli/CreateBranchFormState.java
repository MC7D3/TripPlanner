package tpgroup.view.cli;

import tpgroup.controller.graphical.cli.RoomGController;
import tpgroup.view.cli.statemachine.CliViewState;

public class CreateBranchFormState extends CliViewState {

	public CreateBranchFormState() {
		super();
	}

	@Override
	public void present() {
		CliViewState next = RoomGController.createBranch();
		this.machine.setState(next);
	}

}
