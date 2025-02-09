package tpgroup.controller;

import java.util.List;

import tpgroup.model.RoomBean;
import tpgroup.model.Session;
import tpgroup.model.domain.Room;
import tpgroup.model.domain.User;
import tpgroup.model.exception.RecordNotFoundException;
import tpgroup.model.exception.RoomGenConflictException;
import tpgroup.persistence.DAO;
import tpgroup.persistence.factory.DAOFactory;

public class RoomController {
	
	private RoomController(){
		super();
	}
	
	public static void createRoom(RoomBean newRoom) throws RoomGenConflictException {
		Room room = new Room(newRoom.getName(), Session.getInstance().getLogged(), newRoom.getDestination());
		DAO<Room> roomDao = DAOFactory.getInstance().getDAO(Room.class);
		if (!roomDao.add(room))
			throw new RoomGenConflictException();
	}

	public static List<Room> getJoinedRooms() {
		return DAOFactory.getInstance().getDAO(Room.class)
				.getFiltered(room -> room.getMembers().contains(Session.getInstance().getLogged()));
	}

	public static boolean abbandonRoom(String roomCode) {
		User user = Session.getInstance().getLogged();
		DAO<Room> roomDao = DAOFactory.getInstance().getDAO(Room.class);
		try {
			Room rm = roomDao.get(new Room(roomCode));
			rm.getMembers().remove(user);
			if (user.equals(rm.getAdmin()) && rm.getMembers().size() > 1) {
				rm.setAdmin(rm.getMembers().stream().findAny().get());	
			}else if (user.equals(rm.getAdmin())){
				roomDao.delete(rm);
				return true;
			}
			roomDao.save(rm);
		} catch (RecordNotFoundException e) {
			return false;
		}
		return true;
	}

	public static void enterRoom(Room room){
		Session.getInstance().setEnteredRoom(room);
	}

	public static boolean amIAdmin(){
		return Session.getInstance().getEnteredRoom().getAdmin().equals(Session.getInstance().getLogged());
	}


}
