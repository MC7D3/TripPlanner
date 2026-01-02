package tpgroup.view.cli;

import java.util.List;

import tpgroup.controller.graphical.cli.RoomGController;
import tpgroup.model.EventsNode;
import tpgroup.model.domain.PointOfInterest;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.statemachine.CliViewState;

public class ProposeAddForm extends CliViewState {

	public ProposeAddForm() {
		super();
	}

	@Override
	public void present() {
		try {
			EventsNode chosenBranch= FormFieldFactory.getInstance().newSelectItem( "select the branch where u want to propose the new event:", RoomGController.getBranches()).get();
			System.out.println("filter poi search:");
			System.out.println("insert values only if u want to specify that filter, else keep empty");
			String minRatingTxt = FormFieldFactory.getInstance()
					.newDefault("min rating filter (one/two/three/four/five stars):", str -> str).get();
			String maxRatingTxt = FormFieldFactory.getInstance()
					.newDefault("max rating filter (one/two/three/four/five stars):", str -> str).get();
			List<String> chosenTags = FormFieldFactory.getInstance()
					.newMultiItem("select tags filters (fun/families/food/culture/gastronomy):", str -> str).get();
			PointOfInterest poi = FormFieldFactory.getInstance()
					.newSelectItem("select the point of interest:", RoomGController.getFilteredPOIs(minRatingTxt, maxRatingTxt, chosenTags), true).get();
			System.out.println("NOTE: u can keep the start time empty, that way the event will put after the last one in the node");
			String startTimeTxt = FormFieldFactory.getInstance()
					.newDefault("start time (es 23-01-2025 14:30):", str -> str).get();
			String endTimeTxt = FormFieldFactory.getInstance().newDefault("end time (es 23-01-2025 15:30):", str -> str)
					.get();
			CliViewState next = RoomGController.createAddProposal(poi, chosenBranch, startTimeTxt, endTimeTxt);
			this.machine.setState(next);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

}
