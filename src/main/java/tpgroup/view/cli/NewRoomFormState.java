package tpgroup.view.cli;


import tpgroup.model.RoomBean;
import tpgroup.model.exception.InvalidBeanParamException;
import tpgroup.model.exception.RoomGenConflictException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.controller.RoomController;

public class NewRoomFormState extends CliViewState{
	private String location;

	public NewRoomFormState(CliView sm) {
	 	super(sm);
	}

	@Override
	public void show() {
		RoomBean newRoom = null;
		try {
			FormFieldFactory ref = FormFieldFactory.getInstance();
			System.out.println("NOTE: if you want to go back keep the field blank");
			String name = ref.newDefault("room's name:", str -> str).get();
			String destination = ref.newDefault("trip destination:", str -> str).get();
			if(name.isEmpty() && destination.isEmpty()){
				return;
			}
			newRoom = new RoomBean(name, location);
			attemptRoomCreation(newRoom, 3);
			System.out.println("ERROR: too many rooms with this name are present, try another one");
			this.machine.setState(new RoomAdminMenuState(this.machine));
		} catch (InvalidBeanParamException e2){
			System.err.println("ERROR: " + e2.getMessage());
			this.machine.setState(new LoggedMenuState(this.machine));
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

