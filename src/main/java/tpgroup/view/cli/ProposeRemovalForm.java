package tpgroup.view.cli;

import java.util.ArrayList;

import tpgroup.controller.graphical.cli.RoomGController;
import tpgroup.model.Event;
import tpgroup.model.EventsNode;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.statemachine.CliViewState;

public class ProposeRemovalForm extends CliViewState {

	public ProposeRemovalForm() {
		super();
	}

	@Override
	public void present() {
		try {
			EventsNode chosenNode = FormFieldFactory.getInstance().newSelectItem(
					"select the branch where you want to propose the removal of the event:", RoomGController.getBranches()).get();
			Event chosenEvent = FormFieldFactory.getInstance()
					.newSelectItem("select the event you want to remove:", new ArrayList<>(chosenNode.getEvents())).get();
			RoomGController.createRemoveProposal(chosenNode, chosenEvent);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

}
