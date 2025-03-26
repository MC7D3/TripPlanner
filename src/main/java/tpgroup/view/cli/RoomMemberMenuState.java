package tpgroup.view.cli;

import java.util.List;

import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;

public class RoomMemberMenuState extends CliViewState {
	protected final List<String> menuOptions;

	public RoomMemberMenuState(CliView machine) {
		super(machine);
		menuOptions = List.of("propose new Event", "propose event removal", "list other proposals",
				"propose event update", "undo proposal", "go back");
	}

	protected void handleChoice(String choice) {
		switch (choice) {
			case "propose new Event" -> this.machine.setState(new ProposeAddForm(this.machine));
			case "propose event removal" -> this.machine.setState(new ProposeRemovalForm(this.machine));
			case "list other proposals" -> this.machine.setState(new ListAndLikeProposalsState(this.machine));
			case "propose event update" -> this.machine.setState(new ProposeUpdateForm(this.machine));
			case "undo proposal" -> this.machine.setState(new RemoveProposalForm(this.machine));
			default -> this.machine.setState(new LoggedMenuState(this.machine));
		}
	}

	@Override
	public void show() {
		try {
			String chosen = FormFieldFactory.getInstance().newSelectItem(menuOptions).get();
			handleChoice(chosen);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e);
		}
	}

}
