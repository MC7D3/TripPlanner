package tpgroup.persistence.filesystem;

import java.util.List;
import java.util.function.Predicate;

import com.google.gson.GsonBuilder;

import tpgroup.model.domain.User;
import tpgroup.model.exception.RecordNotFoundException;

public class UserDAOFile extends FileDAO<User>{

	public UserDAOFile(String filePath) {
		super(filePath, new GsonBuilder());
	}


	@Override
	public User get(User user) throws RecordNotFoundException {
		List<User> users = readAll();
		for (User u : users) {
			if (u.equals(user)) {
				return u;
			}
		}
		throw new RecordNotFoundException();
	}

	@Override
	public void save(User user) {
		List<User> users = readAll();
		if (users.contains(user)) {
			users.remove(user);
		}
		users.add(user);
		writeAll(users);
	}

	@Override
	public List<User> getAll() {
		return readAll();
	}

	@Override
	public List<User> getFiltered(Predicate<User> filter) {
		return readAll().stream().filter(filter).toList();
	}

	@Override
	public boolean add(User user) {
		List<User> users = readAll();
		if (users.contains(user)) {
			return false;
		}
		users.add(user);
		writeAll(users);
		return true;
	}

	@Override
	public void delete(User user) throws RecordNotFoundException {
		List<User> users = readAll();
		boolean removed = users.remove(user);
		if (!removed) {
			throw new RecordNotFoundException();
		}
		writeAll(users);
	}

}
