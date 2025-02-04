package tpgroup.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

public class EventsNode implements Comparable<EventsNode> {
	private String name;
	private TreeSet<Event> events;
	private EventsGraph graph;

	public EventsNode(EventsGraph graph, String name) {
		this.events = new TreeSet<>();
		this.graph = graph;
		this.name = name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TreeSet<Event> getEvents() {
		return events;
	}

	public void setEvents(TreeSet<Event> events) {
		this.events = events;
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

	public boolean split(Event pivot, String newNodeName) {
		if (!this.events.contains(pivot)) {
			return false;
		}
		NavigableSet<Event> toNewNode = this.events.tailSet(pivot, false);
		EventsNode newNode = new EventsNode(graph, newNodeName);
		newNode.events.addAll(toNewNode);
		graph.notifySplit(this, newNode);
		return true;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		EventsNode other = (EventsNode) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public int compareTo(EventsNode arg0) {
		if (this.name.equals(arg0.name))
			return 0;

		return this.getEventsStart().compareTo(arg0.getEventsStart());
	}

}
