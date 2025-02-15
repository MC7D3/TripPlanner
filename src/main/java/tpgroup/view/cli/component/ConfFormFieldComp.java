package tpgroup.view.cli.component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConfFormFieldComp implements FormFieldComp<Boolean>{
	private static final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	private final String prompt;

	public ConfFormFieldComp(String prompt) {
		super();
		this.prompt = prompt;
	}

	@Override
	public Boolean get() {
		String answer = "";
		try{
			System.out.println(prompt);
			System.out.println("(yes/no must be provided)");
			System.out.print(">");
			answer = in.readLine();
			return answer.equals("yes");
		}catch(IOException e){
			System.err.println("ERROR: unable to process input");
			return false;
		}
	}

}

