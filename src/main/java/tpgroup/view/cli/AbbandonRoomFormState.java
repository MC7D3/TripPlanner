package tpgroup.view.cli;

import tpgroup.controller.RoomController;
import tpgroup.model.domain.Room;
import tpgroup.view.cli.template.CliSelectItemFormState;

public class AbbandonRoomFormState extends CliSelectItemFormState<Room> {

	public AbbandonRoomFormState(CliView sm) {
		super(sm, RoomController.getJoinedRooms(), true);
	}

	@Override
	protected void handleChosenElement(Room chosen) {
		if (chosen != null) {
			RoomController.abbandonRoom(chosen.getCode());
			System.out.format("room %s has been abbandoned%n", chosen);
		}
		this.machine.setState(new LoggedMenuState(this.machine));
	}

}
