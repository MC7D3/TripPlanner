package tpgroup.view.cli;

import java.util.Collections;

import tpgroup.controller.graphical.cli.RoomGController;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.statemachine.CliViewState;

public class RoomAdminMenuState extends RoomMemberMenuState {

	private final RoomGController roomGCtrl = new RoomGController();

	public RoomAdminMenuState() {
		super();
		Collections.addAll(this.menuOptions, "accept proposal", "create alternative branch", "split branch",
				"connect branches", "disconnect branches", "delete branch", "delete room");
	}

	@Override
	public void present() {
		try {
			String chosen = FormFieldFactory.getInstance().newSelectItem(this.menuOptions).get();
			CliViewState next = roomGCtrl.processAdmin(chosen);
			this.machine.setState(next);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

}
