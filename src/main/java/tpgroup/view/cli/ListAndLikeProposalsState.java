package tpgroup.view.cli;

import java.util.ArrayList;
import java.util.List;

import tpgroup.controller.RoomController;
import tpgroup.controller.TripController;
import tpgroup.model.domain.Proposal;
import tpgroup.model.exception.FormFieldIOException;
import tpgroup.view.cli.component.FormFieldFactory;

public class ListAndLikeProposalsState extends CliViewState{

	protected ListAndLikeProposalsState(CliView machine) {
		super(machine);
	}

	@Override
	public void show() {
		try {
			List<Proposal> proposal = new ArrayList<>(TripController.getProposals());
			proposal.sort((p1, p2) -> Integer.compare(p2.getLikes(), p1.getLikes()));
			Proposal chosen = null;
			System.out.println("NOTE: if u want to go back, choose the no option value");
			do{
				chosen = FormFieldFactory.getInstance().newSelectItem(proposal, true).get();
				if(chosen != null){
					TripController.likeProposal(chosen);
					System.out.println("proposal liked!");
				}
			}while(chosen != null);
		} catch (FormFieldIOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
		this.machine.setState(RoomController.amIAdmin() ? new RoomAdminMenuState(this.machine)
				: new RoomMemberMenuState(this.machine));
	}
	
}
