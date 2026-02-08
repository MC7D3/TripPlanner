package tpgroup.view.cli;

import tpgroup.controller.graphical.cli.LoggedMenuGController;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.statemachine.CliViewState;

public class UpdatePwdFormState extends CliViewState {

	private final LoggedMenuGController loggedMenuGCtrl = new LoggedMenuGController();

	public UpdatePwdFormState() {
		super();
	}

	@Override
	public void present() {
		try {
			FormFieldFactory ref = FormFieldFactory.getInstance();
			System.out.println("NOTE: keep the fields you want to keep the password unchanged");
			String password = ref.newPwdField("new password").get();
			String confPwd = ref.newPwdField("confirm new password:").get();
			CliViewState next = loggedMenuGCtrl.updatePwd(password, confPwd);
			this.machine.setState(next);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}
}
