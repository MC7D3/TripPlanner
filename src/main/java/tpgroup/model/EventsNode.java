package tpgroup.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import tpgroup.model.exception.NodeConflictException;

public class EventsNode implements Comparable<EventsNode> {
	private final UUID id;
	private NavigableSet<Event> events;
	private EventsGraph graph;

	public EventsNode(UUID id, NavigableSet<Event> events, EventsGraph graph){
		this.id = id;
		this.events = events;
		this.graph = graph;
	}

	public EventsNode(EventsGraph graph) {
		this(UUID.randomUUID(), new TreeSet<>(), graph);
	}

	public boolean addEvent(Event event) {
		Event beforeCandidate = events.floor(event);
		Event afterCandidate = events.ceiling(event);

		if (beforeCandidate != null && beforeCandidate.overlapsWith(event)) {
			return false;
		}

		if (afterCandidate != null && afterCandidate.overlapsWith(event)) {
			return false;
		}

		if (event.compareTo(events.first()) < 0) {
			graph.checkFatherNodesConflicts(this, event.getStart());
		}

		if (event.compareTo(events.last()) > 0) {
			graph.checkChildNodesConflicts(this, getEventsStart());
		}

		events.add(event);
		return true;
	}

	public boolean removeEvent(Event event) {
		return events.remove(event);
	}

	public List<Event> allEvents() {
		return events.stream().toList();
	}

	public LocalDateTime getEventsStart() {
		return events.first().getStart();
	}

	public LocalDateTime getEventsEnd() {
		return events.last().getEnd();
	}

	public SortedSet<Event> getEvents() {
		return events;
	}

	public UUID getId(){
		return id;
	}

	public void setEvents(SortedSet<Event> events) {
		this.events = new TreeSet<>(events);
	}

	public EventsGraph getGraph() {
		return graph;
	}

	public void setGraph(EventsGraph graph) {
		this.graph = graph;
	}

	public boolean mergeWith(EventsNode other) throws NodeConflictException{
		if (!graph.areConnected(this, other) && graph.connCount(this) != 1) {
			return false;
		}
		this.events.addAll(other.events);
		graph.removeNode(other);
		return true;
	}

	public boolean split(Event pivot) {
		if (!this.events.contains(pivot)) {
			return false;
		}
		NavigableSet<Event> toNewNode = new TreeSet<>(this.events.tailSet(pivot, false));
		this.events.removeAll(toNewNode);
		EventsNode newNode = new EventsNode(graph);
		newNode.events.addAll(toNewNode);
		graph.notifySplit(this, newNode);
		return true;

	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
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
		EventsNode other = (EventsNode) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public int compareTo(EventsNode arg0) {
		if (this.equals(arg0))
			return 0;

		return this.getEventsStart().compareTo(arg0.getEventsStart());
	}

}
