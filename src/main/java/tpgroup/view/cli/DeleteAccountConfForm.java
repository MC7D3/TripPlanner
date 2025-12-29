package tpgroup.view.cli;

import tpgroup.controller.graphical.cli.LoggedMenuGController;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.stateMachine.CliViewState;

public class DeleteAccountConfForm extends CliViewState {

	public DeleteAccountConfForm() {
		super();
	}

	@Override
	public void present() {
		try {
			boolean answer = FormFieldFactory.getInstance()
					.newConfField("are you sure you want to proceed, this operation cannot be undone (y/n):").get();
			CliViewState next = LoggedMenuGController.processDeleteRequest(answer);
			this.machine.setState(next);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

}
