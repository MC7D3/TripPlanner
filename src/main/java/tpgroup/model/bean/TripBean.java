package tpgroup.model.bean;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import tpgroup.model.domain.Trip;

public class TripBean {
	private final String country;
	private final String mainCity;
	private final EventsGraphBean tripGraph;
	private final Set<ProposalBean> proposals;

	public TripBean(Trip trip){
		this.country = trip.getCountry();
		this.mainCity = trip.getMainCity();
		this.tripGraph = new EventsGraphBean(trip.getTripGraph());
		this.proposals = trip.getProposals().stream().map(prop -> new ProposalBean(prop)).collect(Collectors.toSet());
	}

	public TripBean(String country, String city){
		this.country = country;
		this.mainCity = city;
		this.tripGraph = null;
		this.proposals = null;
	}

	public String getCountry() {
		return country;
	}

	public String getMainCity() {
		return mainCity;
	}

	public EventsGraphBean getTripGraph() {
		return tripGraph;
	}

	public Set<ProposalBean> getProposals() {
		return proposals;
	}

	@Override
	public int hashCode() {
		return Objects.hash(country, mainCity, tripGraph, proposals);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TripBean other = (TripBean) obj;
		return Objects.equals(country, other.country) && Objects.equals(mainCity, other.mainCity)
				&& Objects.equals(tripGraph, other.tripGraph) && Objects.equals(proposals, other.proposals);
	}

}
