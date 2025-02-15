package tpgroup.view.cli;

import tpgroup.model.EmailBean;
import tpgroup.model.PwdBean;
import tpgroup.model.exception.InvalidBeanParamException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.controller.AuthController;

public class LoginFormState extends CliViewState {

	public LoginFormState(CliView sm) {
		super(sm);
	}

	@Override
	public void show() {
		CliViewState nextState = new LoggedMenuState(this.machine);
		boolean result;
		do {
			result = false;
			try {
				FormFieldFactory ref = FormFieldFactory.getInstance();
				System.out.println("NOTE: if u want to go back keep both field blank");
				String email = ref.newDefault("email:", str -> str).get();
				String password = ref.newPwdField("password:").get();
				if (email.isEmpty() && password.isEmpty()) {
					nextState = new UnloggedMenuState(this.machine);
					break;
				}
				result = AuthController.validateCredentials(new EmailBean(email), new PwdBean(password));
			} catch (InvalidBeanParamException e2) {
				System.err.println("ERROR: " + e2.getMessage());
			}
			System.out.println(result ? "login succesfull!" : "email or password incorrect, try again");
		} while (!result);
		this.machine.setState(nextState);
	}

}
