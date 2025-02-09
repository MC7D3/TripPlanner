package tpgroup.view.cli;


import tpgroup.controller.RoomController;
import tpgroup.model.Session;
import tpgroup.model.domain.Room;
import tpgroup.view.cli.template.CliSelectItemFormState;

public class JoinRoomFormState extends CliSelectItemFormState<Room>{

	JoinRoomFormState(CliView sm) {
		super(sm, RoomController.getJoinedRooms(), true);
	}

	@Override
	protected void handleChosenElement(Room chosen) {
		if(chosen == null) {
			this.machine.setState(new LoggedMenuState(this.machine));
		}
		RoomController.enterRoom(chosen);
		if(chosen.getAdmin().equals(Session.getInstance().getLogged())){
			this.machine.setState(new RoomAdminMenuState(this.machine));
		}else{
			this.machine.setState(new RoomMemberMenuState(this.machine));
		}
		
	}

}
