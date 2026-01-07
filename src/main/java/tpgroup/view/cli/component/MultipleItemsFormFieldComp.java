package tpgroup.view.cli.component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import tpgroup.model.exception.FormFieldIOException;

public class MultipleItemsFormFieldComp<T> implements FormFieldComp<List<T>>{
	private static final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	private final String prompt;
	private final Function<String, T> conversion;

	public MultipleItemsFormFieldComp(String prompt, Function<String, T> conversion) {
		super();
		this.prompt = prompt;
		this.conversion = conversion;
	}

	public MultipleItemsFormFieldComp(Function<String, T> conversion) {
		super();
		this.prompt = "";
		this.conversion = conversion;
	}

	@Override
	public List<T> get() throws FormFieldIOException{
		List<T> items = new ArrayList<>();
		do{
			try {
				System.out.println(prompt);
				System.out.print(">");
				if(prompt.isEmpty()){
					break;
				}
				items.add(conversion.apply(in.readLine()));
			} catch (IOException e) {
				throw new FormFieldIOException();
			}
		}while(true);
		return items;
	}
	
}
