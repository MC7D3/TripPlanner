package tpgroup.view.cli;

import tpgroup.controller.graphical.cli.AuthGController;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.stateMachine.CliViewState;

public class RegistrationFormState extends CliViewState{

	public RegistrationFormState() {
		super();
	}

	@Override
	public void present() {
		try {
			FormFieldFactory ref = FormFieldFactory.getInstance();
			System.out.println("NOTE: if you want to go back keep all field blank");
			System.out.println("the password must have at least 8 characters, an upper and lowercase letter, a symbol and a number");
			String email = ref.newDefault("email:", str -> str).get();
			String password = ref.newPwdField("password").get();
			String confPassword = ref.newPwdField("confirm password").get();
			CliViewState nextState = AuthGController.register(email, password, confPassword);
			this.machine.setState(nextState);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

}
