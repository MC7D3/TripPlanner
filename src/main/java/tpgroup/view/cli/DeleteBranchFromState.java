package tpgroup.view.cli;

import tpgroup.controller.RoomController;
import tpgroup.controller.TripController;
import tpgroup.model.EventsNode;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;

public class DeleteBranchFromState extends CliViewState{

	protected DeleteBranchFromState(CliView machine) {
		super(machine);
	}

	@Override
	public void show() {
		try {
			EventsNode chosen = FormFieldFactory.getInstance().newSelectItem("select the branch u want to delete", TripController.getBranches(), true).get();
			boolean deleteRoom = FormFieldFactory.getInstance().newConfField("are u sure u want to proceed this operation cannot be undone").get();
			if(deleteRoom){
				TripController.removeNode(chosen);
				System.out.println("branch deleted succesfully");
			}
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
		this.machine.setState(RoomController.amIAdmin() ? new RoomAdminMenuState(this.machine)
			: new RoomMemberMenuState(this.machine));
	}
	
}
