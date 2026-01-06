package tpgroup.view.cli;

import tpgroup.controller.graphical.cli.RoomGController;
import tpgroup.model.bean.BranchBean;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.statemachine.CliViewState;

public class CreateBranchFormState extends CliViewState {

	public CreateBranchFormState() {
		super();
	}

	@Override
	public void present() {
		try {
			BranchBean parent = FormFieldFactory.getInstance().newSelectItem("select the branch parent", RoomGController.getBranches()).get();
			CliViewState next = RoomGController.createBranch(parent);
			this.machine.setState(next);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

}
