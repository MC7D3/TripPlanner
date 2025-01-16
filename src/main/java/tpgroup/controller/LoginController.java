package tpgroup.controller;

import org.springframework.security.crypto.bcrypt.BCrypt;

import tpgroup.model.EmailBean;
import tpgroup.model.PwdBean;
import tpgroup.persistence.factory.DAOFactory;
import tpgroup.model.domain.User;
import tpgroup.persistence.DAO;
import tpgroup.model.exception.RecordNotFoundException;
import tpgroup.model.Session;

public class LoginController {
	private LoginController(){
		super();
	}

	public static Boolean validateCredentials(EmailBean email, PwdBean password){
		try{
			DAO<User> userDao = DAOFactory.getInstance().getDAO(User.class);
			User fullCred = userDao.get(new User(email.getEmail(), null));
			boolean res = BCrypt.checkpw(password.getPassword(), fullCred.getPassword());
			if(res){
				Session.getInstance().setLogged(fullCred);
			}
			return res;
		}catch(RecordNotFoundException e){
			System.out.println("record not found");
			return false;
		}
	}

	public static void logout(){
		Session.getInstance().resetSession();
	}
}
