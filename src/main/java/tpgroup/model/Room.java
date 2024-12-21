package tpgroup.model;

import java.util.Set;

public class Room {
	Long code;
	RoomAdmin admin;
	Set<RoomMember> members;
	Plan plan;

	public Room(RoomAdmin admin, Set<RoomMember> members) {
		this.code = 0L; 
		//TODO i have to create something like a codefactory, a random number generator where i keep in a stack o queue i valori gia generati e usati non arrivare a codici enormi in modo ingiustificato
		this.admin = admin;
		this.members = Set.copyOf(members);
		this.plan = new Plan();
	}
}
