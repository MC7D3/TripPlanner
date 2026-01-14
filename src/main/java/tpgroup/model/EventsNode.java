package tpgroup.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import tpgroup.model.exception.NodeConflictException;

public class EventsNode {
	private final UUID id;
	private NavigableSet<Event> events;
	private final EventsGraph graph;

	public EventsNode(UUID id, NavigableSet<Event> events, EventsGraph graph){
		this.id = id;
		this.events = events;
		this.graph = graph;
	}

	public EventsNode(EventsGraph graph) {
		this(UUID.randomUUID(), new TreeSet<>(), graph);
	}

	public void resetEvents(){
		events = new TreeSet<>();
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

		if (!events.isEmpty() && event.compareTo(events.first()) < 0) {
			graph.checkFatherNodesConflicts(this, event.getStart());
		}

		if (!events.isEmpty() && event.compareTo(events.last()) > 0) {
			graph.checkChildNodesConflicts(this, getEventsStart());
		}

		events.add(event);
		return true;
	}

	public boolean removeEvent(Event event) throws NodeConflictException{
		if(events.size() == 1){
			if(graph.getConnectedNodes(this).isEmpty()){
				graph.removeNode(this);
				return true;
			}else{
				throw new NodeConflictException("cannot have a node with no events with child nodes");
			}
		}
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

	public EventsGraph getGraph() {
		return graph;
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
		NavigableSet<Event> toNewNode = new TreeSet<>(this.events.tailSet(pivot, true));
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
	public String toString() {
		return "EventsNode{id=" + id + ", events=" + events + "}";
	}

}
