package tpgroup.controller;

import java.util.Optional;

import tpgroup.model.PwdBean;
import tpgroup.model.Session;
import tpgroup.model.domain.User;
import tpgroup.model.exception.RecordNotFoundException;
import tpgroup.persistence.DAO;
import tpgroup.persistence.factory.DAOFactory;

public class OptionsController {

	public static void updateCredentials(Optional<PwdBean> newPassword){
		try{
			DAO<User> userDao = DAOFactory.getInstance().getDAO(User.class);
			User curCred = Session.getInstance().getLogged();
			User updatedCred = new User(curCred.getEmail(), 
				newPassword.isEmpty()? curCred.getPassword(): newPassword.get().getPassword());
			userDao.delete(curCred);
			userDao.add(updatedCred);
		}catch(RecordNotFoundException e){
			throw new IllegalStateException(e);
		}
	}

	public static void deleteLoggedAccount(){
		DAO<User> userDao = DAOFactory.getInstance().getDAO(User.class);
		try {
			//TODO: cascade fatto solo da testare 
			userDao.delete(Session.getInstance().getLogged());
			Session.getInstance().resetSession();
		} catch (RecordNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}
	
}
