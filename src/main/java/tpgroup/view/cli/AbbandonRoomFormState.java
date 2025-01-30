package tpgroup.view.cli;

import java.util.List;
import java.util.stream.Stream;

import tpgroup.controller.RoomController;
import tpgroup.model.Session;
import tpgroup.model.domain.Room;
import tpgroup.view.cli.template.CliMenuState;

public class AbbandonRoomFormState extends CliMenuState {
	private static List<Room> joinedRooms;

	public AbbandonRoomFormState(CliView sm) {
		super(sm, 
			Stream.concat(
				initJoinedRooms()
				.stream()
				.map(room -> room.getName()),
				Stream.of("go back")
			).toList()
		);
	}
	
	private static List<Room> initJoinedRooms(){
		joinedRooms = RoomController.getJoinedRooms(Session.getInstance().getLogged());
		return joinedRooms;
	}

	@Override
	protected void handleChoice(int choice) {
		int chosenRoom = choice - 1;
		if (chosenRoom < joinedRooms.size()) {
			RoomController.abbandonRoom(Session.getInstance().getLogged(), joinedRooms.get(chosenRoom));
			System.out.format("room %s has been abbandoned\n", joinedRooms.get(chosenRoom).getName());
		}
		this.machine.setState(new LoggedMenuState(this.machine));
	}

}
