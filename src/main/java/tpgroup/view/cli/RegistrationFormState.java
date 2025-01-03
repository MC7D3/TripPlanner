package tpgroup.view.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import tpgroup.controller.RegistrationController;
import tpgroup.model.RegistrationCredBean;
import tpgroup.model.exception.InvalidBeanParamException;

public class RegistrationFormState extends CliViewState{
	BufferedReader in;
	
	public RegistrationFormState(CliView sm) {
		super(sm);
		in = new BufferedReader(new InputStreamReader(System.in));
	}

	private String pwdRead() throws IOException{ //TODO testing funzionalita
		StringBuilder password = new StringBuilder(); 
		for(char pwd_char = (char) in.read(); pwd_char != '\n' && ((int) pwd_char) != -1; pwd_char = (char) in.read()){
			System.out.println("\b*");
			password.append(pwd_char);
		}
		return password.toString();
	}

	@Override
	public void show() {
		CliViewState nextState = new LoggedMenuState(this.machine);
		Boolean validCredentials;
		RegistrationCredBean credentials = null;
		do{
			validCredentials = false;
			try {
				System.out.println("NOTE: if u want to go back keep all field blank\n");
				System.out.print("email:");
				String email = in.readLine();
				System.out.print("password:");
				String password = pwdRead();
				System.out.print("comfirm password:");
				String confPassword = pwdRead();
				if(email.isEmpty() && password.isEmpty() && confPassword.isEmpty()){
					nextState = new UnloggedMenuState(this.machine);
					break;
				}
				credentials = new RegistrationCredBean(email, password, confPassword);
				validCredentials = true;
			} catch (IOException e) {
				System.err.println("ERROR: unable to process inserted credentials");
			} catch (InvalidBeanParamException e2){
				System.err.println("ERROR: " + e2.getMessage());
			}
		}while(!validCredentials || !RegistrationController.executeRegistration(credentials));
		this.machine.setState(nextState);
	}
	
}


