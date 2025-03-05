package tpgroup.persistence.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import tpgroup.model.domain.PointOfInterest;
import tpgroup.model.domain.Room;
import tpgroup.model.domain.User;

import tpgroup.persistence.DAO;
import tpgroup.persistence.cascade.UserToRoomCascade;
import tpgroup.persistence.demo.*;

class DemoDAOFactory extends DAOFactory{
	private static Map<Class<?>, DAO<?>> daos = new HashMap<>();

	static{
		UserDAODemo userDaoDemo = new UserDAODemo();
		RoomDAODemo roomDaoDemo = new RoomDAODemo();
		PointOfInterestDAODemo poiDaoDemo = new PointOfInterestDAODemo();

		userDaoDemo.setCascadePolicy(new UserToRoomCascade(roomDaoDemo));

		daos.put(User.class, userDaoDemo);
		daos.put(Room.class, roomDaoDemo);
		daos.put(PointOfInterest.class, poiDaoDemo);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> DAO<T> getDAO(Class<T> of) {
		return (DAO<T>) Optional.ofNullable(daos.get(of)).orElseThrow();
	}

}
