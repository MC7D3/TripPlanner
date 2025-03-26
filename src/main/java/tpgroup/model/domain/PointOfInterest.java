package tpgroup.model.domain;

import java.util.List;

public class PointOfInterest {
	String name;
	String description;
	String location;
	Rating rating;
	List<Tag> tags;

	public PointOfInterest(String name, String description, String location, Rating rating, List<Tag> tags) {
		this.name = name;
		this.description = description;
		this.location = location;
		this.rating = rating;
		this.tags = tags;
	}

	public PointOfInterest(String name, String location){
		this(name, location, null, null, null);
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
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
		PointOfInterest other = (PointOfInterest) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name + ", " + description + ", (" + location + ") rating="
				+ rating; 
	}

}
