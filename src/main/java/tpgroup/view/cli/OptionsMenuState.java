package tpgroup.view.cli;

import java.io.IOException;

public class OptionsMenuState extends CliViewState {

	public OptionsMenuState(CliView sm) {
		super(sm);
	}

	@Override
	public void show() {
		int choice = 0;
		boolean outOfRange; 
		do{
			System.out.println("1. update informations");
			System.out.println("2. delete account");
			System.out.println("3. go back");
			System.out.print(">");
			boolean isInt;
			try {
				choice = Integer.parseInt(in.readLine());
				isInt = true;
			} catch (IOException | NumberFormatException e) {
				isInt = false;
			}
			outOfRange = choice < 1 || choice > 4;
			System.err.print(!isInt || outOfRange? "\nERROR selecting menu option\n": "");
		}while(outOfRange);
		switch(choice){
			case 1 -> this.machine.setState(new UpdateCredentialsFormState(this.machine));
			case 3 -> this.machine.setState(new ConfirmationForm(this.machine,
				"Account deleted succesfully",
				new UnloggedMenuState(this.machine),
				new LoggedMenuState(this.machine)
			));
			default -> this.machine.setState(new LoggedMenuState(this.machine));
		}

	}

}
