package tpgroup.controller;

import java.util.List;

import tpgroup.model.Session;
import tpgroup.model.bean.RoomBean;
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

	public static RoomBean getEnteredRoom(){
		return new RoomBean(Session.getInstance().getEnteredRoom());
	}

	public static void createRoom(RoomBean newRoom) throws RoomGenConflictException, InvalidBeanParamException {
		Room room = new Room(newRoom.getName(), Session.getInstance().getLogged(), newRoom.getTrip().getCountry(),
				newRoom.getTrip().getMainCity());
		if (!POIController.isValidCountry(room.getTrip().getCountry())) {
			throw new InvalidBeanParamException("country");
		}
		if (!POIController.isValidCity(room.getTrip().getMainCity())) {
			throw new InvalidBeanParamException("city");
		}
		DAO<Room> roomDao = DAOFactory.getInstance().getDAO(Room.class);
		if (!roomDao.add(room))
			throw new RoomGenConflictException();
		Session.getInstance().setEnteredRoom(room);
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
		try {
			User user = Session.getInstance().getLogged();
			DAO<Room> roomDao = DAOFactory.getInstance().getDAO(Room.class);
			Room toAbbandon = roomDao.get(new Room(toAbbandonBean.getCode()));
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

	public static boolean enterRoom(RoomBean room) {
		try {
			DAO<Room> roomDao = DAOFactory.getInstance().getDAO(Room.class);
			Room entered = roomDao.get(new Room(room.getCode()));
			Session.getInstance().setEnteredRoom(entered);
		} catch (RecordNotFoundException e) {
			return false;
		}
		return true;
	}

	public static boolean amIAdmin() {
		return Session.getInstance().getEnteredRoom().getAdmin().equals(Session.getInstance().getLogged());
	}

	// inteso per la prima volta che entri, volte successive usi la card
	public static boolean joinRoom(RoomBean roomCode) {
		try {
			DAO<Room> roomDao = DAOFactory.getInstance().getDAO(Room.class);
			Room joined = roomDao.get(new Room(roomCode.getName()));
			joined.add(Session.getInstance().getLogged());
			Session.getInstance().setEnteredRoom(joined);
			roomDao.save(joined);
		} catch (RecordNotFoundException e) {
			return false;
		}
		return true;
	}

}
