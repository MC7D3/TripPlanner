package tpgroup.controller.graphical.cli;

import tpgroup.controller.AuthController;
import tpgroup.model.EmailBean;
import tpgroup.model.PwdBean;
import tpgroup.model.exception.InvalidBeanParamException;
import tpgroup.view.cli.LoggedMenuState;
import tpgroup.view.cli.LoginFormState;
import tpgroup.view.cli.RegistrationFormState;
import tpgroup.view.cli.UnloggedMenuState;
import tpgroup.view.cli.stateMachine.CliViewState;

public class AuthGController {
	public static CliViewState login(String email, String password) {
		if (email.isEmpty() && password.isEmpty()) {
			return new UnloggedMenuState();
		}

		CliViewState ret = new LoginFormState();
		try {
			boolean result = AuthController.validateCredentials(new EmailBean(email), new PwdBean(password));
			if (result) {
				ret.setOutLogTxt("login Succesful");
				ret = new LoggedMenuState();
			}
		} catch (InvalidBeanParamException e) {
			ret.setOutLogTxt("ERROR: " + e.getMessage());
		}
		return ret;
	}

	public static CliViewState register(String email, String password, String confPassword){
		CliViewState ret = new RegistrationFormState();
		if (email.isEmpty() && password.isEmpty() && confPassword.isEmpty()) {
			return new UnloggedMenuState();
		}
		try {
			boolean result = AuthController.executeRegistration(new EmailBean(email), new PwdBean(password, confPassword));
			if(result) {
				ret.setOutLogTxt("registration Succesful");
				ret = new LoggedMenuState();
			}else{
				ret.setOutLogTxt("ERROR: there is already an account binded to the inserted email");
			}
		} catch (InvalidBeanParamException e) {
			ret.setOutLogTxt("ERROR: " + e.getMessage());
		}
		return ret;
	}

}
