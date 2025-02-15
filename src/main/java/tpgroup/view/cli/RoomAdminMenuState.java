package tpgroup.view.cli;

import java.util.ArrayList;
import java.util.List;

import tpgroup.view.cli.component.FormFieldFactory;

public class RoomAdminMenuState extends RoomMemberMenuState{

	public RoomAdminMenuState(CliView machine) {
		super(machine);
	}

	//TODO
	@Override
	public void show() {
		List<String> adminMenuOptions = new ArrayList<>(List.of("accept proposals", "create alternative sequence", "split a sequence into two", "connect sequences", "disconnect branches", "delete branches", "delete room"));
		adminMenuOptions.addAll(this.menuOptions);
		String chosen = FormFieldFactory.getInstance().newSelectItem(adminMenuOptions).get();
		super.handleChoice(chosen);
		switch(chosen){
			case "accept proposal" -> this.machine.setState(new AcceptProposalFormState(this.machine));
			case "create alternative branch" -> this.machine.setState(new CreateBranchFormState(this.machine));
			case "connect branches" -> this.machine.setState(new ConnectBranchesFormState(this.machine));
			case "disconnect branches" -> this.machine.setState(new DisconnectBranchesFormState(this.machine));
			case "delete branch" -> this.machine.setState(new DeleteBranchFromState(this.machine));
			case "delete room" -> this.machine.setState(new DeleteRoomConfirmationState(this.machine));
		}

	}
	
}
