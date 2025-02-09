package tpgroup.view.cli;

import tpgroup.controller.RoomController;
import tpgroup.controller.TripController;
import tpgroup.model.EventsNode;
import tpgroup.model.FormData;
import tpgroup.model.IntervalBean;
import tpgroup.view.cli.template.ConfirmationForm;

public class SubmitAddProposal extends ConfirmationForm {
	private FormData formData;

	protected SubmitAddProposal(CliView sm, FormData previousData) {
		super(sm);
		this.formData = previousData;
	}

	@Override
	public void show() {

	}

	@Override
	public void handleAnswer(String answer) {
		if (answer.equals("yes")) {
			if(TripController.createAddProposal(formData.get("eventsNode"), formData.get("poi"), formData.get("intervalBean"))){
				System.out.println("proposal inserted succesfully!");
			}else{
				System.out.println("proposal invalid or malformed");
			}
		}
		this.machine.setState(RoomController.amIAdmin() ? new RoomAdminMenuState(this.machine)
				: new RoomMemberMenuState(this.machine));
	}

	@Override
	public void promptText() {
		EventsNode chosenNode = formData.get("eventsNode");
		String poi = formData.get("poi");
		IntervalBean intervalBean = formData.get("intervalBean");
		if (chosenNode == null || poi == null || intervalBean == null) {
			System.err.println("ERROR: unable to recover Event data");
			this.machine.setState(RoomController.amIAdmin() ? new RoomAdminMenuState(this.machine)
					: new RoomMemberMenuState(this.machine));
			return;
		}
		System.out.println("new event data summary");
		System.out.println("poi: " + poi);
		System.out.println("start: " + intervalBean.getStartTime());
		System.out.println("end: " + intervalBean.getEndTime());
		System.out.println("inserted in node: " + chosenNode);
		System.out.println("do u confirm (yes/no):");
	}

}
