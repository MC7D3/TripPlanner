package tpgroup.model.domain;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Room {
	private final String code;
	private String name;
	private RoomAdmin admin;
	private Set<RoomMember> members;
	private Plan plan;
 
	public Room(String name, RoomAdmin admin, Set<RoomMember> members) {
		Random rand = new SecureRandom();
		this.code = String.format("%s-%d", name.toLowerCase().replaceAll("[^a-z0-9]","-"), rand.nextInt(100000) + 1); 
		this.name = name;
		this.admin = admin;

		this.members = new HashSet<>();
		this.members.addAll(members);
		this.members.add(admin);

		this.plan = new Plan();
	}

	public Room(String name, RoomAdmin admin) {
		this(name, admin, new HashSet<RoomMember>());
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

	public RoomAdmin getAdmin() {
		return admin;
	}

	public void setAdmin(RoomAdmin admin) {
		this.admin = admin;
	}

	public Set<RoomMember> getMembers() {
		return members;
	}

	public boolean add(User user) {
		return members.add(new RoomMember(user));
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

	public void setMembers(Set<RoomMember> members) {
		this.members = members;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
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
		Room other = (Room) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Room [code=" + code + ", name=" + name + ", admin=" + admin + ", members=" + members + "]";
	}	

}
