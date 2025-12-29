package tpgroup.controller.graphical.cli;

import tpgroup.view.cli.LoginFormState;
import tpgroup.view.cli.RegistrationFormState;
import tpgroup.view.cli.stateMachine.CliViewState;

public class UnloggedGController {
	public static CliViewState process(String choice){
		switch(choice){
			case "login": return new LoginFormState();
			case "register": return new RegistrationFormState();
			default: 
				System.exit(0);
				return null;
		}
	}
	
}
