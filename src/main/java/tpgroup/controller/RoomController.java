package tpgroup.controller;

import tpgroup.model.RoomBean;
import tpgroup.model.Session;
import tpgroup.model.domain.Room;
import tpgroup.model.domain.RoomAdmin;
import tpgroup.model.exception.RoomGenConflictException;
import tpgroup.persistence.DAO;
import tpgroup.persistence.factory.DAOFactory;

public class RoomController {
	public static void createRoom(RoomBean newRoom) throws RoomGenConflictException {
		Room room = new Room(newRoom.getName(), new RoomAdmin(Session.getInstance().getLogged()));
		DAO<Room> roomDao = DAOFactory.getInstance().getDAO(Room.class);
		if (!roomDao.add(room))
			throw new RoomGenConflictException();
	}

}
