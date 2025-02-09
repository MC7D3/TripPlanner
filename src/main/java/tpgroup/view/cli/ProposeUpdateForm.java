package tpgroup.view.cli;

import tpgroup.controller.RoomController;
import tpgroup.model.EventsNode;
import tpgroup.view.cli.template.SelectSequenceFormState;

public class ProposeUpdateForm extends SelectSequenceFormState{

	protected ProposeUpdateForm(CliView sm) {
		super(sm);
	}

	@Override
	protected void handleChosenElement(EventsNode chosen) {
		if(chosen == null){
			this.machine.setState(RoomController.amIAdmin()? new RoomAdminMenuState(this.machine) : new RoomMemberMenuState(this.machine));
			return;
		}
		formData.put("eventsNode", chosen);
		this.machine.setState(new SubmitUpdateProposal(this.machine, this.formData));
	}
	
}
