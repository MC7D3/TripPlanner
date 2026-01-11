package tpgroup.view.cli;

import tpgroup.controller.graphical.cli.RoomGController;
import tpgroup.model.bean.TripBean;
import tpgroup.view.cli.statemachine.CliViewState;

public class TripStatusState extends CliViewState{

	@Override
	public void present() {
		TripBean trip = RoomGController.getTrip();
		System.out.println(trip);
		CliViewState next = RoomGController.showTripStatus();
		this.machine.setState(next);
	}
	
}
