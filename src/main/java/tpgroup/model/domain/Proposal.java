package tpgroup.model.domain;

import tpgroup.model.Event;
import tpgroup.model.EventsNode;

public class Proposal {
	private final User creator;
	private final EventsNode node;
	private final Event proposal;
	private final ProposalType proposalType;
	private int likes;
	private int dislikes;

	public Proposal(ProposalType proposalType, EventsNode node, Event proposal, User creator) {
		super();
		this.proposalType = proposalType;
		this.node = node;
		this.proposal = proposal;
		this.creator = creator;
		this.likes = 0;
		this.dislikes = 0;
	}

	public EventsNode getNode() {
		return node;
	}

	public Event getProposal() {
		return proposal;
	}

	public int getLikes() {
		return likes;
	}

	public int getDislikes() {
		return dislikes;
	}

	public void like(){
		this.likes++;
	}

	public void dislike(){
		this.dislikes++;
	}

	public User getCreator() {
		return creator;
	}

	public ProposalType getProposalType() {
		return proposalType;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((node == null) ? 0 : node.hashCode());
		result = prime * result + ((proposal == null) ? 0 : proposal.hashCode());
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
		Proposal other = (Proposal) obj;
		if (creator == null) {
			if (other.creator != null)
				return false;
		} else if (!creator.equals(other.creator))
			return false;
		if (node == null) {
			if (other.node != null)
				return false;
		} else if (!node.equals(other.node))
			return false;
		if (proposal == null) {
			if (other.proposal != null)
				return false;
		} else if (!proposal.equals(other.proposal))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Proposal [creator=" + creator + ", node=" + node + ", proposal=" + proposal + ", proposalType="
				+ proposalType + "]";
	}

}
