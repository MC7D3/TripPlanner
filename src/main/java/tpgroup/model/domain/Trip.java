package tpgroup.model.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tpgroup.model.EventsGraph;
import tpgroup.model.EventsNode;
import tpgroup.model.exception.NodeConflictException;
import tpgroup.model.exception.NodeConnectionException;

public class Trip {
	protected String country;
	protected String mainCity;
	protected EventsGraph tripGraph;
	protected Set<Proposal> proposals;

	public Trip(String country, String mainCity, Set<Proposal> proposals, EventsGraph tripGraph) {
		this.proposals = proposals;
		this.country = country;
		this.mainCity = mainCity;
		this.tripGraph = tripGraph;
	}

	public Trip(String country, String mainCity) {
		this(country, mainCity, new HashSet<>(), new EventsGraph());
	}

	public List<EventsNode> getConnectedBranches(EventsNode of) {
		return tripGraph.getConnectedNodes(of);
	}

	public int connCount(EventsNode of) {
		return tripGraph.connCount(of);
	}

	public String getDestination() {
		return country;
	}

	public boolean addProposal(Proposal proposal) {
		if(proposal.getProposalType().equals(ProposalType.UPDATE) && !proposal.getNodeName().getEvents().contains(proposal.getEvent())){
			return false;
		}
		return proposals.add(proposal);
	}

	public boolean containsProposal(Proposal proposal) {
		return proposals.contains(proposal);
	}

	public boolean proposalIsEmpty() {
		return proposals.isEmpty();
	}

	public boolean acceptProposal(Proposal proposal){
		return proposal.accept();
	}

	public boolean removeProposal(Proposal proposal) {
		return proposals.remove(proposal);
	}

	public int proposalSize() {
		return proposals.size();
	}

	public Set<Proposal> getProposals() {
		return this.proposals;
	}

	public List<EventsNode> getAllBranches() {
		return tripGraph.getAllNodes();
	}

	public void createBranch(EventsNode parentNode) throws NodeConflictException {
		tripGraph.createEmptyNode(parentNode);
	}

	public void connectBranches(EventsNode parentNode, EventsNode childNode) throws NodeConnectionException {
		tripGraph.connect(parentNode, childNode);
	}

	public void disconnectBranches(EventsNode parentNode, EventsNode childNode) {
		tripGraph.disconnect(parentNode, childNode);
	}

	public void removeNode(EventsNode toRemove) {
		tripGraph.removeNode(toRemove);
	}

	public String getCountry() {
		return country;
	}

	public String getMainCity() {
		return mainCity;
	}

	public EventsGraph getTripGraph() {
		return tripGraph;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setMainCity(String mainCity) {
		this.mainCity = mainCity;
	}

	public void resetTrip() {
		this.tripGraph = new EventsGraph();
		this.proposals = new HashSet<Proposal>();
	}

}
