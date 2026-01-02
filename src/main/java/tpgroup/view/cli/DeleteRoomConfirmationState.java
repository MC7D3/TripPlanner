package tpgroup.view.cli;

import tpgroup.controller.graphical.cli.RoomGController;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.statemachine.CliViewState;

public class DeleteRoomConfirmationState extends CliViewState {

	public DeleteRoomConfirmationState() {
		super();
	}

	@Override
	public void present() {
		try {
			boolean delete = FormFieldFactory.getInstance().newConfField("are you sure u want to proceed, all the room's data will be lost and cannot be recovered").get();
			CliViewState next = RoomGController.deleteRoom(delete);
			this.machine.setState(next);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

	
}
