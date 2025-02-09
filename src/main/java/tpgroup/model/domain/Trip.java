package tpgroup.model.domain;


import java.util.ArrayList;
import java.util.List;

import tpgroup.model.EventsGraph;
import tpgroup.model.EventsNode;

public class Trip {
	private final String destination;
	private final EventsGraph tripGraph;
	private final List<Proposal> proposals;
	
	public Trip(String destination) {
		this.destination = destination;
		tripGraph = new EventsGraph();
		proposals = new ArrayList<>();
	}

	public String getDestination() {
		return destination;
	}

	public boolean addProposal(Proposal arg0) {
		return proposals.add(arg0);
	}

	public boolean containsProposal(Proposal event) {
		return proposals.contains(event);
	}

	public boolean ProposalIsEmpty() {
		return proposals.isEmpty();
	}

	public boolean removeProposal(Proposal event) {
		return proposals.remove(event);
	}

	public int ProposalSize() {
		return proposals.size();
	}

	public List<Proposal> getProposals(){
		return this.proposals;
	}

	public List<EventsNode> getSequenceNodes(){
		return tripGraph.getAllNodes();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((tripGraph == null) ? 0 : tripGraph.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Trip other = (Trip) obj;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (tripGraph == null) {
			if (other.tripGraph != null)
				return false;
		} else if (!tripGraph.equals(other.tripGraph))
			return false;
		return true;
	}

}
