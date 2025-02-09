package tpgroup.view.cli;

import tpgroup.controller.RoomController;
import tpgroup.controller.TripController;
import tpgroup.model.domain.Proposal;
import tpgroup.view.cli.template.CliSelectItemFormState;

public class RemoveProposalForm extends CliSelectItemFormState<Proposal>{

	protected RemoveProposalForm(CliView sm) {
		super(sm, TripController.getLoggedProposals(), true);
	}

	@Override
	protected void handleChosenElement(Proposal chosen) {
		if(chosen != null){
			if(TripController.undoProposal(chosen)){
				System.out.println("proposal removed successfully!");
			}else{
				System.out.println("invalid or malformed proposal");
			}
		}
		this.machine.setState(RoomController.amIAdmin() ? new RoomAdminMenuState(this.machine)
				: new RoomMemberMenuState(this.machine));
	}

	
}
