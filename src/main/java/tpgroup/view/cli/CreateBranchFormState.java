package tpgroup.view.cli;

import tpgroup.controller.graphical.cli.RoomGController;
import tpgroup.view.cli.statemachine.CliViewState;

public class CreateBranchFormState extends CliViewState {

	private final RoomGController roomGCtrl = new RoomGController();

	public CreateBranchFormState() {
		super();
	}

	@Override
	public void present() {
		CliViewState next = roomGCtrl.createBranch();
		this.machine.setState(next);
	}

}
