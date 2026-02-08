package tpgroup.controller.graphical.cli;

import tpgroup.controller.AuthController;
import tpgroup.model.bean.UserBean;
import tpgroup.model.exception.InvalidBeanParamException;
import tpgroup.view.cli.LoggedMenuState;
import tpgroup.view.cli.LoginFormState;
import tpgroup.view.cli.RegistrationFormState;
import tpgroup.view.cli.UnloggedMenuState;
import tpgroup.view.cli.statemachine.CliViewState;

public class AuthGController {

	private final AuthController authCtrl = new AuthController();

	public AuthGController() {
		super();
	}

	public CliViewState login(String email, String password) {
		if (email.isEmpty() && password.isEmpty()) {
			return new UnloggedMenuState();
		}

		CliViewState ret = new LoginFormState();
		try {
			boolean result = authCtrl.validateCredentials(new UserBean(email, password));
			if (result) {
				ret = new LoggedMenuState();
				ret.setOutLogTxt("login Succesful");
			}else{
				ret.setOutLogTxt("wrong credentials");
			}
		} catch (InvalidBeanParamException e) {
			ret.setOutLogTxt("ERROR: " + e.getMessage());
		}
		return ret;
	}

	public CliViewState register(String email, String password, String confPassword){
		CliViewState ret = new RegistrationFormState();
		if (email.isEmpty() && password.isEmpty() && confPassword.isEmpty()) {
			return new UnloggedMenuState();
		}
		try {
			boolean result = authCtrl.executeRegistration(new UserBean(email, password, confPassword));
			if(result) {
				ret = new LoggedMenuState();
				ret.setOutLogTxt("registration Succesful");
			}else{
				ret.setOutLogTxt("ERROR: there is already an account binded to the inserted email");
			}
		} catch (InvalidBeanParamException e) {
			ret.setOutLogTxt("ERROR: " + e.getMessage());
		}
		return ret;
	}

}
