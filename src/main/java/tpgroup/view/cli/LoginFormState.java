package tpgroup.view.cli;

import tpgroup.controller.graphical.cli.AuthGController;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.statemachine.CliViewState;

public class LoginFormState extends CliViewState {

	private final AuthGController authGCtrl = new AuthGController();

	public LoginFormState() {
		super();
	}

	@Override
	public void present() {
		try {
			FormFieldFactory ref = FormFieldFactory.getInstance();
			System.out.println("NOTE: if u want to go back keep both field blank");
			String email = ref.newDefault("email:", str -> str).get();
			String password = ref.newPwdField("password:").get();
			CliViewState next = authGCtrl.login(email, password);
			this.machine.setState(next);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

}
