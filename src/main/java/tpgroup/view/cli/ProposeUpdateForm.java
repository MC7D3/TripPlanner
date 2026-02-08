package tpgroup.view.cli;

import java.util.ArrayList;

import tpgroup.controller.graphical.cli.RoomGController;
import tpgroup.model.bean.BranchBean;
import tpgroup.model.bean.EventBean;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.statemachine.CliViewState;

public class ProposeUpdateForm extends CliViewState {

	private final RoomGController roomGCtrl = new RoomGController();

	public ProposeUpdateForm() {
		super();
	}

	@Override
	public void present() {
		try {
			BranchBean chosenNode = FormFieldFactory.getInstance().newSelectItem(
					"select the branch:", roomGCtrl.getBranches()).get();
			EventBean chosenEvent = FormFieldFactory.getInstance()
					.newSelectItem("select the event you want to update:",
							new ArrayList<>(chosenNode.getEvents()))
					.get();
			System.out.println("selected event:\n" + chosenEvent);
			String startTimeTxt = FormFieldFactory.getInstance()
					.newDefault("new start time (es 23-01-2025 14:30):", str -> str).get();
			String endTimeTxt = FormFieldFactory.getInstance()
					.newDefault("new end time (es 23-01-2025 15:30):", str -> str)
					.get();
			CliViewState next = roomGCtrl.createUpdateProposal(chosenNode, chosenEvent, startTimeTxt, endTimeTxt);
			this.machine.setState(next);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

}
