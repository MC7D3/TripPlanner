package tpgroup.controller;

import org.springframework.security.crypto.bcrypt.BCrypt;

import tpgroup.model.Session;
import tpgroup.model.bean.UserBean;
import tpgroup.model.domain.User;
import tpgroup.model.exception.InvalidBeanParamException;
import tpgroup.model.exception.RecordNotFoundException;
import tpgroup.persistence.DAO;
import tpgroup.persistence.factory.DAOFactory;

public class AuthController {
	private AuthController() {
		super();
	}

	public static boolean validateCredentials(UserBean user) {
		try {
			DAO<User> userDao = DAOFactory.getInstance().getDAO(User.class);
			User fullCred = userDao.get(new User(user.getEmail(), null));
			boolean res = BCrypt.checkpw(user.getPassword(), fullCred.getPassword());
			if (res) {
				Session.getInstance().setLogged(fullCred);
			}
			return res;
		} catch (RecordNotFoundException e) {
			return false;
		}
	}

	public static boolean executeRegistration(UserBean user) throws InvalidBeanParamException{
		DAO<User> userDao = DAOFactory.getInstance().getDAO(User.class);
		String encPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
		if(!BCrypt.checkpw(user.getConfPassword(), encPassword)){
			throw new InvalidBeanParamException("confPassword", "the passwords do not match");
		}
		User newUser = new User(user.getEmail(), user.getPassword());
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
