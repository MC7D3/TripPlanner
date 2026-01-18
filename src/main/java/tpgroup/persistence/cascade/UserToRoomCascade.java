package tpgroup.persistence.cascade;

import java.util.List;

import tpgroup.model.domain.Room;
import tpgroup.model.domain.User;
import tpgroup.model.exception.RecordNotFoundException;
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
			if(room.getAdmin().equals(toDelete)){
				if(!room.getMembers().isEmpty()){
					room.setAdmin(room.getMembers().stream().findFirst().get());
				}else{
					try {
						getTo().delete(room);
					} catch (RecordNotFoundException e) {
						throw new IllegalStateException(e);
					}
				}
			}
		}
		return true;
	}

	@Override
	public boolean propagateUpdate(User toUpdate) {
		for(Room room : getTo().getFiltered(room -> room.isMember(toUpdate))){
			room.remove(toUpdate);
			room.add(toUpdate);
			getTo().save(room);
		}
		return true;
	}

	@Override
	public List<User> propagateGetAll(List<User> toGet) {
		return toGet; //not needed
	}

	@Override
	public User propagateGet(User toGet) {
		return toGet; //not needed
	}

}
