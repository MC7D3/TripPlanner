package tpgroup.view.cli;

import java.util.List;

import tpgroup.controller.graphical.cli.UnloggedGController;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;
import tpgroup.view.cli.statemachine.CliViewState;

public class UnloggedMenuState extends CliViewState {

	private final UnloggedGController unloggedGCtrl = new UnloggedGController();

	public UnloggedMenuState() {
		super();
	}

	@Override
	public void present() {
		String choice = "";
		try {
			choice = FormFieldFactory.getInstance().newSelectItem(List.of("login", "register", "exit")).get();
			CliViewState next = unloggedGCtrl.process(choice);
			this.machine.setState(next);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

}
