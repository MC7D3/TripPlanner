package tpgroup.view.cli;

import java.util.List;

import tpgroup.view.cli.template.CliMenuState;
import tpgroup.view.cli.template.ConfirmationForm;

public class OptionsMenuState extends CliMenuState{

	public OptionsMenuState(CliView sm) {
		super(sm, List.of("update informations", "delete account", "go back"));
	}

	@Override
	protected void handleChoice(int choice) {
		switch(choice){
			case 1 -> this.machine.setState(new UpdateCredentialsFormState(this.machine));
			case 2 -> this.machine.setState(new ConfirmationForm(this.machine,
				"account deleted succesfully!",
				new UnloggedMenuState(this.machine),
				new LoggedMenuState(this.machine)
			));
			default -> this.machine.setState(new LoggedMenuState(this.machine));
		}
	}

}
