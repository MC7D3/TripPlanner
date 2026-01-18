package tpgroup.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import tpgroup.model.domain.PointOfInterest;

public class Event implements Comparable<Event> {
	private final PointOfInterest info;
	private final LocalDateTime start;
	private final LocalDateTime end;

	public Event(PointOfInterest info, LocalDateTime start, LocalDateTime end) {
		this.info = info;
		this.start = start.truncatedTo(ChronoUnit.MINUTES);
		this.end = end.truncatedTo(ChronoUnit.MINUTES);
	}

	public boolean overlapsWith(Event event) {
		return this.start.isBefore(event.getEnd()) &&
				event.getStart().isBefore(this.end);
	}

	public PointOfInterest getInfo() {
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
		Event other = (Event) obj;
		return Objects.equals(info, other.info) && Objects.equals(start, other.start) && Objects.equals(end, other.end);
	}

	@Override
	public int compareTo(Event arg0) {
		return this.start.compareTo(arg0.start);
	}

	@Override
	public String toString() {
		return "Event [" + info + ", start=" + start + ", end=" + end + "]";
	}

}
