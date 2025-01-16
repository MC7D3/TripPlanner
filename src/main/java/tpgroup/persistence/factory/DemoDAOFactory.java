package tpgroup.persistence.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import tpgroup.model.domain.Room;
import tpgroup.model.domain.User;

import tpgroup.persistence.DAO;
import tpgroup.persistence.demo.*;

class DemoDAOFactory extends DAOFactory{
	private static Map<Class<?>, DAO<?>> daos = new HashMap<>();

	static{
		daos.put(User.class, UserDAODemo.getInstance());
		daos.put(Room.class, RoomDAODemo.getInstance());
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> DAO<T> getDAO(Class<T> of) {
		return (DAO<T>) Optional.ofNullable(daos.get(of)).orElseThrow();
	}

}
