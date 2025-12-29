package tpgroup.view.cli;

import java.util.List;

import tpgroup.controller.graphical.cli.LoggedMenuGController;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.stateMachine.CliViewState;

public class OptionsMenuState extends CliViewState{


	public OptionsMenuState() {
		super();
	}

	@Override
	public void present() {
		try {
			String choice = "";
			choice = FormFieldFactory.getInstance().newSelectItem(List.of("change password", "delete account", "go back")).get();
			CliViewState next = LoggedMenuGController.processOptionsChoice(choice);
			this.machine.setState(next);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

}
