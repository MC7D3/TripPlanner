package tpgroup.view.cli;

import tpgroup.controller.graphical.cli.LoggedMenuGController;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.statemachine.CliViewState;

public class JoinRoomFormState extends CliViewState {

	private final LoggedMenuGController loggedMenuGCtrl = new LoggedMenuGController();

	public JoinRoomFormState() {
		super();
	}

	@Override
	public void present() {
		try {
			System.out.println("NOTE: if you want to go back keep the field blank");
			String roomCode = FormFieldFactory.getInstance().newDefault("room's code:", str -> str).get();
			CliViewState next = loggedMenuGCtrl.joinRoom(roomCode);
			this.machine.setState(next);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

}
