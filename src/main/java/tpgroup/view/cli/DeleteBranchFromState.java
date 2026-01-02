package tpgroup.view.cli;

import tpgroup.controller.graphical.cli.RoomGController;
import tpgroup.model.EventsNode;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.statemachine.CliViewState;

public class DeleteBranchFromState extends CliViewState{

	public DeleteBranchFromState() {
		super();
	}

	@Override
	public void present() {
		try {
			EventsNode chosenBranch = FormFieldFactory.getInstance().newSelectItem("select the branch u want to delete", RoomGController.getBranches(), true).get();
			boolean deleteConf = FormFieldFactory.getInstance().newConfField("are u sure u want to proceed this operation cannot be undone").get();
			CliViewState next = RoomGController.deleteBranch(chosenBranch, deleteConf);
			this.machine.setState(next);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}
	
}
