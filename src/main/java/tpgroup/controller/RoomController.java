package tpgroup.controller;

import tpgroup.model.RoomBean;
import tpgroup.model.Session;
import tpgroup.model.domain.Room;
import tpgroup.model.domain.RoomAdmin;
import tpgroup.persistence.DAO;
import tpgroup.persistence.factory.DAOFactory;

public class RoomController {
	public static boolean createRoom(RoomBean newRoom){
		Room room = new Room(newRoom.getName(), new RoomAdmin(Session.getInstance().getLogged()));
		DAO<Room> roomDao = DAOFactory.getInstance().getDAO(Room.class);
		return roomDao.add(room);
	}

}
