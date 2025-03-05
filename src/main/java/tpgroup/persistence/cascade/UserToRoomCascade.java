package tpgroup.persistence.cascade;

import tpgroup.model.domain.Room;
import tpgroup.model.domain.User;
import tpgroup.persistence.Cascade;
import tpgroup.persistence.DAO;

public class UserToRoomCascade extends Cascade<User, Room> {

	public UserToRoomCascade(DAO<Room> to) {
		super(to);
	}

	@Override
	public boolean propagateAdd(User toAdd) {
		// not needed
		return true;
	}

	@Override
	public boolean propagateDelete(User toDelete) {
		for (Room room : getTo().getAll()) {
			if (room.getMembers().contains(toDelete)) {
				room.getMembers().remove(toDelete);
				getTo().save(room);
			}
		}
		return true;
	}

	@Override
	public boolean propagateUpdate(User toUpdate) {
		for (Room room : getTo().getAll()) {
			if (room.isMember(toUpdate)) {
				room.remove(toUpdate);
				room.add(toUpdate);
				getTo().save(room);
			}
		}
		return true;
	}

}
