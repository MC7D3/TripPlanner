package tpgroup.persistence.factory;

import tpgroup.persistence.PersistenceType;
import tpgroup.model.exception.FactoryNotInitializedException;
import tpgroup.model.exception.InvalidPersistenceTypeException;
import tpgroup.persistence.DAO;

public abstract class DAOFactory {
	private static DAOFactory instance;

	protected DAOFactory() {
		super();
	}

	public static void initDAOFactory(PersistenceType type) throws InvalidPersistenceTypeException{
		switch(type){
			case DEMO:
				instance = new DemoDAOFactory();
				break;
			case MYSQLDB:
				instance = new DBDAOFactory();
				break;
			case FILE:
				instance = new FileDAOFactory();

			default:
				throw new InvalidPersistenceTypeException();
		}

	}

	public static DAOFactory getInstance() throws FactoryNotInitializedException{
		if(instance == null){
			throw new FactoryNotInitializedException();
		}
		return instance;
	}

	public abstract <T> DAO<T> getDAO(Class<T> of);
	
}
