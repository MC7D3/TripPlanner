package tpgroup.view.cli;

import java.util.ArrayList;

import tpgroup.controller.graphical.cli.RoomGController;
import tpgroup.model.bean.BranchBean;
import tpgroup.model.bean.EventBean;
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
			BranchBean chosenNode = FormFieldFactory.getInstance().newSelectItem(
					"select the branch where you want to propose the removal of the event:", RoomGController.getBranches()).get();
			EventBean chosenEvent = FormFieldFactory.getInstance()
					.newSelectItem("select the event you want to remove:", new ArrayList<>(chosenNode.getEvents()), true).get();
			RoomGController.createRemoveProposal(chosenNode, chosenEvent);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

}
