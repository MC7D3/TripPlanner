package tpgroup.view.cli;

import tpgroup.controller.RoomController;
import tpgroup.controller.TripController;
import tpgroup.model.Event;
import tpgroup.model.EventsNode;
import tpgroup.view.cli.component.FormFieldFactory;

public class ProposeRemovalForm extends CliViewState {

	protected ProposeRemovalForm(CliView machine) {
		super(machine);
	}

	@Override
	public void show() {
		EventsNode chosenNode = FormFieldFactory.getInstance().newSelectItem(
				"select the branch where you want to propose the new event:", TripController.getBranches()).get();
		Event chosenEvent = FormFieldFactory.getInstance()
				.newSelectItem("select the event you want to remove:", TripController.getEvents(chosenNode)).get();
		if (TripController.createRemoveProposal(chosenNode, chosenEvent)) {
			System.out.println("proposal inserted successfully!");
		} else {
			System.out.println("invalid or malformed proposal");
		}
		this.machine.setState(RoomController.amIAdmin() ? new RoomAdminMenuState(this.machine)
				: new RoomMemberMenuState(this.machine));
	}

}
