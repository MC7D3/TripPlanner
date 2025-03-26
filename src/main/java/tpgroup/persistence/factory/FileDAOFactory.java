package tpgroup.persistence.factory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import tpgroup.model.ConfigReader;
import tpgroup.persistence.DAO;

public class FileDAOFactory extends DAOFactory{
	private static Map<Class<?>, DAO<?>> daos = new HashMap<>();

	static{			
		try {
			ConfigReader reader = new ConfigReader("configuration.properties");
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	
	}

	@Override
	public <T> DAO<T> getDAO(Class<T> of) {
		throw new UnsupportedOperationException("Unimplemented method 'getDAO'");
	}
	
}
