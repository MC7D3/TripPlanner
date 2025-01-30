package tpgroup.view.cli.template;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import tpgroup.view.cli.CliView;
import tpgroup.view.cli.CliViewState;

public abstract class CliMenuState extends CliViewState {
	Map<Integer, String> menuOptions;

	protected CliMenuState(CliView sm, List<String> menuOptions) {
		super(sm);
		this.menuOptions = IntStream.range(0, menuOptions.size()).boxed()
				.collect(Collectors.toMap(key -> key + 1, key -> menuOptions.get(key)));
	}

	private int selectMenuOption() {
		int choice = 0;
		boolean outOfRange;
		do {
			for (Entry<Integer, String> option : menuOptions.entrySet()) {
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
			outOfRange = !menuOptions.containsKey(choice);
			if(!isInt || outOfRange){
				System.err.println("ERROR: invalid menu option");
			}
		} while (outOfRange);
		return choice;
	}

	@Override
	public void show() {
		int choice = selectMenuOption();
		handleChoice(choice);

	}

	protected abstract void handleChoice(int choice);

}
