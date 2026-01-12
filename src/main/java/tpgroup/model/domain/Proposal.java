package tpgroup.model.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import tpgroup.model.Event;
import tpgroup.model.EventsNode;

public class Proposal {
	private User creator;
	private final LocalDateTime creationTime;
	private EventsNode nodeName;
	private Event event;
	private Optional<Event> updateEvent;
	private ProposalType proposalType;
	private Set<User> likes;

	public Proposal(ProposalType proposalType, EventsNode nodeName, Event event, Optional<Event> updateEvent,
			User creator,
			Set<User> likes, LocalDateTime creationTime) {
		super();
		this.proposalType = proposalType;
		this.nodeName = nodeName;
		this.event = event;
		this.creator = creator;
		this.likes = likes;
		this.updateEvent = updateEvent;
		this.creationTime = creationTime;
	}

	public Proposal(ProposalType proposalType, EventsNode nodeName, Event event, User creator) {
		this(proposalType, nodeName, event, Optional.empty(), creator, new HashSet<>(), LocalDateTime.now());
	}

	public Proposal(ProposalType proposalType, EventsNode nodeName, Event event, Event updatedEvent, User creator) {
		this(proposalType, nodeName, event, Optional.of(updatedEvent), creator, new HashSet<>(), LocalDateTime.now());
	}

	public EventsNode getNodeName() {
		return nodeName;
	}

	public Event getEvent() {
		return event;
	}

	public int getLikes() {
		return likes.size();
	}

	public boolean like(User user) {
		return likes.add(user);
	}

	public boolean unlike(User user) {
		return likes.remove(user);
	}

	public User getCreator() {
		return creator;
	}

	public ProposalType getProposalType() {
		return proposalType;
	}

	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	@Override
	public String toString() {
		return "Proposal [creator=" + creator + ", node name=" + nodeName + ", event=" + event + ", updateEvent="
				+ updateEvent
				+ ", proposalType=" + proposalType + ", likes=" + likes + "]";
	}

	public Optional<Event> getUpdateEvent() {
		return updateEvent;
	}

	public void setNode(EventsNode nodeName) {
		this.nodeName = nodeName;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public void setUpdateEvent(Optional<Event> updateEvent) {
		this.updateEvent = updateEvent;
	}

	public void setProposalType(ProposalType proposalType) {
		this.proposalType = proposalType;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public boolean accept() {
		switch (this.proposalType) {
			case ADD:
				return nodeName.getEvents().add(event);
			case REMOVE:
				return nodeName.getEvents().remove(event);
			case UPDATE:
				if (updateEvent.isEmpty()) {
					break;
				}
				if (!nodeName.getEvents().remove(event)) {
					break;
				}
				return nodeName.addEvent(updateEvent.get());
			default:
				break;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(creator, nodeName, event, updateEvent, proposalType);
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
		Proposal other = (Proposal) obj;
		return Objects.equals(creator, other.creator)
				&& Objects.equals(nodeName, other.nodeName) && Objects.equals(event, other.event)
				&& Objects.equals(updateEvent, other.updateEvent) && proposalType == other.proposalType;
	}

}
