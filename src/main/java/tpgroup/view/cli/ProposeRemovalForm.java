package tpgroup.view.cli;

import java.util.ArrayList;

import tpgroup.controller.graphical.cli.RoomGController;
import tpgroup.model.bean.BranchBean;
import tpgroup.model.bean.EventBean;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.statemachine.CliViewState;

public class ProposeRemovalForm extends CliViewState {

	private final RoomGController roomGCtrl = new RoomGController();

	public ProposeRemovalForm() {
		super();
	}

	@Override
	public void present() {
		try {
			System.out.println("NOTE: if u want to go back keep the event selection after the node selection empty");
			BranchBean chosenNode = FormFieldFactory.getInstance().newSelectItem(
					"select the branch where you want to propose the removal of the event:", roomGCtrl.getBranches(),
					true).get();
			EventBean chosenEvent = FormFieldFactory.getInstance()
					.newSelectItem("select the event you want to remove:", new ArrayList<>(chosenNode.getEvents()),
							true)
					.get();
			CliViewState next = roomGCtrl.createRemoveProposal(chosenNode, chosenEvent);
			this.machine.setState(next);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

}
