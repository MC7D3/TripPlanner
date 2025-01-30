package tpgroup.controller;

import tpgroup.model.EmailBean;
import tpgroup.model.PwdBean;
import tpgroup.model.Session;
import tpgroup.model.domain.User;
import tpgroup.persistence.DAO;
import tpgroup.persistence.factory.DAOFactory;

public class RegistrationController {

	private RegistrationController(){
		super();
	}
	
	public static boolean executeRegistration(EmailBean email, PwdBean password) {
		DAO<User> userDao = DAOFactory.getInstance().getDAO(User.class);
		User newUser = new User(email.getEmail(), password.getPassword());
		boolean res = userDao.add(newUser);
		if(res){
			Session.getInstance().setLogged(newUser);
		}
		return res;
	}
}
