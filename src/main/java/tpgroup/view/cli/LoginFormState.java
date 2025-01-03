package tpgroup.view.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import tpgroup.model.LoginCredBean;
import tpgroup.model.exception.InvalidBeanParamException;

import tpgroup.controller.LoginController;

public class LoginFormState extends CliViewState{
	BufferedReader in;
	
	public LoginFormState(CliView sm) {
		super(sm);
		in = new BufferedReader(new InputStreamReader(System.in));
	}

	private String pwdRead() throws IOException{ //TODO testing funzionalita
		String password = "";
		for(char pwd_char = (char) in.read(); pwd_char != '\n' && ((int) pwd_char) != -1; pwd_char = (char) in.read()){
			System.out.println("\b*");
			password += pwd_char;
		}
		return password;
	}

	@Override
	public void show() {
		CliViewState nextState = new LoggedMenuState(this.machine);
		Boolean result;
		LoginCredBean credentials = null;
		do{
			result = false;
			try {
				System.out.println("NOTE: if u want to go back keep both field blank\n");
				System.out.print("email:");
				String email = in.readLine();
				System.out.print("password:");
				String password = pwdRead();
				if(email.isEmpty() && password.isEmpty()){
					nextState = new UnloggedMenuState(this.machine);
					break;
				}
				credentials = new LoginCredBean(email, password);
				result = LoginController.validateCredentials(credentials);
			} catch (IOException | InvalidBeanParamException e) {
				System.err.println("ERROR: the credentials inserted are incorrect try again");
			}
			System.out.println(result? "login succesfull!": "email or password incorrect, try again");
		}while(!result);
		this.machine.setState(nextState);
	}
	
}

