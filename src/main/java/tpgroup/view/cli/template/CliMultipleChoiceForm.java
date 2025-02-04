package tpgroup.view.cli.template;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import tpgroup.view.cli.CliView;
import tpgroup.view.cli.CliViewState;

public abstract class CliMultipleChoiceForm<T> extends CliViewState {
	private final Map<Integer, T> choices;
	private Integer chosen;

	protected CliMultipleChoiceForm(CliView sm, List<T> choices) {
		super(sm);
		this.choices = IntStream.range(0, choices.size()).boxed()
				.collect(Collectors.toMap(key -> key + 1, key -> choices.get(key)));
	}

	protected int selectMenuOption() {
		int choice = 0;
		boolean outOfRange;
		do {
			for (Entry<Integer, T> option : choices.entrySet()) {
				System.out.println(option.getKey() + ". " + option.getValue());
			}
			System.out.print(">");
			boolean isInt;
			try {
				choice = Integer.parseInt(in.readLine());
				isInt = true;
			} catch (IOException | NumberFormatException e) {
				isInt = false;
			}
			outOfRange = !choices.containsKey(choice);
			if(!isInt || outOfRange){
				System.err.println("ERROR: invalid menu option");
			}
		} while (outOfRange);
		return choice;
	}

	protected T getChosen(){
		return choices.get(chosen);

	}

}
