package tpgroup.view.cli;

import tpgroup.controller.RoomController;
import tpgroup.controller.TripController;
import tpgroup.model.Event;
import tpgroup.model.FormData;
import tpgroup.view.cli.template.SelectEventFormState;

public class SubmitRemoveProposal extends SelectEventFormState {

	protected SubmitRemoveProposal(CliView sm, FormData previousData) {
		super(sm, previousData);
	}

	@Override
	protected void handleChosenElement(Event chosen) {
		if(chosen != null){
			if(TripController.createRemoveProposal(formData.get("eventsNode"), chosen)){
				System.out.println("proposal inserted successfully!");
			}else{
				System.out.println("invalid or malformed proposal");
			}
		}
		this.machine.setState(RoomController.amIAdmin() ? new RoomAdminMenuState(this.machine)
				: new RoomMemberMenuState(this.machine));
	}

	
}
