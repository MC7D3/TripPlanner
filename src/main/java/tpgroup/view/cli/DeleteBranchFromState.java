package tpgroup.view.cli;

import tpgroup.controller.graphical.cli.RoomGController;
import tpgroup.model.bean.BranchBean;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.statemachine.CliViewState;

public class DeleteBranchFromState extends CliViewState {

	private final RoomGController roomGCtrl = new RoomGController();

	public DeleteBranchFromState() {
		super();
	}

	@Override
	public void present() {
		try {
			System.out.println("NOTE: select any branch and then say no later if u want to go back");
			BranchBean chosenBranch = FormFieldFactory.getInstance()
					.newSelectItem("select the branch u want to delete", roomGCtrl.getBranches(), true).get();
			boolean deleteConf = FormFieldFactory.getInstance()
					.newConfField("are u sure u want to proceed this operation cannot be undone").get();
			CliViewState next = roomGCtrl.deleteBranch(chosenBranch, deleteConf);
			this.machine.setState(next);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

}
