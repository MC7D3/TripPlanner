package tpgroup.view.cli;

import tpgroup.controller.graphical.cli.RoomGController;
import tpgroup.model.bean.BranchBean;
import tpgroup.model.bean.EventBean;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.statemachine.CliViewState;

public class SplitBranchMenuState extends CliViewState {

	@Override
	public void present() {
		try {
			BranchBean toSplit = FormFieldFactory.getInstance()
					.newSelectItem("select the branch u want to split into two:", RoomGController.getBranches(), true)
					.get();
			EventBean pivot = FormFieldFactory.getInstance().newSelectItem("select where u want to perform the split:", toSplit.getEvents().stream().toList(), true).get();
			CliViewState next = RoomGController.splitBranch(toSplit, pivot);
			this.machine.setState(next);
		} catch (FormFieldIOException e) {
		}
	}

}
