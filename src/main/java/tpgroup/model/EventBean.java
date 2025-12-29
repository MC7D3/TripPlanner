package tpgroup.model;

import java.util.Objects;

public class EventBean {
	private final Event event;

	public EventBean(Event event) {
		super();
		this.event = event;
	}

	public Event getEvent() {
		return event;
	}

	@Override
	public int hashCode() {
		return Objects.hash(event);
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
		return Objects.equals(event, other.event);
	}

}
