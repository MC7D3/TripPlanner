package tpgroup.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

public class EventsNode implements Comparable<EventsNode> {
	private TreeSet<Event> events;
	private EventsGraph graph;

	public EventsNode(EventsGraph graph) {
		this.events = new TreeSet<>();
		this.graph = graph;
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
			graph.checkNodesConflicts(this, event.getStart(), getEventsEnd());
		}

		if (event.compareTo(events.last()) > 0) {
			graph.checkNodesConflicts(this, getEventsStart(), event.getEnd());
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

	public void setEvents(SortedSet<Event> events) {
		this.events = new TreeSet<>(events);
	}

	public EventsGraph getGraph() {
		return graph;
	}

	public void setGraph(EventsGraph graph) {
		this.graph = graph;
	}

	public boolean mergeWith(EventsNode other) {
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
		NavigableSet<Event> toNewNode = this.events.tailSet(pivot, false);
		EventsNode newNode = new EventsNode(graph);
		newNode.events.addAll(toNewNode);
		graph.notifySplit(this, newNode);
		return true;

	}

	@Override
	public int hashCode() {
		return Objects.hash(events, graph);
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
		return Objects.equals(events, other.events) && Objects.equals(graph, other.graph);
	}

	@Override
	public int compareTo(EventsNode arg0) {
		if (this.equals(arg0))
			return 0;

		return this.getEventsStart().compareTo(arg0.getEventsStart());
	}

}
