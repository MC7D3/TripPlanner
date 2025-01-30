package tpgroup.view.cli;

import java.io.IOException;
import java.util.Optional;

import tpgroup.controller.OptionsController;
import tpgroup.model.PwdBean;
import tpgroup.model.Session;
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
			String password = in.readLine();
			System.out.print("confirm password:");
			String confPwd = in.readLine();

			if(!password.isEmpty()){
				Optional<PwdBean> passwordBean = Optional.ofNullable(new PwdBean(password, confPwd));
				OptionsController.updateCredentials(Session.getInstance().getLogged(), passwordBean);
				System.out.println("credentials updated succesfully!");
			}
			next = new LoggedMenuState(this.machine);
		} catch (IOException e) {
			System.err.println("ERROR: unable to process inserted credentials");
		} catch (InvalidBeanParamException e2){
			System.err.println("ERROR: " + e2.getMessage());
		}
		this.machine.setState(next);
	}
}

