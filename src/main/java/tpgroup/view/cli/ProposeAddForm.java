package tpgroup.view.cli;

import java.util.List;

import tpgroup.controller.POIController;
import tpgroup.controller.RoomController;
import tpgroup.controller.TripController;
import tpgroup.model.EventsNode;
import tpgroup.model.IntervalBean;
import tpgroup.model.POIFilterBean;
import tpgroup.model.domain.PointOfInterest;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.model.exception.InvalidBeanParamException;
import tpgroup.view.cli.component.FormFieldFactory;

public class ProposeAddForm extends CliViewState {

	protected ProposeAddForm(CliView machine) {
		super(machine);
	}

	@Override
	public void show() {
		try {
			EventsNode chosenNode = FormFieldFactory.getInstance().newSelectItem( "select the branch where u want to propose the new event:", TripController.getBranches()).get();
			System.out.println("filter poi search:");
			System.out.println("insert values only if u want to specify that filter, else keep empty");
			String minRatingTxt = FormFieldFactory.getInstance()
					.newDefault("min rating filter (one/two/three/four/five stars):", str -> str).get();
			String maxRatingTxt = FormFieldFactory.getInstance()
					.newDefault("max rating filter (one/two/three/four/five stars):", str -> str).get();
			List<String> chosenTags = FormFieldFactory.getInstance()
					.newMultiItem("select tags filters (fun/families/food/culture/gastronomy):", str -> str).get();
			POIFilterBean filters = new POIFilterBean(minRatingTxt, maxRatingTxt, chosenTags);
			PointOfInterest poi = FormFieldFactory.getInstance()
					.newSelectItem("select the point of interest:", POIController.getPOIFiltered(filters)).get();
			System.out.println("NOTE: u can keep the start time empty, that way the event will put after the last one in the node");
			String startTimeTxt = FormFieldFactory.getInstance()
					.newDefault("start time (es 23-01-2025 14:30):", str -> str).get();
			String endTimeTxt = FormFieldFactory.getInstance().newDefault("end time (es 23-01-2025 15:30):", str -> str)
					.get();
			IntervalBean ProposalStartEnd = new IntervalBean(startTimeTxt, endTimeTxt);
			if (TripController.createAddProposal(chosenNode, poi, ProposalStartEnd)) {
				System.out.println("proposal inserted succesfully!");
			} else {
				System.out.println("proposal invalid or malformed");
			}
		} catch (InvalidBeanParamException | FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
		this.machine.setState(RoomController.amIAdmin() ? new RoomAdminMenuState(this.machine)
				: new RoomMemberMenuState(this.machine));
	}

}
