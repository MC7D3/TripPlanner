package tpgroup.controller;

import org.springframework.security.crypto.bcrypt.BCrypt;

import tpgroup.model.Session;
import tpgroup.model.bean.UserBean;
import tpgroup.model.domain.User;
import tpgroup.model.exception.RecordNotFoundException;
import tpgroup.persistence.DAO;
import tpgroup.persistence.factory.DAOFactory;

public class OptionsController {

	private OptionsController() {
		super();
	}

	public static void updatePassword(UserBean newPassword) {
		DAO<User> userDao = DAOFactory.getInstance().getDAO(User.class);
		String encPassword = BCrypt.hashpw(newPassword.getPassword(), BCrypt.gensalt());
		User updatedCred = new User(Session.getInstance().getLogged().getEmail(), encPassword);
		userDao.save(updatedCred);
		Session.getInstance().setLogged(updatedCred);
	}

	public static void deleteAccount() {
		DAO<User> userDao = DAOFactory.getInstance().getDAO(User.class);
		try {
			userDao.delete(Session.getInstance().getLogged());
			Session.resetSession();
		} catch (RecordNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}

}
