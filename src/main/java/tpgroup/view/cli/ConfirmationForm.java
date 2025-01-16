package tpgroup.view.cli;

import java.io.IOException;

import tpgroup.controller.OptionsController;

public class ConfirmationForm extends CliViewState{
	private String confirmMsg;
	private CliViewState nextYes;
	private CliViewState nextNo;

	public ConfirmationForm(CliView sm, String confirmMsg, CliViewState nextYes, CliViewState nextNo) {
		super(sm);
	}

	@Override
	public void show() {
		CliViewState nextState = this.nextYes; 
		try{
			System.out.println("are u sure u want to proceed, this cannot be undone? yes/no(def)");
			System.out.print(">");
			String answer = in.readLine();
			if(answer.equals("yes")){
				OptionsController.deleteLoggedAccount();
				System.out.println(this.confirmMsg);
				nextState = this.nextNo;
			}
		}catch(IOException e){
			System.err.println("ERROR: unable to process input");
		}
		this.machine.setState(nextState);
	}

}

