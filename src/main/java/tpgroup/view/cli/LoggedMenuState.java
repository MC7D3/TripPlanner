package tpgroup.view.cli;

import java.util.List;

import tpgroup.model.Session;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;

public class LoggedMenuState extends CliViewState{

	protected LoggedMenuState(CliView machine) {
		super(machine);
	}

	@Override
	public void show() {
		String choice = "";
		try {
			choice = FormFieldFactory.getInstance().newSelectItem(List.of("create new room", "join room", "enter room", "abbandon room", "options", "logout", "exit")).get();
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
		switch(choice){
			case "create new room" -> this.machine.setState(new NewRoomFormState(this.machine));
			case "join room" -> this.machine.setState(new JoinRoomFormState(this.machine));
			case "enter room" -> this.machine.setState(new EnterRoomFormState(this.machine));
			case "abbandon room" -> this.machine.setState(new AbbandonRoomFormState(this.machine));
			case "options" -> this.machine.setState(new OptionsMenuState(this.machine));
			case "logout" -> {
				Session.resetSession();
				this.machine.setState(new UnloggedMenuState(this.machine));
			}
			default -> System.exit(0);
		}
	}

}

