package tpgroup.view.cli.template;

import tpgroup.controller.TripController;
import tpgroup.model.Event;
import tpgroup.model.FormData;
import tpgroup.view.cli.CliView;

public abstract class SelectEventFormState extends CliSelectItemFormState<Event>{


	protected SelectEventFormState(CliView sm, FormData previousData) {
		super(sm, TripController.getEvents(previousData.get("eventsNode")), true, "select the event:", previousData);
	}

}
