package tpgroup.view.cli;

import tpgroup.controller.RoomController;
import tpgroup.controller.TripController;
import tpgroup.model.Event;
import tpgroup.model.EventsNode;
import tpgroup.view.cli.component.FormFieldFactory;

public class RemoveProposalForm extends CliViewState{

	protected RemoveProposalForm(CliView machine) {
		super(machine);
	}

	@Override
	public void show() {
		EventsNode chosenNode = FormFieldFactory.getInstance().newSelectItem(
				"select the branch:", TripController.getBranches()).get();
		Event chosenEvent = FormFieldFactory.getInstance()
				.newSelectItem("select the event you want to update:", TripController.getEvents(chosenNode)).get();
		if (TripController.createRemoveProposal(chosenNode, chosenEvent)) {
			System.out.println("proposal inserted succesfully!");
		} else {
			System.out.println("proposal invalid or malformed");
		}
		this.machine.setState(RoomController.amIAdmin() ? new RoomAdminMenuState(this.machine)
				: new RoomMemberMenuState(this.machine));
	}

	
}
