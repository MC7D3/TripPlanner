package tpgroup.view.cli;

import java.io.IOException;
import java.util.Optional;

import tpgroup.controller.OptionsController;
import tpgroup.model.PwdBean;
import tpgroup.model.exception.InvalidBeanParamException;

public class UpdateCredentialsFormState extends CliViewState{

	public UpdateCredentialsFormState(CliView sm) {
		super(sm);
	}

	@Override
	public void show() {
		try {
			System.out.println("current credentials:");
			System.out.println("NOTE: keep the fields u want to keep unchanged empty");
			System.out.print("password:");
			String password = in.readLine();
			System.out.print("confirm password:");
			String confPwd = in.readLine();

			if(!password.isEmpty()){
				Optional<PwdBean> passwordBean = Optional.ofNullable(new PwdBean(password, confPwd));
				OptionsController.updateCredentials(passwordBean);
			}
		} catch (IOException e) {
			System.err.println("ERROR: unable to process inserted credentials");
		} catch (InvalidBeanParamException e2){
			System.err.println("ERROR: " + e2.getMessage());
		}
		this.machine.setState(new LoggedMenuState(this.machine));
	}
}

