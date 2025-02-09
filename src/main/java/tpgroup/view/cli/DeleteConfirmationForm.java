package tpgroup.view.cli;

import tpgroup.controller.OptionsController;
import tpgroup.view.cli.template.ConfirmationForm;

public class DeleteConfirmationForm extends ConfirmationForm {

	public DeleteConfirmationForm(CliView sm) {
		super(sm);
	}

	@Override
	public void handleAnswer(String answer) {
		if (answer.equals("yes")) {
			OptionsController.deleteAccount();
			System.out.println("account deleted succesfully!");
			this.machine.setState(new UnloggedMenuState(this.machine));
		}
		this.machine.setState(new LoggedMenuState(this.machine));
	}

	@Override
	public void promptText() {
		System.out.println("are you sure you want to proceed, this operation cannot be undone (y/n):");
	}

}
