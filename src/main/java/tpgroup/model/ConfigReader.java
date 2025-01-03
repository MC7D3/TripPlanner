package tpgroup.model;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import tpgroup.model.exception.InvalidPersistenceTypeException;
import tpgroup.model.exception.InvalidViewTypeException;
import tpgroup.persistence.PersistenceType;
import tpgroup.view.ViewType;

public class ConfigReader {
	Properties conf;

	public ConfigReader(String path) throws IOException{
		File confFile = new File(path);
		this.conf = new Properties();
		conf.load(new FileReader(confFile));
	}

	public PersistenceType readPersistenceType() throws InvalidPersistenceTypeException{
		try{
			return PersistenceType.valueOf(conf.getProperty("persistencetype"));
		}catch(IllegalArgumentException e){
			throw new InvalidPersistenceTypeException(e);
		}
	}

	public ViewType readViewType() throws InvalidViewTypeException{
		try {
			return ViewType.valueOf(conf.getProperty("viewtype"));
		} catch (Exception e) {
			throw new InvalidViewTypeException(e);
		}
	}

}
