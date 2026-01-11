package tpgroup.view.cli;

import java.util.List;

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
			System.out.println("NOTE: if u want to go back just do not choose anything in the pivot selection");
			BranchBean toSplit = FormFieldFactory.getInstance()
					.newSelectItem("select the branch u want to split into two:", RoomGController.getBranches())
					.get();
			System.out.println("NOTE: the first event of this node has been hidden to avoid leaving an empty node in the graph\nthe pivot selection is inclusive hence the pivot element will go in the new node");
			List<EventBean> toSplitEvents = toSplit.getEvents().stream().toList();
			toSplitEvents.removeFirst();
			EventBean pivot = FormFieldFactory.getInstance().newSelectItem("select where u want to perform the split:", toSplitEvents, true).get();
			CliViewState next = RoomGController.splitBranch(toSplit, pivot);
			this.machine.setState(next);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

}
