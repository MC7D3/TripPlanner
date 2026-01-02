package tpgroup.view.cli;

import tpgroup.controller.graphical.cli.LoggedMenuGController;
import tpgroup.model.domain.Room;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.statemachine.CliViewState;

public class EnterRoomFormState extends CliViewState {

	public EnterRoomFormState() {
		super();
	}

	@Override
	public void present() {
		try {
			Room chosen = FormFieldFactory.getInstance()
					.newSelectItem("select a room to enter:", LoggedMenuGController.getJoinedRooms(), true).get();
			CliViewState next = LoggedMenuGController.enterRoom(chosen);
			this.machine.setState(next);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

}
