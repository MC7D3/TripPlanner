package tpgroup.model;

import java.util.Objects;

import tpgroup.model.domain.PointOfInterest;

public class POIBean {
	private final PointOfInterest poi;

	public POIBean(PointOfInterest poi) {
		super();
		this.poi = poi;
	}

	public PointOfInterest getPoi() {
		return poi;
	}

	@Override
	public int hashCode() {
		return Objects.hash(poi);
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
		POIBean other = (POIBean) obj;
		return Objects.equals(poi, other.poi);
	}

	
}
