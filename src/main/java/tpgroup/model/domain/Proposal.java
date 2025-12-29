package tpgroup.model.domain;

import java.time.LocalDateTime;
import java.util.Optional;

import tpgroup.model.Event;
import tpgroup.model.EventsNode;

public class Proposal {
	private User creator;
	private final LocalDateTime creationTime;
	private EventsNode nodeName;
	private Event event;
	private Optional<Event> updateEvent;
	private ProposalType proposalType;
	private int likes;

	public Proposal(ProposalType proposalType, EventsNode nodeName, Event event, Optional<Event> updateEvent, User creator,
			int likes, LocalDateTime creationTime) {
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
		this(proposalType, null, event, Optional.empty(), creator, 0, LocalDateTime.now());
	}

	public Proposal(ProposalType proposalType, EventsNode nodeName, Event event, Event updatedEvent, User creator) {
		this(proposalType, null, event, Optional.of(updatedEvent), creator, 0, LocalDateTime.now());
	}

	public EventsNode getNodeName() {
		return nodeName;
	}

	public Event getEvent() {
		return event;
	}

	public int getLikes() {
		return likes;
	}

	public void like() {
		this.likes++;
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

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public void setNodeName(EventsNode nodeName) {
		this.nodeName = nodeName;
	}

	public boolean accept(){
		switch(this.proposalType){
			case ADD:
				nodeName.getEvents().add(event);
				return true;
			case REMOVE:
				nodeName.getEvents().remove(event);
				break;
			case UPDATE:
				if(updateEvent.isEmpty()){
					return false;
				}
				nodeName.getEvents().remove(event);
				nodeName.getEvents().add(updateEvent.get());
			default:
				break;
		}
		return false;
	}

}
