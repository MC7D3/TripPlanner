package tpgroup.view.cli;

import tpgroup.controller.RoomController;
import tpgroup.controller.TripController;
import tpgroup.model.domain.Proposal;
import tpgroup.view.cli.component.FormFieldFactory;

public class AcceptProposalFormState extends CliViewState{

	protected AcceptProposalFormState(CliView machine) {
		super(machine);
	}

	@Override
	public void show() {
		Proposal accepted = FormFieldFactory.getInstance().newSelectItem("select the proposal you intend to accept:\n", TripController.getProposals(), true).get();
		if(accepted != null){
			TripController.acceptProposal(accepted);
			System.out.println("proposal accepted succesfully!");
		}
		this.machine.setState(RoomController.amIAdmin() ? new RoomAdminMenuState(this.machine)
				: new RoomMemberMenuState(this.machine));
	}
	
}
