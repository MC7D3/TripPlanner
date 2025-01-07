package tpgroup.view.cli;

import java.io.IOException;

import tpgroup.controller.RegistrationController;
import tpgroup.model.RegistrationCredBean;
import tpgroup.model.exception.InvalidBeanParamException;

public class RegistrationFormState extends LoginFormState{

	public RegistrationFormState(CliView sm) {
		super(sm);
	}

	@Override
	public void show() {
		CliViewState nextState = new LoggedMenuState(this.machine);
		boolean validCredentials;
		RegistrationCredBean credentials = null;
		do{
			validCredentials = false;
			try {
				System.out.println("NOTE: if u want to go back keep all field blank");
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


