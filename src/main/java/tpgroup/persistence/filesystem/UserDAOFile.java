package tpgroup.persistence.filesystem;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import tpgroup.model.domain.User;
import tpgroup.model.exception.InvalidPathException;
import tpgroup.model.exception.RecordNotFoundException;
import tpgroup.persistence.DAO;
import tpgroup.persistence.gson.GsonFactory;

public class UserDAOFile implements DAO<User>{
	private final File file;
	private final Gson gson;
	private final Type collectionType;

	public UserDAOFile(String filePath) {
		this.file = new File(filePath);

		File parentDir = this.file.getParentFile();
		if (parentDir != null && !parentDir.exists()) {
			parentDir.mkdirs();
		}

		this.gson = GsonFactory.createDefaultBuilder().create();
		this.collectionType = new TypeToken<List<User>>() {}.getType();

		if (!file.exists()) {
			try (Writer wr = new FileWriter(file)) {
				wr.write("[]");
			} catch (IOException e) {
				throw new InvalidPathException();
			}
		}
	}

	private List<User> readAll() {
		try (Reader reader = new FileReader(file)) {
			List<User> list = gson.fromJson(reader, collectionType);
			return list != null ? list : new ArrayList<>();
		} catch (IOException e) {
			throw new InvalidPathException();
		}
	}

	private void writeAll(List<User> list) {
		try (Writer writer = new FileWriter(file)) {
			gson.toJson(list, collectionType, writer);
		} catch (IOException e) {
			throw new InvalidPathException();
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
