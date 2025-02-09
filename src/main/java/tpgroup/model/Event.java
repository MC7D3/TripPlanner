package tpgroup.model;

import java.time.LocalDateTime;

import tpgroup.model.domain.PointOfInterest;

public class Event implements Comparable<Event>{
	private final PointOfInterest info;
	private LocalDateTime start;
	private LocalDateTime end;

	public Event(PointOfInterest info, LocalDateTime start, LocalDateTime end) {
		this.info = info;
		this.start = start;
		this.end = end;
	}

	public boolean overlapsWith(Event event){
        return this.start.isBefore(event.getEnd()) &&
               event.getStart().isBefore(this.end);
	}

	public PointOfInterest getInfo() {
		return info;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public LocalDateTime getEnd() {
		return end;
	}

	public void setEnd(LocalDateTime end) {
		this.end = end;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((info == null) ? 0 : info.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		result = prime * result + ((end == null) ? 0 : end.hashCode());
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
		Event other = (Event) obj;
		if (info == null) {
			if (other.info != null)
				return false;
		} else if (!info.equals(other.info))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		return true;
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
