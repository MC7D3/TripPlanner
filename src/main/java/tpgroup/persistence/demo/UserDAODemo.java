package tpgroup.persistence.demo;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import tpgroup.model.domain.User;
import tpgroup.model.exception.RecordNotFoundException;
import tpgroup.persistence.DAO;

public class UserDAODemo implements DAO<User>{
	private static UserDAODemo instance;

	private final Set<User> userList = new HashSet<>();

	private UserDAODemo() {
		super();
	}

	@Override
	public User get(User obj) throws RecordNotFoundException {
		try {
			return userList.stream().filter((elem) -> elem.equals(obj)).findFirst().orElseThrow();
		} catch (NoSuchElementException e) {
			throw new RecordNotFoundException(e);
		}
	}

	@Override
	public void save(User obj) {
		if(!userList.add(obj)){
			userList.remove(obj);
			userList.add(obj);
		}
	}

	@Override
	public void delete(User obj) throws RecordNotFoundException{
		if(!userList.remove(obj)) throw new RecordNotFoundException();
	}

	public static UserDAODemo getInstance(){
		if(instance == null) instance = new UserDAODemo();
		return instance;
	}

	@Override
	public boolean add(User obj) {
		return userList.add(obj);
	}
	
}
