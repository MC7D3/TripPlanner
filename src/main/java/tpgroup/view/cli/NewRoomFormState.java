package tpgroup.view.cli;


import tpgroup.controller.graphical.cli.LoggedMenuGController;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.statemachine.CliViewState;

public class NewRoomFormState extends CliViewState{

	public NewRoomFormState() {
	 	super();
	}

	@Override
	public void present() {
		try {
			FormFieldFactory ref = FormFieldFactory.getInstance();
			System.out.println("NOTE: if you want to go back keep the field blank");
			String name = ref.newDefault("room's name:", str -> str).get();
			String country = ref.newSelectItem("trip country destination:", LoggedMenuGController.getAllCountries(), true).get();
			String city = ref.newSelectItem("trip main city destination: ", LoggedMenuGController.getAllCities(country), true).get();
			CliViewState next = LoggedMenuGController.createNewRoom(name, country, city);
			this.machine.setState(next);
		} catch (FormFieldIOException e){
			System.err.println("ERROR: " + e.getMessage());
		}
	}
}

