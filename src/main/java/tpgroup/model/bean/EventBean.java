package tpgroup.model.bean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import tpgroup.model.Event;

public class EventBean implements Comparable<Event> {
	private final POIBean info;
	private final LocalDateTime start;
	private final LocalDateTime end;

	public EventBean(Event event) {
		super();
		this.info = new POIBean(event.getInfo());
		this.start = event.getStart();
		this.end = event.getEnd();
	}

	public POIBean getInfo() {
		return info;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public LocalDateTime getEnd() {
		return end;
	}

	@Override
	public int hashCode() {
		return Objects.hash(info, start, end);
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
		EventBean other = (EventBean) obj;
		return Objects.equals(info, other.info) && Objects.equals(start, other.start) && Objects.equals(end, other.end);
	}

	@Override
	public int compareTo(Event arg0) {
		return this.start.compareTo(arg0.getStart());
	}

	@Override
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
		return info + "\nbegin: " + formatter.format(start) + " , end: " + formatter.format(end);
	}
}
