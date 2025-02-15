package tpgroup.view.cli;

import java.util.ArrayList;
import java.util.List;

import tpgroup.controller.RoomController;
import tpgroup.controller.TripController;
import tpgroup.model.EventsNode;
import tpgroup.model.exception.BranchConnectionException;
import tpgroup.view.cli.component.FormFieldFactory;

public class ConnectBranchesFormState extends CliViewState {

	protected ConnectBranchesFormState(CliView machine) {
		super(machine);
	}

	@Override
	public void show() {
		EventsNode from = FormFieldFactory.getInstance().newSelectItem("select from where u want the connection to start:", TripController.getBranches()).get();
		List<EventsNode> toList = new ArrayList<>(TripController.getBranches());
		toList.remove(from);
		EventsNode to = FormFieldFactory.getInstance().newSelectItem("select where u want the connection to go:", toList).get();
		try {
			TripController.connectBranches(from, to);
			System.out.println("branches connected succesfully");
		} catch (BranchConnectionException e) {
			System.out.println(e.getMessage() + (e.getCause() != null? "\n caused by: " + e.getCause().getMessage() : ""));
		}
		this.machine.setState(RoomController.amIAdmin() ? new RoomAdminMenuState(this.machine)
				: new RoomMemberMenuState(this.machine));
	}

	
}
