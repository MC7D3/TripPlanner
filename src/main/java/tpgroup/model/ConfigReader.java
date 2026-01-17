package tpgroup.model;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import tpgroup.model.exception.InvalidPersistenceTypeException;
import tpgroup.model.exception.InvalidViewTypeException;
import tpgroup.model.exception.PropertyNotFoundException;
import tpgroup.persistence.PersistenceType;
import tpgroup.view.ViewType;

public class ConfigReader {
	Properties conf;

	public ConfigReader(String path) throws IOException {
		File confFile = new File(path);
		this.conf = new Properties();
		conf.load(new FileReader(confFile));
	}

	public String readFileDBRoot() throws PropertyNotFoundException {
		String res = conf.getProperty("dbfileroot");
		if (res != null)
			return res;
		throw new PropertyNotFoundException();
	}

	public PersistenceType readPersistenceType() throws InvalidPersistenceTypeException {
		try {
			return PersistenceType.valueOf(conf.getProperty("persistencetype"));
		} catch (IllegalArgumentException e) {
			throw new InvalidPersistenceTypeException(e);
		}
	}

	public ViewType readViewType() throws InvalidViewTypeException {
		try {
			return ViewType.valueOf(conf.getProperty("viewtype"));
		} catch (IllegalArgumentException e) {
			throw new InvalidViewTypeException(e);
		}
	}

	public String readJdbcUrl() throws PropertyNotFoundException {
		String res = conf.getProperty("jdbc.url");
		if (res != null)
			return res;
		throw new PropertyNotFoundException();
	}

	public String readJdbcUsername() throws PropertyNotFoundException {
		String res = conf.getProperty("jdbc.username");
		if (res != null)
			return res;
		throw new PropertyNotFoundException();
	}

	public String readJdbcPassword() throws PropertyNotFoundException {
		String res = conf.getProperty("jdbc.password");
		if (res != null)
			return res;
		throw new PropertyNotFoundException();
	}

	public String readJdbcDriver() throws PropertyNotFoundException {
		String res = conf.getProperty("jdbc.driver");
		if (res != null)
			return res;
		throw new PropertyNotFoundException();
	}

	public boolean readReloadPOI() throws PropertyNotFoundException {
		String res = conf.getProperty("reloadPOI");
		if (res != null)
			return Boolean.parseBoolean(res);
		throw new PropertyNotFoundException();
	}

	public boolean readMockData() throws PropertyNotFoundException {
		String res = conf.getProperty("mockdata");
		if (res != null)
			return Boolean.parseBoolean(res);
		throw new PropertyNotFoundException();
	}
}
