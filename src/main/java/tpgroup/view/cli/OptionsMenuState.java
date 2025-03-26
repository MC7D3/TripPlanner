package tpgroup.view.cli;

import java.util.List;

import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;

public class OptionsMenuState extends CliViewState{


	protected OptionsMenuState(CliView machine) {
		super(machine);
	}

	@Override
	public void show() {
		String choice = "";
		try {
			choice = FormFieldFactory.getInstance().newSelectItem(List.of("change password", "delete account", "go back")).get();
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
		switch(choice){
			case "change password" -> this.machine.setState(new UpdatePwdFormState(this.machine));
			case "delete account" -> this.machine.setState(new DeleteAccountConfForm(this.machine));
			default -> this.machine.setState(new LoggedMenuState(this.machine));
		}
	}

}
