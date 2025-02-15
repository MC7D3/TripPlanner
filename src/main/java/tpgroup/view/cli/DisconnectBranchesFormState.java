package tpgroup.view.cli;

import tpgroup.controller.RoomController;
import tpgroup.controller.TripController;
import tpgroup.model.EventsNode;
import tpgroup.view.cli.component.FormFieldFactory;

public class DisconnectBranchesFormState extends CliViewState {

	protected DisconnectBranchesFormState(CliView machine) {
		super(machine);
	}

	@Override
	public void show() {
		this.machine.setState(RoomController.amIAdmin() ? new RoomAdminMenuState(this.machine)
			: new RoomMemberMenuState(this.machine));

		EventsNode from = FormFieldFactory.getInstance()
				.newSelectItem("select from where u want the connection to start:", TripController.getBranches(), true)
				.get();
		if (from == null) {
			return;
		}
		EventsNode to = FormFieldFactory.getInstance().newSelectItem(
				"select where u want the connection to go:", TripController.getDeletionCandidates(from)).get();
		TripController.disconnectBranches(from, to);
}

}
