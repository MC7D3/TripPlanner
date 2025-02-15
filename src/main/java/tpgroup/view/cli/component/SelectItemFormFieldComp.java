package tpgroup.view.cli.component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SelectItemFormFieldComp<T> implements FormFieldComp<T>{
	private static final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	private final String title;
	private final Map<Integer, T> choices;
	private final boolean nullable;

	public SelectItemFormFieldComp(String prompt, List<T> choices, boolean nullable) {
		this.choices = IntStream.range(0, choices.size()).boxed()
				.collect(Collectors.toMap(key -> key + 1, key -> choices.get(key)));
		this.title = prompt;
		this.nullable = nullable;
	}

	public SelectItemFormFieldComp(List<T> choices, boolean nullable) {
		this(null, choices, nullable);
	}

	public SelectItemFormFieldComp(List<T> choices) {
		this(null, choices, false);
	}

	public SelectItemFormFieldComp(String prompt, List<T> choices) {
		this(prompt, choices, false);
	}

	private int selectMenuOption() {
		int choice = 0;
		boolean outOfRange;
		do {
			if(title != null) System.out.println(title);
			for (Entry<Integer, T> option : choices.entrySet()) {
				System.out.println(option.getKey() + ". " + option.getValue());
			}
			if(nullable){
				System.out.println((choices.size() + 1) + ". no choice");
			}
			System.out.print(">");
			boolean isInt;
			try {
				choice = Integer.parseInt(in.readLine());
				isInt = true;
			} catch (IOException | NumberFormatException e) {
				isInt = false;
			}
			outOfRange = !(choices.containsKey(choice) || (nullable && choice == choices.size() + 1));
			if(!isInt || outOfRange){
				System.err.println("ERROR: invalid menu option");
			}
		} while (outOfRange);
		return choice;
	}

	@Override
	public T get(){
		int chosen = selectMenuOption();
		if(nullable && chosen == choices.size() + 1){
			return null;
		}
		return this.choices.get(chosen);
	}

}
