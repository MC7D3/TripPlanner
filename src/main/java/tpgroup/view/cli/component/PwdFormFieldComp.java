package tpgroup.view.cli.component;

import java.io.Console;
import java.io.IOError;

import tpgroup.model.exception.FormFieldIOException;

public class PwdFormFieldComp implements FormFieldComp<String>{
	private final Console in = System.console();
	private final String prompt;

	public PwdFormFieldComp(String prompt) {
		super();
		this.prompt = prompt;
	}

	@Override
	public String get() throws FormFieldIOException{
		try {
			System.out.println(prompt);
			System.out.print(">");
			return new String(in.readPassword());
		} catch (IOError e) {
			throw new FormFieldIOException();
		}
	}
	
	
}
