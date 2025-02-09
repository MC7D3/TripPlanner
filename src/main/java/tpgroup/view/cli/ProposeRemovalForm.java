package tpgroup.view.cli;

import tpgroup.controller.RoomController;
import tpgroup.model.EventsNode;
import tpgroup.view.cli.template.SelectSequenceFormState;

public class ProposeRemovalForm extends SelectSequenceFormState{

	protected ProposeRemovalForm(CliView sm) {
		super(sm);
	}

	@Override
	protected void handleChosenElement(EventsNode chosen) {
		if(chosen == null){
			this.machine.setState(RoomController.amIAdmin()? new RoomAdminMenuState(this.machine) : new RoomMemberMenuState(this.machine));
			return;
		}
		formData.put("eventsNode", chosen);
		this.machine.setState(new SubmitRemoveProposal(this.machine, this.formData));
	}
	
}
