package tpgroup.controller;

import org.springframework.security.crypto.bcrypt.BCrypt;

import tpgroup.model.LoginCredBean;
import tpgroup.persistence.factory.DAOFactory;
import tpgroup.model.domain.User;
import tpgroup.persistence.DAO;
import tpgroup.model.exception.RecordNotFoundException;
import tpgroup.model.Session;

public class LoginController {
	private LoginController(){
		super();
	}

	public static Boolean validateCredentials(LoginCredBean credentials){
		try{
			DAO<User> userDao = DAOFactory.getInstance().getDAO(User.class);
			User fullCred = userDao.get(new User(credentials.getEmail(), null));
			boolean res;
			if(res = BCrypt.checkpw(credentials.getPassword(), fullCred.getPassword())){
				Session.getInstance().setLogged(fullCred);
			}
			return res;
		}catch(RecordNotFoundException e){
			System.out.println("record not found");
			return false;
		}
	}
}
