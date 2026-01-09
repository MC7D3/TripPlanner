package tpgroup.model.bean;

import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

import tpgroup.model.EventsNode;

public class BranchBean {
	private final UUID id;
	private final NavigableSet<EventBean> events;

	public BranchBean(EventsNode branch) {
		this.id = branch.getId();
		this.events = branch.getEvents().stream().map(event -> new EventBean(event))
				.collect(Collectors.toCollection(() -> new TreeSet<>()));
	}

	public UUID getId() {
		return id;
	}

	public NavigableSet<EventBean> getEvents() {
		return events;
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

	@Override
	public String toString() {
		try {
			return events + " - " + events.first().getStart() + " , " + events.getLast().getEnd();
		} catch (NoSuchElementException e) {
			return "[no events]";
		}
	}

}
