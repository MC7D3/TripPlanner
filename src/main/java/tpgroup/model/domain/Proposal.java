package tpgroup.model.domain;

import java.util.Optional;

import tpgroup.model.Event;
import tpgroup.model.EventsNode;

public class Proposal {
	private final User creator;
	private final EventsNode node;
	private final Event event;
	private final Optional<Event> updateEvent;
	private final ProposalType proposalType;
	private int likes;

	private Proposal(ProposalType proposalType, EventsNode node, Event event, Optional<Event> updateEvent, User creator) {
		super();
		this.proposalType = proposalType;
		this.node = node;
		this.event = event;
		this.creator = creator;
		this.likes = 0;
		this.updateEvent = updateEvent;
	}

	public Proposal(ProposalType proposalType, EventsNode node, Event event, User creator){
		this(proposalType, node, event, Optional.empty(), creator);
	}

	public Proposal(ProposalType proposalType, EventsNode node, Event event, Event updatedEvent, User creator){
		this(proposalType, node, event, Optional.of(updatedEvent), creator);
	}

	public EventsNode getNode() {
		return node;
	}

	public Event getEvent() {
		return event;
	}

	public int getLikes() {
		return likes;
	}

	public void like(){
		this.likes++;
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
		result = prime * result + ((event == null) ? 0 : event.hashCode());
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
		if (event == null) {
			if (other.event != null)
				return false;
		} else if (!event.equals(other.event))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Proposal [creator=" + creator + ", node=" + node + ", event=" + event + ", updateEvent=" + updateEvent
				+ ", proposalType=" + proposalType + ", likes=" + likes + "]";
	}

	public Optional<Event> getUpdateEvent() {
		return updateEvent;
	}

}
