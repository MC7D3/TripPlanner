package tpgroup.persistence.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import tpgroup.model.domain.PointOfInterest;
import tpgroup.model.domain.Room;
import tpgroup.model.domain.User;
import tpgroup.persistence.CacheDecorator;
import tpgroup.persistence.CascadeDecorator;
import tpgroup.persistence.DAO;
import tpgroup.persistence.cascade.UserToProposalCascadeDemo;
import tpgroup.persistence.cascade.UserToRoomCascade;
import tpgroup.persistence.demo.PointOfInterestDAODemo;
import tpgroup.persistence.demo.RoomDAODemo;
import tpgroup.persistence.demo.UserDAODemo;

class DemoDAOFactory extends DAOFactory{
	private static Map<Class<?>, DAO<?>> daos = new HashMap<>();

	static{
		UserDAODemo userDaoDemo = new UserDAODemo();
		RoomDAODemo roomDaoDemo = new RoomDAODemo();
		PointOfInterestDAODemo poiDaoDemo = new PointOfInterestDAODemo();

		CascadeDecorator<User> usrDaoDemoCascade = new CascadeDecorator<>(userDaoDemo)
			.addCascadePolicy(new UserToRoomCascade(roomDaoDemo))
			.addCascadePolicy(new UserToProposalCascadeDemo(roomDaoDemo));


		daos.put(User.class, new CacheDecorator<>(usrDaoDemoCascade));
		daos.put(Room.class, new CacheDecorator<>(roomDaoDemo));
		daos.put(PointOfInterest.class, new CacheDecorator<>(poiDaoDemo));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> DAO<T> getDAO(Class<T> of) {
		return (DAO<T>) Optional.ofNullable(daos.get(of)).orElseThrow();
	}

}
