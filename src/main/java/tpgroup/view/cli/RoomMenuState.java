package tpgroup.view.cli;

import java.util.List;

import tpgroup.model.domain.Room;
import tpgroup.view.cli.template.CliMenuState;

//TODO gestione stanza
public class RoomMenuState extends CliMenuState{
	private Room curRoom;

	public RoomMenuState(CliView sm, Room curRoom) {
		super(sm, List.of("choice 1", "choice 2", "choice 3"));
		this.curRoom = curRoom;
	}

	@Override
	protected void handleChoice(int choice) {
		throw new UnsupportedOperationException("Unimplemented method 'handleChoice'");
	}

}

