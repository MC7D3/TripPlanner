package tpgroup.view.cli;

import java.util.List;

import tpgroup.controller.graphical.cli.RoomGController;
import tpgroup.model.bean.BranchBean;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.statemachine.CliViewState;

public class ConnectBranchesFormState extends CliViewState {

	public ConnectBranchesFormState() {
		super();
	}

	@Override
	public void present() {
		try {
			BranchBean parent = FormFieldFactory.getInstance().newSelectItem("select from where u want the connection to start:", RoomGController.getBranches()).get();
			List<BranchBean> childList = RoomGController.getBranches();
			childList.remove(parent);
			BranchBean child = FormFieldFactory.getInstance().newSelectItem("select where u want the connection to go:", childList).get();
			CliViewState next = RoomGController.connectBranches(parent, child);
			this.machine.setState(next);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

	
}
