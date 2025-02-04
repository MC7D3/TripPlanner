package tpgroup.model.domain;

import java.util.HashSet;
import java.util.Set;

import tpgroup.model.EventsGraph;

public class Trip {
	private final String destination;
	private final Set<EventsGraph> days;
	
	public Trip(String destination) {
		this.destination = destination;
		days = new HashSet<>();
	}

	public String getDestination() {
		return destination;
	}

	public Set<EventsGraph> getDays() {
		return days;
	}

}
