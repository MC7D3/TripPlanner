package tpgroup.model.domain;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class Room {
	private final String code;
	private String name;
	private User admin;
	private Set<User> members;
	private Trip trip;
 
	public Room(String code, String name, User admin, Set<User> members, Trip trip) {
		this.code = code;
		this.name = name;
		this.admin = admin;
		this.members = members;
		this.trip = trip;
	}

	public Room(String name, User admin, String country, String mainCity) {
		Random rand = new SecureRandom();
		this.code = String.format("%s-%d", name.toLowerCase().replaceAll("[^a-z0-9]","-"), rand.nextInt(99999) + 1); 
		this.name = name;
		this.admin = admin;

		this.members = new HashSet<>();
		this.members.add(admin);

		this.trip = new Trip(country, mainCity);
	}

	public Room(String code){
		this.code = code;
		this.name = null;
		this.admin = null;
		this.members = null;
		this.trip = null;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getAdmin() {
		return admin;
	}

	public void setAdmin(User admin) {
		this.admin = admin;
	}

	public Set<User> getMembers() {
		return members;
	}

	public boolean add(User user) {
		return members.add(user);
	}

	public boolean isMember(User user) {
		return members.contains(user);
	}

	public boolean remove(User user) {
		return members.remove(user);
	}

	public int size() {
		return members.size();
	}

	public void setMembers(Set<User> members) {
		this.members = members;
	}

	public Trip getTrip() {
		return trip;
	}

	public void setTrip(Trip plan) {
		this.trip = plan;
	}

	@Override
	public int hashCode() {
		return Objects.hash(code, name, admin, members, trip);
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
		Room other = (Room) obj;
		return Objects.equals(code, other.code) && Objects.equals(name, other.name)
				&& Objects.equals(admin, other.admin) && Objects.equals(members, other.members)
				&& Objects.equals(trip, other.trip);
	}

	@Override
	public String toString() {
		return "Room{code=" + code + ", name=" + name + ", admin=" + admin + ", members=" + members + ", trip=" + trip
				+ "}";
	}

}
