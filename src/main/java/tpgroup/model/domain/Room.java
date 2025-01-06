package tpgroup.model.domain;

import java.util.Random;
import java.util.Set;

public class Room {
	String code;
	String name;
	RoomAdmin admin;
	Set<RoomMember> members;
	Plan plan;
 
	public Room(String name, RoomAdmin admin, Set<RoomMember> members) {
		Random rand = new Random();
		this.code = String.format("%s-%d", name.toLowerCase().replaceAll("[^a-z0-9]","-"), rand.nextInt(100000) + 1); 
		this.name = name;
		this.admin = admin;
		this.members = Set.copyOf(members);
		this.plan = new Plan();
	}

}
