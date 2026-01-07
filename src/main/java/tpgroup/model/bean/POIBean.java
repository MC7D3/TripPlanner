package tpgroup.model.bean;

import java.util.List;
import java.util.Objects;

import tpgroup.model.domain.Coordinates;
import tpgroup.model.domain.PointOfInterest;
import tpgroup.model.domain.Rating;
import tpgroup.model.domain.Tag;

public class POIBean {
	private final String name;
	private final String description;
	private final String city;
	private final String country;
	private final Coordinates coordinates;
	private final Rating rating;
	private final List<Tag> tags;

	public POIBean(PointOfInterest poi) {
		this.name = poi.getName();
		this.description = poi.getDescription();
		this.city = poi.getCity();
		this.country = poi.getCountry();
		this.coordinates = poi.getCoordinates();
		this.rating = poi.getRating();
		this.tags = poi.getTags();

	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public Coordinates getCoordinates() {
		return coordinates;
	}

	public Rating getRating() {
		return rating;
	}

	public List<Tag> getTags() {
		return tags;
	}

	@Override
	public int hashCode() {
		return Objects.hash(coordinates);
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
		return Objects.equals(coordinates, other.coordinates);
	}

	@Override
	public String toString() {
		return "POIBean{name=" + name + ", description=" + description + ", city=" + city + ", country=" + country
				+ ", coordinates=" + coordinates + ", rating=" + rating + ", tags=" + tags + "}";
	}

}
