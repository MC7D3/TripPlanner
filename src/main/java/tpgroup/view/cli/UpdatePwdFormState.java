package tpgroup.view.cli;

import tpgroup.controller.OptionsController;
import tpgroup.model.PwdBean;
import tpgroup.model.exception.InvalidBeanParamException;
import tpgroup.view.cli.component.FormFieldFactory;

public class UpdatePwdFormState extends CliViewState{

	public UpdatePwdFormState(CliView sm) {
		super(sm);
	}

	@Override
	public void show() {
		try {
			FormFieldFactory ref = FormFieldFactory.getInstance();
			System.out.println("NOTE: keep the fields you want to keep the password unchanged");
			String password = ref.newPwdField("new password").get();
			String confPwd = ref.newPwdField("confirm new password:").get();
			if(!password.isEmpty() && !confPwd.isEmpty()){
				PwdBean passwordBean = new PwdBean(password, confPwd);
				OptionsController.updatePassword(passwordBean);
				System.out.println("password updated succesfully!");
			}
		} catch (InvalidBeanParamException e2){
			System.err.println("ERROR: " + e2.getMessage());
		}
		this.machine.setState(new OptionsMenuState(this.machine));
	}
}

