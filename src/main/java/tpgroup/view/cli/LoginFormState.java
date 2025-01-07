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

	protected String pwdRead() throws IOException{ //TODO testing funzionalita
		StringBuilder password = new StringBuilder();
		for(int pwd_char = in.read(); pwd_char != '\n' && pwd_char != -1; pwd_char = in.read()){
			if(pwd_char != '\b'){
				System.out.print("\b*");
				password.append((char) pwd_char);
			}else if(password.length() > 0){
				password.deleteCharAt(password.length() - 1);
				System.out.print("\b \b");
			}
		}
		return password.toString();
	}

	@Override
	public void show() {
		CliViewState nextState = new LoggedMenuState(this.machine);
		boolean result;
		LoginCredBean credentials = null;
		do{
			result = false;
			try {
				System.out.println("NOTE: if u want to go back keep both field blank");
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
			} catch (IOException e) {
				System.err.println("ERROR: unable to process inserted credentials");
			} catch (InvalidBeanParamException e2){
				System.err.println("ERROR: " + e2.getMessage());
			}
			System.out.println(result? "login succesfull!": "email or password incorrect, try again");
		}while(!result);
		this.machine.setState(nextState);
	}
	
}

