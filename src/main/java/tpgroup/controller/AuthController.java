package tpgroup.controller;

import org.springframework.security.crypto.bcrypt.BCrypt;

import tpgroup.model.EmailBean;
import tpgroup.model.PwdBean;
import tpgroup.model.Session;
import tpgroup.model.domain.User;
import tpgroup.model.exception.RecordNotFoundException;
import tpgroup.persistence.DAO;
import tpgroup.persistence.factory.DAOFactory;

public class AuthController {
	private AuthController() {
		super();
	}

	public static boolean validateCredentials(EmailBean email, PwdBean password) {
		try {
			DAO<User> userDao = DAOFactory.getInstance().getDAO(User.class);
			User fullCred = userDao.get(new User(email.getEmail(), null));
			boolean res = BCrypt.checkpw(password.getPassword(), fullCred.getPassword());
			if (res) {
				Session.getInstance().setLogged(fullCred);
			}
			return res;
		} catch (RecordNotFoundException e) {
			return false;
		}
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

	public static void logout() {
		Session.resetSession();
	}
}
