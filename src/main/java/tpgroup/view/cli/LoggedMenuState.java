package tpgroup.view.cli;

import java.util.List;

import tpgroup.controller.graphical.cli.LoggedMenuGController;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.statemachine.CliViewState;

public class LoggedMenuState extends CliViewState {

	private final LoggedMenuGController loggedMenuGCtrl = new LoggedMenuGController();

	public LoggedMenuState() {
		super();
	}

	@Override
	public void present() {
		try {
			String choice = FormFieldFactory.getInstance().newSelectItem(
					List.of("create new room", "join room", "enter room", "abbandon room", "options", "logout", "exit"))
					.get();
			CliViewState next = loggedMenuGCtrl.process(choice);
			this.machine.setState(next);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

}
