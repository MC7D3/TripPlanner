package tpgroup.view.cli.template;

import java.io.IOException;

import tpgroup.view.cli.CliView;
import tpgroup.view.cli.CliViewState;

public abstract class ConfirmationForm extends CliViewState{

	public ConfirmationForm(CliView sm){
		super(sm);
	}

	@Override
	public void show() {
		String answer = "";
		try{
			promptText();
			System.out.print(">");
			answer = in.readLine();
		}catch(IOException e){
			System.err.println("ERROR: unable to process input");
		}
		handleAnswer(answer);
	}

	public abstract void handleAnswer(String answer);
	
	public abstract void promptText();
}

