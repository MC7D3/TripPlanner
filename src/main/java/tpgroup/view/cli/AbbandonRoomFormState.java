package tpgroup.view.cli;

import tpgroup.controller.RoomController;
import tpgroup.model.domain.Room;
import tpgroup.view.cli.component.FormFieldFactory;

public class AbbandonRoomFormState extends CliViewState{


	protected AbbandonRoomFormState(CliView machine) {
		super(machine);
	}

	@Override
	public void show() {
		Room chosen = FormFieldFactory.getInstance().newSelectItem("room's code:", RoomController.getJoinedRooms()).get();
		RoomController.abbandonRoom(chosen);
		System.out.println("room abbandoned succesfully!");
		this.machine.setState(new LoggedMenuState(this.machine));
	}
}
