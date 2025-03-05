package tpgroup.persistence.filesystem;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.gson.GsonBuilder;

import tpgroup.model.domain.User;
import tpgroup.model.exception.RecordNotFoundException;

public class UserDAOFile extends FileDAO<User>{


	public UserDAOFile(String filePath) {
		super(filePath, new GsonBuilder());
		if (!file.exists()) {
			try (Writer writer = new FileWriter(file)) {
				gson.toJson(new ArrayList<User>(), collectionType, writer);
			} catch (IOException e) {
				throw new RuntimeException("failed to create or recover the file", e);
			}
		}
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
		boolean updated = false;
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).equals(user)) {
				users.set(i, user);
				updated = true;
				break;
			}
		}
		if (!updated) {
			users.add(user);
		}
		writeAll(users);
	}

	@Override
	public List<User> getAll() {
		return readAll();
	}

	@Override
	public List<User> getFiltered(Predicate<User> filter) {
		return readAll().stream().filter(filter).collect(Collectors.toList());
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
