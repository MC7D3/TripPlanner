package tpgroup.view.cli;

import java.io.IOException;

import tpgroup.controller.RegistrationController;
import tpgroup.model.EmailBean;
import tpgroup.model.PwdBean;
import tpgroup.model.exception.InvalidBeanParamException;

public class RegistrationFormState extends CliViewState{

	public RegistrationFormState(CliView sm) {
		super(sm);
	}

	@Override
	public void show() {
		CliViewState nextState = new LoggedMenuState(this.machine);
		boolean validCredentials;
		boolean registrationRes;
		String email;
		String password;
		String confPassword;
		EmailBean emailBean = null;
		PwdBean passwordBean = null;
		do {
			validCredentials = false;
			registrationRes = false;
			try {
				System.out.println("NOTE: if you want to go back keep all field blank");
				System.out.print("email:");
				email = in.readLine();
				System.out.print("password:");
				password = new String(System.console().readPassword()); 
				System.out.print("comfirm password:");
				confPassword = new String(System.console().readPassword());
				if (email.isEmpty() && password.isEmpty() && confPassword.isEmpty()) {
					nextState = new UnloggedMenuState(this.machine);
					break;
				}
				emailBean = new EmailBean(email);
				passwordBean = new PwdBean(password, confPassword);
				validCredentials = true;
				registrationRes = RegistrationController.executeRegistration(emailBean, passwordBean);
				if(!registrationRes){
					System.out.println("ERROR: there is already an account binded to the inserted email");
				}
				System.out.println("registration succesfull!");
			} catch (IOException e) {
				System.err.println("ERROR: unable to process inserted credentials");
			} catch (InvalidBeanParamException e2) {
				System.err.println("ERROR: " + e2.getMessage());
			}
		} while (!validCredentials || !registrationRes);
		this.machine.setState(nextState);
	}

}
