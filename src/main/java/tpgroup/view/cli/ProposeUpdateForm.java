package tpgroup.view.cli;

import tpgroup.controller.RoomController;
import tpgroup.controller.TripController;
import tpgroup.model.Event;
import tpgroup.model.EventsNode;
import tpgroup.model.IntervalBean;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.model.exception.InvalidBeanParamException;
import tpgroup.view.cli.component.FormFieldFactory;

public class ProposeUpdateForm extends CliViewState {

	protected ProposeUpdateForm(CliView machine) {
		super(machine);
	}

	@Override
	public void show() {
		try {
			EventsNode chosenNode = FormFieldFactory.getInstance().newSelectItem(
					"select the branch:", TripController.getBranches()).get();
			Event chosenEvent = FormFieldFactory.getInstance()
					.newSelectItem("select the event you want to update:", TripController.getEvents(chosenNode)).get();
			System.out.println("selected event:\n" + chosenEvent);
			String startTimeTxt = FormFieldFactory.getInstance()
					.newDefault("new start time (es 23-01-2025 14:30):", str -> str).get();
			String endTimeTxt = FormFieldFactory.getInstance().newDefault("new end time (es 23-01-2025 15:30):", str -> str)
					.get();
			IntervalBean ProposalStartEnd = new IntervalBean(startTimeTxt, endTimeTxt);
			if (TripController.createUpdateProposal(chosenNode, chosenEvent, ProposalStartEnd)) {
				System.out.println("proposal inserted succesfully!");
			} else {
				System.out.println("proposal invalid or malformed");
			}
		} catch (InvalidBeanParamException | FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
		this.machine.setState(RoomController.amIAdmin() ? new RoomAdminMenuState(this.machine)
				: new RoomMemberMenuState(this.machine));
	}

}
