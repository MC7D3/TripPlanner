package tpgroup.view.cli;

import tpgroup.controller.RoomController;
import tpgroup.model.RoomCodeBean;
import tpgroup.model.exception.InvalidBeanParamException;
import tpgroup.view.cli.component.FormFieldFactory;

public class JoinRoomFormState extends CliViewState{

	protected JoinRoomFormState(CliView machine) {
		super(machine);
	}

	@Override
	public void show() {
		try {
			String roomCode = FormFieldFactory.getInstance().newDefault("room's code:", str -> str).get();
			if(RoomController.joinRoom(new RoomCodeBean(roomCode))){
				System.out.println("room joined successfully!");
				this.machine.setState(new RoomMemberMenuState(this.machine));
			}
		} catch (InvalidBeanParamException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
		this.machine.setState(new LoggedMenuState(this.machine));
	}

}
