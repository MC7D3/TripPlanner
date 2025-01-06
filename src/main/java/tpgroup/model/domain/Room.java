package tpgroup.model.domain;

import java.util.Set;

public class Room {
	String code;
	String name;
	RoomAdmin admin;
	Set<RoomMember> members;
	Plan plan;
 
	public Room(String name, RoomAdmin admin, Set<RoomMember> members) {
		this.code = String.format("%s-%d", name.toLowerCase().replaceAll("[^a-z0-9]","-"), (int) Math.random() * 100000); 
		this.name = name;
		this.admin = admin;
		this.members = Set.copyOf(members);
		this.plan = new Plan();
	}

}
