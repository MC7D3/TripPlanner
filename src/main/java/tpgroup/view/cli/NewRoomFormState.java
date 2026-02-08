package tpgroup.view.cli;

import tpgroup.controller.graphical.cli.LoggedMenuGController;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.statemachine.CliViewState;

public class NewRoomFormState extends CliViewState {

	private final LoggedMenuGController loggedMenuGCtrl = new LoggedMenuGController();

	public NewRoomFormState() {
		super();
	}

	@Override
	public void present() {
		try {
			FormFieldFactory ref = FormFieldFactory.getInstance();
			System.out.println("NOTE: if you want to go back keep the field blank");
			String name = ref.newDefault("room's name:", str -> str).get();
			String country = ref.newSelectItem("trip country destination:", loggedMenuGCtrl.getAllCountries(), true)
					.get();
			String city = ref.newSelectItem("trip main city destination: ", loggedMenuGCtrl.getAllCities(country), true)
					.get();
			CliViewState next = loggedMenuGCtrl.createNewRoom(name, country, city);
			this.machine.setState(next);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}
}
