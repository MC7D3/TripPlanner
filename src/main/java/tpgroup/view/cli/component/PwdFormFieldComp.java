package tpgroup.view.cli.component;

import java.io.Console;

public class PwdFormFieldComp implements FormFieldComp<String>{
	private final Console in = System.console();
	private final String prompt;

	public PwdFormFieldComp(String prompt) {
		super();
		this.prompt = prompt;
	}

	@Override
	public String get() {
		System.out.println(prompt);
		System.out.print(">");
		return new String(in.readPassword());
	}
	
	
}
