package tpgroup.view.cli;

import tpgroup.controller.RoomController;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;

public class DeleteRoomConfirmationState extends CliViewState {

	protected DeleteRoomConfirmationState(CliView machine) {
		super(machine);
	}

	@Override
	public void show() {
		try {
			boolean delete = FormFieldFactory.getInstance().newConfField("are you sure u want to proceed, all the room's data will be lost and cannot be recovered").get();
			if(delete){
				RoomController.deleteRoom();
				System.out.println("room deleted successfully!");
				this.machine.setState(new LoggedMenuState(this.machine));
				return;
			}
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
		this.machine.setState(RoomController.amIAdmin() ? new RoomAdminMenuState(this.machine)
			: new RoomMemberMenuState(this.machine));
	}

	
}
