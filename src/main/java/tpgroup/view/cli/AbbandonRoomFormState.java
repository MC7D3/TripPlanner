package tpgroup.view.cli;

import tpgroup.controller.RoomController;
import tpgroup.model.domain.Room;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;

public class AbbandonRoomFormState extends CliViewState{


	protected AbbandonRoomFormState(CliView machine) {
		super(machine);
	}

	@Override
	public void show() {
		try {
			Room chosen = FormFieldFactory.getInstance().newSelectItem("room's code:", RoomController.getJoinedRooms()).get();
			RoomController.abbandonRoom(chosen);
			System.out.println("room abbandoned succesfully!");
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
		this.machine.setState(new LoggedMenuState(this.machine));
	}
}
