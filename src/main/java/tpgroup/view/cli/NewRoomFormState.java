package tpgroup.view.cli;

import java.io.IOException;

import tpgroup.model.RoomBean;
import tpgroup.model.Session;
import tpgroup.model.exception.InvalidBeanParamException;
import tpgroup.model.exception.RoomGenConflictException;
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
			System.out.println("NOTE: if you want to go back keep the field blank");
			System.out.print("room's name:");
			String name = in.readLine();
			if(name.isEmpty()){
				return;
			}
			newRoom = new RoomBean(name);
			for(int attempt = 0; attempt < 3; attempt++){
				try{
					RoomController.createRoom(Session.getInstance().getLogged(), newRoom);
					System.out.println("room created successfully!");
					return;
				}catch(RoomGenConflictException e){}
			}
			System.out.println("ERROR: too many rooms with this name are present, try another one");
		} catch (IOException e) {
			System.err.println("ERROR: unable to gather the new room informations");
		} catch (InvalidBeanParamException e2){
			System.err.println("ERROR: " + e2.getMessage());
		}
	}

}

