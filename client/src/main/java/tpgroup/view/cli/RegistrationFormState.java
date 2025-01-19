package tpgroup.view.cli;

import java.io.IOException;

import tpgroup.controller.RegistrationController;
import tpgroup.model.EmailBean;
import tpgroup.model.PwdBean;
import tpgroup.model.exception.InvalidBeanParamException;

public class RegistrationFormState extends LoginFormState {

	public RegistrationFormState(CliView sm) {
		super(sm);
	}

	@Override
	public void show() {
		CliViewState nextState = new LoggedMenuState(this.machine);
		boolean validCredentials;
		String email;
		String password;
		String confPassword;
		EmailBean emailBean = null;
		PwdBean passwordBean = null;
		do {
			validCredentials = false;
			try {
				System.out.println("NOTE: if u want to go back keep all field blank");
				System.out.print("email:");
				email = in.readLine();
				System.out.print("password:");
				password = pwdRead();
				System.out.print("comfirm password:");
				confPassword = pwdRead();
				if (email.isEmpty() && password.isEmpty() && confPassword.isEmpty()) {
					nextState = new UnloggedMenuState(this.machine);
					break;
				}
				emailBean = new EmailBean(email);
				passwordBean = new PwdBean(password, confPassword);
				validCredentials = true;
			} catch (IOException e) {
				System.err.println("ERROR: unable to process inserted credentials");
			} catch (InvalidBeanParamException e2) {
				System.err.println("ERROR: " + e2.getMessage());
			}
		} while (!validCredentials || !RegistrationController.executeRegistration(emailBean, passwordBean));
		this.machine.setState(nextState);
	}

}
