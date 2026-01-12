package tpgroup.view.cli;

import java.util.List;
import java.util.stream.Collectors;

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
			List<BranchBean> validConnections = RoomGController.getBranches().stream().filter(branch -> branch.getEvents().isEmpty()).collect(Collectors.toList());
			System.out.println("NOTE: to go back without performing the connection, select no choice on the second node selection");
			BranchBean parent = FormFieldFactory.getInstance().newSelectItem("select from where u want the connection to start:", validConnections).get();
			validConnections.remove(parent);
			BranchBean child = FormFieldFactory.getInstance().newSelectItem("select where u want the connection to go:", validConnections, true).get();
			CliViewState next = RoomGController.connectBranches(parent, child);
			this.machine.setState(next);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

	
}
