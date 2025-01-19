package tpgroup.view.cli;

import java.io.IOException;

import tpgroup.controller.LoginController;

public class LoggedMenuState extends CliViewState{

	public LoggedMenuState(CliView sm) {
		super(sm);
	}

	@Override
	public void show() {
		int choice = 0;
		boolean outOfRange; 
		do{
			System.out.println("1. create new room");
			System.out.println("2. join room");
			System.out.println("3. abbandon room");
			System.out.println("4. options");
			System.out.println("5. logout");
			System.out.println("6. exit");
			System.out.print(">");
			boolean isInt;
			try {
				choice = Integer.parseInt(in.readLine());
				isInt = true;
			} catch (IOException | NumberFormatException e) {
				isInt = false;
			}
			outOfRange = choice < 1 || choice > 6;
			System.err.print(!isInt || outOfRange? "\nERROR selecting menu option\n": "");
		}while(outOfRange);
		switch(choice){
			case 1 -> this.machine.setState(new NewRoomFormState(this.machine));
			case 2 -> this.machine.setState(new JoinRoomFormState(this.machine));
			case 3 -> this.machine.setState(new abbandonRoomFormState(this.machine));
			case 4 -> this.machine.setState(new OptionsMenuState(this.machine));
			case 5 -> {
				LoginController.logout();
				this.machine.setState(new UnloggedMenuState(this.machine));
			}
			default -> System.exit(0);
		}
	}

}

