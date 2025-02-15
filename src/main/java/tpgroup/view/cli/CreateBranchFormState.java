package tpgroup.view.cli;

import tpgroup.controller.RoomController;
import tpgroup.controller.TripController;
import tpgroup.model.BranchNameBean;
import tpgroup.model.EventsNode;
import tpgroup.model.exception.InvalidBeanParamException;
import tpgroup.model.exception.NodeConflictException;
import tpgroup.view.cli.component.FormFieldFactory;

public class CreateBranchFormState extends CliViewState{

	protected CreateBranchFormState(CliView machine) {
		super(machine);
	}

	@Override
	public void show() {
		try {
			EventsNode parent = FormFieldFactory.getInstance().newSelectItem("select the branch parent", TripController.getBranches()).get();
			String branchName = FormFieldFactory.getInstance().newDefault("new branch name:", str -> str).get();
			TripController.createBranch(new BranchNameBean(branchName), parent);
			System.out.println(String.format("branch %s created succesfully!", branchName));
		} catch (InvalidBeanParamException | NodeConflictException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
		this.machine.setState(RoomController.amIAdmin() ? new RoomAdminMenuState(this.machine)
				: new RoomMemberMenuState(this.machine));
	}
	
}
