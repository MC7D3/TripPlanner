package tpgroup.view.cli.component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MultipleItemsFormFieldComp<T> implements FormFieldComp<List<T>>{
	private static final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	private final String prompt;
	private final Function<String, T> conversion;

	public MultipleItemsFormFieldComp(String prompt, Function<String, T> conversion) {
		super();
		this.prompt = prompt;
		this.conversion = conversion;
	}

	@Override
	public List<T> get() {
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
				System.err.println("ERROR: unable to process input");
				throw new RuntimeException(e);
			}
		}while(true);
		return items;
	}
	
}
