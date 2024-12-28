package tpgroup.view.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UnloggedMenuState extends CliViewState{
	BufferedReader in;
	
	public UnloggedMenuState(CliViewImpl sm) {
		super(sm);
		in = new BufferedReader(new InputStreamReader(System.in));
	}

	@Override
	public void show() {
		int choice = 0;
		do{
			System.out.println("1. login");
			System.out.println("2. register");
			System.out.print(">");
			try {
				choice = Integer.parseInt(in.readLine());
			} catch (IOException | NumberFormatException e) {
				//TODO: handle exception crea expetion personalizzate;
				System.err.println("ERROR selecting menu option");
				System.exit(-1);
			}
		}while(choice < 1 || choice > 2);
		switch(choice){
			case 1 -> this.machine.setState(new LoginFormState(this.machine));
			case 2 -> this.machine.setState(new RegistrationFormState(this.machine));
		}
	}
	
}
