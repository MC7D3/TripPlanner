package tpgroup.controller;

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
		System.out.println(newPassword.getPassword());
		DAO<User> userDao = DAOFactory.getInstance().getDAO(User.class);
		User updatedCred = new User(Session.getInstance().getLogged().getEmail(),
				newPassword.getPassword());
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
