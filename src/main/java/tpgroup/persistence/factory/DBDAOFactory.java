package tpgroup.persistence.factory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import tpgroup.model.ConfigReader;
import tpgroup.model.domain.PointOfInterest;
import tpgroup.model.domain.Room;
import tpgroup.model.domain.User;
import tpgroup.model.exception.PropertyNotFoundException;
import tpgroup.persistence.CacheDecorator;
import tpgroup.persistence.DAO;
import tpgroup.persistence.database.PointOfInterestDAODB;
import tpgroup.persistence.database.RoomDAODB;
import tpgroup.persistence.database.UserDAODB;

public class DBDAOFactory extends DAOFactory {
	private static Map<Class<?>, DAO<?>> daos = new HashMap<>();

	static {
		Connection conn;
		try {
			ConfigReader reader = new ConfigReader("configuration.properties");
			Class.forName(reader.readJdbcDriver());
			conn = DriverManager.getConnection(reader.readJdbcUrl(), reader.readJdbcUsername(),
					reader.readJdbcPassword());

			UserDAODB userDao = new UserDAODB(conn);
			PointOfInterestDAODB poiDao = new PointOfInterestDAODB(conn);
			RoomDAODB roomDao = new RoomDAODB(conn);

			daos.put(User.class, new CacheDecorator<>(userDao));
			daos.put(Room.class, new CacheDecorator<>(roomDao));
			daos.put(PointOfInterest.class, new CacheDecorator<>(poiDao));
		} catch (ClassNotFoundException | IOException | SQLException | PropertyNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> DAO<T> getDAO(Class<T> of) {
		return (DAO<T>) Optional.ofNullable(daos.get(of)).orElseThrow();
	}

}
