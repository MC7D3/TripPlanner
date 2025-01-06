package tpgroup.view.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UnloggedMenuState extends CliViewState{
	BufferedReader in;
	
	public UnloggedMenuState(CliView sm) {
		super(sm);
		in = new BufferedReader(new InputStreamReader(System.in));
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
			System.err.println(!isInt || outOfRange? "\nERROR selecting menu option\n": "\n");
		}while(outOfRange);
		switch(choice){
			case 1 -> this.machine.setState(new LoginFormState(this.machine));
			case 2 -> this.machine.setState(new RegistrationFormState(this.machine));
			case 3 -> System.exit(0);
		}
	}
	
}
