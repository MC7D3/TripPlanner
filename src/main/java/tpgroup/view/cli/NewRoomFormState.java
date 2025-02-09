package tpgroup.view.cli;

import java.io.IOException;

import tpgroup.model.RoomBean;
import tpgroup.model.exception.InvalidBeanParamException;
import tpgroup.model.exception.RoomGenConflictException;
import tpgroup.controller.RoomController;

public class NewRoomFormState extends CliViewState{
	private String location;

	public NewRoomFormState(CliView sm) {
	 	super(sm);
	}

	@Override
	public void show() {
		RoomBean newRoom = null;
		this.machine.setState(new LoggedMenuState(this.machine));
		try {
			System.out.println("NOTE: if you want to go back keep the field blank");
			System.out.print("room's name:");
			String name = in.readLine();
			System.out.println("trip destination:");
			String destination = in.readLine();
			if(name.isEmpty() && destination.isEmpty()){
				return;
			}
			newRoom = new RoomBean(name, location);
			attemptRoomCreation(newRoom, 3);
			System.out.println("ERROR: too many rooms with this name are present, try another one");
		} catch (IOException e) {
			System.err.println("ERROR: unable to gather the new room informations");
		} catch (InvalidBeanParamException e2){
			System.err.println("ERROR: " + e2.getMessage());
		}
	}

	private boolean attemptRoomCreation(RoomBean newRoom, int attempts){
		for(int attempt = 0; attempt < attempts; attempt++){
			try{
				RoomController.createRoom(newRoom);
				System.out.println("room created successfully!");
				return true;
			}catch(RoomGenConflictException e){
				//it does another attempt, no actions needed
			}
		}
		return false;
	}

}

