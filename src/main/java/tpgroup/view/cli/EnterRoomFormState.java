package tpgroup.view.cli;

import tpgroup.controller.graphical.cli.LoggedMenuGController;
import tpgroup.model.bean.RoomBean;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.statemachine.CliViewState;

public class EnterRoomFormState extends CliViewState {

	private final LoggedMenuGController loggedMenuGCtrl = new LoggedMenuGController();

	public EnterRoomFormState() {
		super();
	}

	@Override
	public void present() {
		try {
			RoomBean chosen = FormFieldFactory.getInstance()
					.newSelectItem("select a room to enter:", loggedMenuGCtrl.getJoinedRooms(), true).get();
			CliViewState next = loggedMenuGCtrl.enterRoom(chosen);
			this.machine.setState(next);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

}
