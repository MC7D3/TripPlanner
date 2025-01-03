package tpgroup.controller;

import tpgroup.model.RegistrationCredBean;
import tpgroup.model.domain.User;
import tpgroup.persistence.DAO;
import tpgroup.persistence.factory.DAOFactory;

public class RegistrationController {
	
	public static Boolean executeRegistration(RegistrationCredBean credentials){
		DAO<User> userDao = DAOFactory.getInstance().getDAO(User.class);
		if(!userDao.add(new User(credentials.getEmail(), credentials.getPassword()))){
			return false;
		}
		return true; 
	}
}
