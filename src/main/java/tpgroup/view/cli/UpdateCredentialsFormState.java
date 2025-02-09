package tpgroup.view.cli;

import tpgroup.controller.OptionsController;
import tpgroup.model.PwdBean;
import tpgroup.model.exception.InvalidBeanParamException;

public class UpdateCredentialsFormState extends CliViewState{

	public UpdateCredentialsFormState(CliView sm) {
		super(sm);
	}

	@Override
	public void show() {
		CliViewState next = new OptionsMenuState(this.machine);
		try {
			System.out.println("current credentials:");
			System.out.println("NOTE: keep the fields you want to keep unchanged empty");
			System.out.print("password:");
			String password = new String(System.console().readPassword());
			System.out.print("confirm password:");
			String confPwd = new String(System.console().readPassword());

			if(!password.isEmpty() && !confPwd.isEmpty()){
				PwdBean passwordBean = new PwdBean(password, confPwd);
				OptionsController.updatePassword(passwordBean);
				System.out.println("credentials updated succesfully!");
			}
			next = new LoggedMenuState(this.machine);
		} catch (InvalidBeanParamException e2){
			System.err.println("ERROR: " + e2.getMessage());
		}
		this.machine.setState(next);
	}
}

