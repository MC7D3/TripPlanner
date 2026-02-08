package tpgroup.view.cli;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tpgroup.controller.graphical.cli.RoomGController;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.statemachine.CliViewState;

public class RoomMemberMenuState extends CliViewState {

	protected final List<String> menuOptions;

	private final RoomGController roomGCtrl = new RoomGController();

	public RoomMemberMenuState() {
		super();
		menuOptions = new ArrayList<>();
		Collections.addAll(menuOptions, "propose new Event", "propose event removal", "list other proposals",
				"show trip status", "propose event update", "undo proposal", "go back");
	}

	@Override
	public void present() {
		try {
			String chosen = FormFieldFactory.getInstance().newSelectItem(menuOptions).get();
			CliViewState next = roomGCtrl.process(chosen);
			this.machine.setState(next);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e);
		}
	}

}
