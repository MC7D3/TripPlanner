package tpgroup.view.cli;

import java.util.ArrayList;
import java.util.List;

import tpgroup.controller.RoomController;
import tpgroup.controller.TripController;
import tpgroup.model.EventsNode;
import tpgroup.model.exception.BranchConnectionException;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;

public class ConnectBranchesFormState extends CliViewState {

	protected ConnectBranchesFormState(CliView machine) {
		super(machine);
	}

	@Override
	public void show() {
		try {
			EventsNode from = FormFieldFactory.getInstance().newSelectItem("select from where u want the connection to start:", TripController.getBranches()).get();
			List<EventsNode> toList = new ArrayList<>(TripController.getBranches());
			toList.remove(from);
			EventsNode to = FormFieldFactory.getInstance().newSelectItem("select where u want the connection to go:", toList).get();
			TripController.connectBranches(from, to);
			System.out.println("branches connected succesfully");
		} catch (BranchConnectionException e) {
			//TODO: non mi piace mettere all'interno delle exception nel caso sia presente una cause edito il messaggio
			System.err.println("ERROR:" + e.getMessage() + (e.getCause() != null? "\n caused by: " + e.getCause().getMessage() : ""));
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
		this.machine.setState(RoomController.amIAdmin() ? new RoomAdminMenuState(this.machine)
				: new RoomMemberMenuState(this.machine));
	}

	
}
