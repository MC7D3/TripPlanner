package tpgroup.controller;

import java.util.List;

import tpgroup.model.RoomBean;
import tpgroup.model.RoomCodeBean;
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

	public static void deleteRoom() {
		DAO<Room> roomDao = DAOFactory.getInstance().getDAO(Room.class);
		try {
			roomDao.delete(Session.getInstance().getEnteredRoom());
			Session.getInstance().setEnteredRoom(null);
		} catch (RecordNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}

	public static List<Room> getJoinedRooms() {
		return DAOFactory.getInstance().getDAO(Room.class)
				.getFiltered(room -> room.getMembers().contains(Session.getInstance().getLogged()));
	}

	public static boolean abbandonRoom(Room toAbbandon) {
		User user = Session.getInstance().getLogged();
		DAO<Room> roomDao = DAOFactory.getInstance().getDAO(Room.class);
		try {
			toAbbandon.getMembers().remove(user);
			if (user.equals(toAbbandon.getAdmin()) && toAbbandon.getMembers().size() > 1) {
				toAbbandon.setAdmin(toAbbandon.getMembers().stream().findAny().get());	
			}else if (user.equals(toAbbandon.getAdmin())){
				roomDao.delete(toAbbandon);
				return true;
			}
			roomDao.save(toAbbandon);
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

	public static boolean joinRoom(RoomCodeBean roomCode){
		DAO<Room> roomDao = DAOFactory.getInstance().getDAO(Room.class);
		List<Room> matches = roomDao.getFiltered(room -> room.getCode().equals(roomCode.getCode()));
		if(matches.isEmpty()){
			return false;
		}
		if(matches.size() > 1){
			throw new IllegalStateException();
		}
		Room chosen = matches.get(0);
		chosen.add(Session.getInstance().getLogged());
		Session.getInstance().setEnteredRoom(chosen);
		roomDao.save(chosen);
		return true;
	}


}
