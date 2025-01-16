package tpgroup.view.cli;

import java.io.IOException;

public class UnloggedMenuState extends CliViewState{
	
	public UnloggedMenuState(CliView sm) {
		super(sm);
	}

	@Override
	public void show() {
		int choice = 0;
		boolean outOfRange; 
		do{
			System.out.println("1. login");
			System.out.println("2. register");
			System.out.println("3. exit");
			System.out.print(">");
			boolean isInt;
			try {
				choice = Integer.parseInt(in.readLine());
				isInt = true;
			} catch (IOException | NumberFormatException e) {
				isInt = false;
			}
			outOfRange = choice < 1 || choice > 3;
			System.err.print(!isInt || outOfRange? "\nERROR selecting menu option\n": "");
		}while(outOfRange);
		switch(choice){
			case 1 -> this.machine.setState(new LoginFormState(this.machine));
			case 2 -> this.machine.setState(new RegistrationFormState(this.machine));
			default -> System.exit(0);
		}
	}
	
}
