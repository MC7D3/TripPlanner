package tpgroup.view.cli;

import java.util.Collections;

import tpgroup.controller.graphical.cli.RoomGController;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.statemachine.CliViewState;

public class RoomAdminMenuState extends RoomMemberMenuState {

	public RoomAdminMenuState() {
		super();
		Collections.addAll(this.menuOptions, "accept proposals", "create alternative sequence", "split a sequence into two",
					"connect sequences", "disconnect branches", "delete branches", "delete room");
	}

	@Override
	public void present() {
		try {
			String chosen = FormFieldFactory.getInstance().newSelectItem(this.menuOptions).get();
			CliViewState next = RoomGController.processAdmin(chosen);
			this.machine.setState(next);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

}
