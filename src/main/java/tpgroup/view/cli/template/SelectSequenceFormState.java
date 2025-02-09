package tpgroup.view.cli.template;


import tpgroup.controller.TripController;
import tpgroup.model.EventsNode;
import tpgroup.view.cli.CliView;

public abstract class SelectSequenceFormState extends CliSelectItemFormState<EventsNode>{

	protected SelectSequenceFormState(CliView sm) {
		super(sm, TripController.getSequences(), true, "select in which event sequence u want to add the event");
	}


}
