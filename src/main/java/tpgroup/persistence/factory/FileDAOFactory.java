package tpgroup.persistence.factory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import tpgroup.model.ConfigReader;
import tpgroup.model.domain.PointOfInterest;
import tpgroup.model.domain.Room;
import tpgroup.model.domain.User;
import tpgroup.model.exception.PropertyNotFoundException;
import tpgroup.persistence.DAO;
import tpgroup.persistence.filesystem.PointOfInterestDAOFile;
import tpgroup.persistence.filesystem.RoomDAOFile;
import tpgroup.persistence.filesystem.UserDAOFile;

public class FileDAOFactory extends DAOFactory{
	private static Map<Class<?>, DAO<?>> daos = new HashMap<>();

	static{			
		try {
			ConfigReader reader = new ConfigReader("configuration.properties");
			String filePath = reader.readFileDBRoot();
			UserDAOFile userDAO = new UserDAOFile(filePath + "user.json");
			PointOfInterestDAOFile poiDAO = new PointOfInterestDAOFile(filePath + "poi.json");
			RoomDAOFile roomDAO = new RoomDAOFile(filePath + "room.json");
			daos.put(User.class, userDAO);
			daos.put(PointOfInterest.class, poiDAO);
			daos.put(Room.class, roomDAO);
		} catch (IOException | PropertyNotFoundException e) {
			throw new IllegalStateException(e);
		}
	
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> DAO<T> getDAO(Class<T> of) {
		return (DAO<T>) daos.get(of);
	}
	
}
