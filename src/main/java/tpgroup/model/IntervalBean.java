package tpgroup.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import tpgroup.model.exception.InvalidBeanParamException;

public class IntervalBean {
	private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
	private final LocalDateTime startTime;
	private final LocalDateTime endTime;

	public IntervalBean(String startTimeTxt, String endTimeTxt) throws InvalidBeanParamException {
		try {
			this.startTime = startTimeTxt.isEmpty() ? null : LocalDateTime.parse(startTimeTxt, format);
		} catch (DateTimeParseException e) {
			throw new InvalidBeanParamException("startTimeTxt");
		}
		try {
			this.endTime = LocalDateTime.parse(endTimeTxt, format);
		} catch (DateTimeParseException e) {
			throw new InvalidBeanParamException("startTimeTxt");
		}
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
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
		IntervalBean other = (IntervalBean) obj;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		return true;
	}

}
