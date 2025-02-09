package tpgroup.view.cli;

import tpgroup.controller.POIController;
import tpgroup.model.FormData;
import tpgroup.model.domain.PointOfInterest;
import tpgroup.view.cli.template.CliSelectItemFormState;

public class SelectPOIFormState extends CliSelectItemFormState<PointOfInterest> {

	protected SelectPOIFormState(CliView sm) {
		super(sm, POIController.getAllPOI(), true, "select the point of interest:");
	}

	protected SelectPOIFormState(CliView sm, FormData previousData) {
		super(sm,
				POIController.getPOIFiltered(previousData.get("filtersBean")),
				false, "select the point of interest:", previousData);
	}

	@Override
	protected void handleChosenElement(PointOfInterest chosen) {
		formData.remove("filtersBean");
		formData.put("poi", chosen);
		this.machine.setState(new StartEndFormState(this.machine, formData));
	}

}
