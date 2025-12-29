package tpgroup.view.cli;

import tpgroup.controller.graphical.cli.LoggedMenuGController;
import tpgroup.model.domain.Room;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.stateMachine.CliViewState;

public class AbbandonRoomFormState extends CliViewState{

	public AbbandonRoomFormState() {
		super();
	}

	@Override
	public void present() {
		try {
			Room chosen = FormFieldFactory.getInstance().newSelectItem("select room to leave:", LoggedMenuGController.getJoinedRooms(), true).get();
			CliViewState next = LoggedMenuGController.abbandonRoom(chosen);
			System.out.println("room abbandoned succesfully!");
			this.machine.setState(next);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}
}
