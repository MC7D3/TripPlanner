package tpgroup.view.cli;

import tpgroup.controller.AuthController;
import tpgroup.model.EmailBean;
import tpgroup.model.PwdBean;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.model.exception.InvalidBeanParamException;
import tpgroup.view.cli.component.FormFieldFactory;

public class RegistrationFormState extends CliViewState{

	public RegistrationFormState(CliView sm) {
		super(sm);
	}

	@Override
	public void show() {
		CliViewState nextState = new LoggedMenuState(this.machine);
		boolean validCredentials;
		boolean registrationRes;
		do {
			validCredentials = false;
			registrationRes = false;
			try {
				FormFieldFactory ref = FormFieldFactory.getInstance();
				System.out.println("NOTE: if you want to go back keep all field blank");
				System.out.println("the password must have at least 8 characters, an upper and lowercase letter, a symbol and a number");
				String email = ref.newDefault("email:", str -> str).get();
				String password = ref.newPwdField("password").get();
				String confPassword = ref.newPwdField("confirm password").get();
				if (email.isEmpty() && password.isEmpty() && confPassword.isEmpty()) {
					nextState = new UnloggedMenuState(this.machine);
					break;
				}
				EmailBean emailBean = new EmailBean(email);
				PwdBean passwordBean = new PwdBean(password, confPassword);
				validCredentials = true;
				registrationRes = AuthController.executeRegistration(emailBean, passwordBean);
				if(!registrationRes){
					System.out.println("ERROR: there is already an account binded to the inserted email");
				}
				System.out.println("registration succesfull!");
			} catch (InvalidBeanParamException | FormFieldIOException e) {
				System.err.println("ERROR: " + e.getMessage());
			}
		} while (!validCredentials || !registrationRes);
		this.machine.setState(nextState);
	}

}
