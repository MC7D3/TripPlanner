package tpgroup.view.cli.template;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import tpgroup.model.FormData;
import tpgroup.view.cli.CliView;

public abstract class CliSelectItemFormState<T> extends CliMultipartForm{
	protected final Map<Integer, T> choices;
	private final boolean includeBackChoice;
	private final String title;

	protected CliSelectItemFormState(CliView sm, List<T> choices, boolean includeBackChoice) {
		super(sm);
		this.choices = IntStream.range(0, choices.size()).boxed()
				.collect(Collectors.toMap(key -> key + 1, key -> choices.get(key)));
		this.includeBackChoice = includeBackChoice;
		this.title = null;
	}

	protected CliSelectItemFormState(CliView sm, List<T> choices, boolean includeBackChoice, FormData previousData) {
		super(sm, previousData);
		this.choices = IntStream.range(0, choices.size()).boxed()
				.collect(Collectors.toMap(key -> key + 1, key -> choices.get(key)));
		this.includeBackChoice = includeBackChoice;
		this.title = null;
	}

	protected CliSelectItemFormState(CliView sm, List<T> choices, boolean includeBackChoice, String title) {
		super(sm);
		this.choices = IntStream.range(0, choices.size()).boxed()
				.collect(Collectors.toMap(key -> key + 1, key -> choices.get(key)));
		this.includeBackChoice = includeBackChoice;
		this.title = title;
	}

	protected CliSelectItemFormState(CliView sm, List<T> choices, boolean includeBackChoice, String title, FormData previousData) {
		super(sm, previousData);
		this.choices = IntStream.range(0, choices.size()).boxed()
				.collect(Collectors.toMap(key -> key + 1, key -> choices.get(key)));
		this.includeBackChoice = includeBackChoice;
		this.title = title;
	}
	

	protected int selectMenuOption() {
		int choice = 0;
		boolean outOfRange;
		do {
			if(title != null) System.out.println(title);
			for (Entry<Integer, T> option : choices.entrySet()) {
				System.out.println(option.getKey() + ". " + option.getValue());
			}
			if(includeBackChoice) System.out.println(choices.size() + ". go back");
			System.out.print(">");
			boolean isInt;
			try {
				choice = Integer.parseInt(in.readLine());
				isInt = true;
			} catch (IOException | NumberFormatException e) {
				isInt = false;
			}
			outOfRange = !choices.containsKey(choice) && (!includeBackChoice || choice != this.choices.size());
			if(!isInt || outOfRange){
				System.err.println("ERROR: invalid menu option");
			}
		} while (outOfRange);
		return choice;
	}

	@Override
	public void show() {
		handleChosenElement(choices.get(selectMenuOption()));
	}

	protected abstract void handleChosenElement(T chosen);

}
