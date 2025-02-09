package tpgroup.view.cli;

import java.util.List;

import tpgroup.controller.TripController;
import tpgroup.view.cli.template.CliMenuState;

public class RoomMemberMenuState extends CliMenuState{

	public RoomMemberMenuState(CliView sm) {
		super(sm, List.of("propose new Event", "propose event removal", "list other proposals", "propose event update", "unpropose proposal", "go back"));
	}

	@Override
	protected void handleChoice(int choice) {
		switch(choice){
			case 1 -> this.machine.setState(new ProposeAddForm(this.machine));
			case 2 -> this.machine.setState(new ProposeRemovalForm(this.machine));
			case 3 -> TripController.getProposals().forEach(System.out::println);
			case 4 -> this.machine.setState(new ProposeUpdateForm(this.machine));
			case 5 -> this.machine.setState(new RemoveProposalForm(this.machine));
			default -> this.machine.setState(new LoggedMenuState(this.machine));
		}
	}

}

