package tpgroup.persistence.demo;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Predicate;

import tpgroup.model.domain.Room;
import tpgroup.model.domain.User;
import tpgroup.model.exception.RecordNotFoundException;
import tpgroup.persistence.Cascade;
import tpgroup.persistence.DAO;
public class UserDAODemo implements DAO<User>{

	private final Set<User> userList = new HashSet<>();
	private Cascade<User, Room> cascadePolicy;

	public UserDAODemo() {
		super();
	}
	


	public Set<User> getUserList() {
		return userList;
	}

	public void setCascadePolicy(Cascade<User, Room> cascadePolicy) {
		this.cascadePolicy = cascadePolicy;
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
		cascadePolicy.propagateUpdate(obj);
	}

	@Override
	public void delete(User obj) throws RecordNotFoundException{
		if(!userList.remove(obj)) throw new RecordNotFoundException();
		cascadePolicy.propagateDelete(obj);
	}

	@Override
	public boolean add(User obj) {
		return userList.add(obj);
	}

	@Override
	public List<User> getAll() {
		return userList.stream().toList();
	}
	
	@Override
	public List<User> getFiltered(Predicate<User> filter) {
		return userList.stream().filter(filter).toList();
	}
}
