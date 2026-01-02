package tpgroup.controller;

import java.util.List;

import tpgroup.model.RoomBean;
import tpgroup.model.Session;
import tpgroup.model.domain.Room;
import tpgroup.model.domain.User;
import tpgroup.model.exception.InvalidBeanParamException;
import tpgroup.model.exception.RecordNotFoundException;
import tpgroup.model.exception.RoomGenConflictException;
import tpgroup.persistence.DAO;
import tpgroup.persistence.factory.DAOFactory;

public class RoomController {

	private RoomController() {
		super();
	}

	public static void createRoom(RoomBean newRoom) throws RoomGenConflictException, InvalidBeanParamException{
		Room room = new Room(newRoom.getName(), Session.getInstance().getLogged(), newRoom.getCountry(),
				newRoom.getCity());
		if(!(POIController.isValidCountry(room.getTrip().getCountry()) || POIController.isValidCity(room.getTrip().getMainCity()))){
		}else{
			throw new InvalidBeanParamException("country");
		}
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

	public static List<RoomBean> getJoinedRooms() {
		return DAOFactory.getInstance().getDAO(Room.class)
				.getFiltered(room -> room.getMembers().contains(Session.getInstance().getLogged()))
				.stream().map(item -> new RoomBean(item)).toList();
	}

	public static boolean abbandonRoom(RoomBean toAbbandonBean) {
		User user = Session.getInstance().getLogged();
		Room toAbbandon = toAbbandonBean.getRoom();
		DAO<Room> roomDao = DAOFactory.getInstance().getDAO(Room.class);
		try {
			toAbbandon.getMembers().remove(user);
			if (user.equals(toAbbandon.getAdmin()) && toAbbandon.getMembers().size() > 1) {
				toAbbandon.setAdmin(toAbbandon.getMembers().stream().findAny().get());
			} else if (user.equals(toAbbandon.getAdmin())) {
				roomDao.delete(toAbbandon);
				return true;
			}
			roomDao.save(toAbbandon);
		} catch (RecordNotFoundException e) {
			return false;
		}
		return true;
	}

	public static void enterRoom(RoomBean room) {
		Session.getInstance().setEnteredRoom(room.getRoom());
	}

	public static boolean amIAdmin() {
		return Session.getInstance().getEnteredRoom().getAdmin().equals(Session.getInstance().getLogged());
	}

	// inteso per la prima volta che entri, volte successive usi la card
	public static boolean joinRoom(RoomBean roomCode) {
		DAO<Room> roomDao = DAOFactory.getInstance().getDAO(Room.class);
		List<Room> matches = roomDao.getFiltered(room -> room.getCode().equals(roomCode.getCode()));
		if (matches.isEmpty()) {
			return false;
		}
		if (matches.size() > 1) {
			throw new IllegalStateException();
		}
		Room chosen = matches.get(0);
		chosen.add(Session.getInstance().getLogged());
		Session.getInstance().setEnteredRoom(chosen);
		roomDao.save(chosen);
		return true;
	}

}
