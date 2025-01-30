package tpgroup.controller;

import java.util.Optional;

import tpgroup.model.PwdBean;
import tpgroup.model.domain.User;
import tpgroup.model.exception.RecordNotFoundException;
import tpgroup.persistence.DAO;
import tpgroup.persistence.factory.DAOFactory;

public class OptionsController {

	private OptionsController(){
		super();
	}

	public static void updateCredentials(User user, Optional<PwdBean> newPassword){
		try{
			DAO<User> userDao = DAOFactory.getInstance().getDAO(User.class);
			User updatedCred = new User(user.getEmail(), 
				newPassword.isEmpty()? user.getPassword(): newPassword.get().getPassword());
			userDao.delete(user);
			userDao.add(updatedCred);
		}catch(RecordNotFoundException e){
			throw new IllegalStateException(e);
		}
	}

	public static void deleteAccount(User user){
		DAO<User> userDao = DAOFactory.getInstance().getDAO(User.class);
		try {
			//TODO: cascade fatto solo da testare 
			userDao.delete(user);
		} catch (RecordNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}
	
}
