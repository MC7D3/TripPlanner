package tpgroup.view.cli;

import java.util.List;

import tpgroup.controller.LoginController;
import tpgroup.view.cli.template.CliMenuState;

public class LoggedMenuState extends CliMenuState{

	public LoggedMenuState(CliView sm) {
		super(sm, List.of("create new room", "join room", "abbandon room", "options", "logout", "exit"));
	}

	@Override
	protected void handleChoice(int choice) {
		switch(choice){
			case 1 -> this.machine.setState(new NewRoomFormState(this.machine));
			case 2 -> this.machine.setState(new JoinRoomFormState(this.machine));
			case 3 -> this.machine.setState(new AbbandonRoomFormState(this.machine));
			case 4 -> this.machine.setState(new OptionsMenuState(this.machine));
			case 5 -> {
				LoginController.logout();
				this.machine.setState(new UnloggedMenuState(this.machine));
			}
			default -> System.exit(0);
		}
	}

}

