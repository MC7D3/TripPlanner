package tpgroup.model.domain;

import java.util.List;
import java.util.Objects;

public class PointOfInterest {
	String name;
	String description;
	String city;
	String country;
	Coordinates coordinates;
	Rating rating;
	List<Tag> tags;

	public PointOfInterest(String name, String description, String city, String country, Coordinates coordinates,
			Rating rating, List<Tag> tags) {
		this.name = name;
		this.description = description;
		this.city = city;
		this.country = country;
		this.coordinates = coordinates;
		this.rating = rating;
		this.tags = tags;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Coordinates getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}

	public Rating getRating() {
		return rating;
	}

	public void setRating(Rating rating) {
		this.rating = rating;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
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
		PointOfInterest other = (PointOfInterest) obj;
		return Objects.equals(coordinates, other.coordinates);
	}

	@Override
	public String toString() {
		return name + ", " + description + "(" + city + ", "
				+ country + "), rating= " + rating + " - tags=" + tags;
	}

}
