package tpgroup.model.bean;

import java.util.NavigableSet;
import java.util.Objects;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

import tpgroup.model.EventsNode;

public class BranchBean {
	private final UUID id;
	private final NavigableSet<EventBean> events;
	private final EventsGraphBean graph;

	public BranchBean(EventsNode branch){
		this.id = branch.getId();
		this.events = branch.getEvents().stream().map(event -> new EventBean(event)).collect(Collectors.toCollection(() -> new TreeSet<>()));
		this.graph = new EventsGraphBean(branch.getGraph());
	}

	public UUID getId() {
		return id;
	}

	public NavigableSet<EventBean> getEvents() {
		return events;
	}

	public EventsGraphBean getGraph() {
		return graph;
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
		BranchBean other = (BranchBean) obj;
		return Objects.equals(id, other.id);
	}

}
