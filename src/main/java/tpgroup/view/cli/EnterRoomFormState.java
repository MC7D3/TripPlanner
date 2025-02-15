package tpgroup.view.cli;

import tpgroup.controller.RoomController;
import tpgroup.model.domain.Room;
import tpgroup.view.cli.component.FormFieldFactory;

public class EnterRoomFormState extends CliViewState{

	protected EnterRoomFormState(CliView machine) {
		super(machine);
	}

	@Override
	public void show() {
		Room chosen = FormFieldFactory.getInstance().newSelectItem(RoomController.getJoinedRooms()).get();
		RoomController.enterRoom(chosen);
		this.machine.setState(RoomController.amIAdmin()? new RoomAdminMenuState(this.machine) : new RoomMemberMenuState(this.machine));
	}
	
}
