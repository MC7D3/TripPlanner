package tpgroup.view.cli;

import java.io.IOException;

import tpgroup.model.RoomBean;
import tpgroup.model.exception.InvalidBeanParamException;
import tpgroup.controller.RoomController;

public class NewRoomFormState extends CliViewState {

	public NewRoomFormState(CliView sm) {
	 	super(sm);
	}

	@Override
	public void show() {
		RoomBean newRoom = null;
		try {
			this.machine.setState(new LoggedMenuState(this.machine));
			System.out.println("NOTE: if u want to go back keep both field blank");
			System.out.print("room's name:");
			String name = in.readLine();
			if(name.isEmpty()){
				return;
			}
			newRoom = new RoomBean(name);
			boolean result;
			do{
				result = RoomController.createRoom(newRoom);
			}while(!result);
		} catch (IOException e) {
			System.err.println("ERROR: unable to gather the new room informations");
		} catch (InvalidBeanParamException e2){
			System.err.println("ERROR: " + e2.getMessage());
		}
	}

}

