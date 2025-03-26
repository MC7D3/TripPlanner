package tpgroup.view.cli;

import java.util.List;

import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;

public class UnloggedMenuState extends CliViewState{

	protected UnloggedMenuState(CliView machine) {
		super(machine);
	}

	@Override
	public void show() {
		String choice = "";
		try {
			choice = FormFieldFactory.getInstance().newSelectItem(List.of("login", "register", "exit")).get();
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
		switch(choice){
			case "login" -> this.machine.setState(new LoginFormState(this.machine));
			case "register" -> this.machine.setState(new RegistrationFormState(this.machine));
			default -> System.exit(0);
		}
	}
	
}
