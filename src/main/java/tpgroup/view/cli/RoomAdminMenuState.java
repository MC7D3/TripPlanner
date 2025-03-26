package tpgroup.view.cli;

import java.util.ArrayList;
import java.util.List;

import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;

public class RoomAdminMenuState extends RoomMemberMenuState {

	public RoomAdminMenuState(CliView machine) {
		super(machine);
	}

	@Override
	public void show() {
		String chosen = "";
		try {
			List<String> adminMenuOptions = new ArrayList<>(
			List.of("accept proposals", "create alternative sequence", "split a sequence into two",
					"connect sequences", "disconnect branches", "delete branches", "delete room"));
			adminMenuOptions.addAll(this.menuOptions);
			chosen = FormFieldFactory.getInstance().newSelectItem(adminMenuOptions).get();
			super.handleChoice(chosen);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
		switch (chosen) {
			case "accept proposal" -> this.machine.setState(new AcceptProposalFormState(this.machine));
			case "create alternative branch" -> this.machine.setState(new CreateBranchFormState(this.machine));
			case "connect branches" -> this.machine.setState(new ConnectBranchesFormState(this.machine));
			case "disconnect branches" -> this.machine.setState(new DisconnectBranchesFormState(this.machine));
			case "delete branch" -> this.machine.setState(new DeleteBranchFromState(this.machine));
			case "delete room" -> this.machine.setState(new DeleteRoomConfirmationState(this.machine));
			//default not needed 
		}
	}

}
