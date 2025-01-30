package tpgroup.view.cli;

import java.util.List;

import tpgroup.view.cli.template.CliMenuState;

public class UnloggedMenuState extends CliMenuState{
	
	public UnloggedMenuState(CliView sm) {
		super(sm, List.of("login", "register", "exit"));
	}

	@Override
	protected void handleChoice(int choice) {
		switch(choice){
			case 1 -> this.machine.setState(new LoginFormState(this.machine));
			case 2 -> this.machine.setState(new RegistrationFormState(this.machine));
			default -> System.exit(0);
		}
	}
	
}
