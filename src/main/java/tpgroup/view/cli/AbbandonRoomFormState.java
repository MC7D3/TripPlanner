package tpgroup.view.cli;

import tpgroup.controller.graphical.cli.LoggedMenuGController;
import tpgroup.model.bean.RoomBean;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.statemachine.CliViewState;

public class AbbandonRoomFormState extends CliViewState{

	private final LoggedMenuGController loggedMenuGCtrl = new LoggedMenuGController();

	public AbbandonRoomFormState() {
		super();
	}

	@Override
	public void present() {
		try {
		RoomBean chosen = FormFieldFactory.getInstance().newSelectItem("select room to leave:", loggedMenuGCtrl.getJoinedRooms(), true).get();
			CliViewState next = loggedMenuGCtrl.abbandonRoom(chosen);
			System.out.println("room abbandoned succesfully!");
			this.machine.setState(next);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}
}
