package tpgroup.view.cli;

import tpgroup.controller.OptionsController;
import tpgroup.view.cli.component.FormFieldFactory;

public class DeleteAccountConfForm extends CliViewState{


	protected DeleteAccountConfForm(CliView machine) {
		super(machine);
	}

	@Override
	public void show() {
		boolean answer = FormFieldFactory.getInstance().newConfField("are you sure you want to proceed, this operation cannot be undone (y/n):").get();
		if(answer){
			OptionsController.deleteAccount();
			System.out.println("account deleted succesfully!");
			this.machine.setState(new UnloggedMenuState(this.machine));
		}
		this.machine.setState(new LoggedMenuState(this.machine));
	}

}
