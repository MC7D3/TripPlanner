package tpgroup.view.cli.component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Function;

import tpgroup.model.exception.FormFieldIOException;

public class DefaultFormFieldComp<T> implements FormFieldComp<T>{
	private static final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	private final String prompt;
	private final Function<String, T> conversion;

	public DefaultFormFieldComp(String prompt, Function<String, T> conversion) {
		super();
		this.prompt = prompt;
		this.conversion = conversion;
	}

	@Override
	public T get() throws FormFieldIOException{
		try {
			System.out.println(prompt);
			System.out.print(">");
			return conversion.apply(in.readLine());
		} catch (IOException e) {
			throw new FormFieldIOException(e);
		}
	}
	
}
