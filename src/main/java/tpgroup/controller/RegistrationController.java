package tpgroup.controller;

import tpgroup.model.RegistrationCredBean;
import tpgroup.model.domain.User;
import tpgroup.persistence.DAO;
import tpgroup.persistence.factory.DAOFactory;

public class RegistrationController {

	private RegistrationController(){
		super();
	}
	
	public static boolean executeRegistration(RegistrationCredBean credentials){
		DAO<User> userDao = DAOFactory.getInstance().getDAO(User.class);
		return userDao.add(new User(credentials.getEmail(), credentials.getPassword()));
	}
}
