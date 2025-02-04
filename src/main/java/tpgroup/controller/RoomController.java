package tpgroup.controller;

import java.util.List;

import tpgroup.model.RoomBean;
import tpgroup.model.domain.Room;
import tpgroup.model.domain.RoomAdmin;
import tpgroup.model.domain.User;
import tpgroup.model.exception.RecordNotFoundException;
import tpgroup.model.exception.RoomGenConflictException;
import tpgroup.persistence.DAO;
import tpgroup.persistence.factory.DAOFactory;

public class RoomController {
	
	private RoomController(){
		super();
	}
	
	public static void createRoom(User creator, RoomBean newRoom) throws RoomGenConflictException {
		Room room = new Room(newRoom.getName(), new RoomAdmin(creator), newRoom.getDestination());
		DAO<Room> roomDao = DAOFactory.getInstance().getDAO(Room.class);
		if (!roomDao.add(room))
			throw new RoomGenConflictException();
	}

	public static List<Room> getJoinedRooms(User user) {
		return DAOFactory.getInstance().getDAO(Room.class)
				.getFiltered(room -> room.getAdmin().equals(user));
	}

	public static boolean abbandonRoom(User user, Room room) {
		DAO<Room> roomDao = DAOFactory.getInstance().getDAO(Room.class);
		try {
			Room rm = roomDao.get(room);
			rm.getMembers().remove(user);
			if (user.equals(room.getAdmin()) && room.getMembers().size() > 1) {
				rm.setAdmin(new RoomAdmin(rm.getMembers().stream().findAny().get()));	
			}else if (user.equals(room.getAdmin())){
				roomDao.delete(rm);
				return true;
			}
			roomDao.save(rm);
		} catch (RecordNotFoundException e) {
			//room no longer exist, no action needed
		}
		return true;
	}
}
