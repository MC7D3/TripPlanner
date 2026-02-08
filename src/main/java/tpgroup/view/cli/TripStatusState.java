package tpgroup.view.cli;

import tpgroup.controller.graphical.cli.RoomGController;
import tpgroup.model.bean.TripBean;
import tpgroup.view.cli.statemachine.CliViewState;

public class TripStatusState extends CliViewState {

	private final RoomGController roomGCtrl = new RoomGController();

	public TripStatusState() {
		super();
	}

	@Override
	public void present() {
		TripBean trip = roomGCtrl.getTrip();
		System.out.println(trip);
		CliViewState next = roomGCtrl.showTripStatus();
		this.machine.setState(next);
	}

}
