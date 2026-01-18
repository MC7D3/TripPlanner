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

import tpgroup.model.domain.Room;
import tpgroup.model.exception.InvalidPathException;
import tpgroup.model.exception.RecordNotFoundException;
import tpgroup.persistence.DAO;
import tpgroup.persistence.gson.GsonFactory;

public class RoomDAOFile implements DAO<Room>{

	private final File file;
	private final Gson gson;
	private final Type collectionType;

	public RoomDAOFile(String filePath) {
		this.file = new File(filePath);

		File parentDir = this.file.getParentFile();
		if (parentDir != null && !parentDir.exists()) {
			parentDir.mkdirs();
		}

		this.gson = GsonFactory.createRoomBuilder().create();
		this.collectionType = new TypeToken<List<Room>>() {}.getType();

		if (!file.exists()) {
			try (Writer wr = new FileWriter(file)) {
				wr.write("[]");
			} catch (IOException e) {
				throw new InvalidPathException();
			}
		}
	}

	private List<Room> readAll() {
		try (Reader reader = new FileReader(file)) {
			List<Room> list = gson.fromJson(reader, collectionType);
			return list != null ? list : new ArrayList<>();
		} catch (IOException e) {
			throw new InvalidPathException();
		}
	}

	private void writeAll(List<Room> list) {
		try (Writer writer = new FileWriter(file)) {
			gson.toJson(list, collectionType, writer);
		} catch (IOException e) {
			throw new InvalidPathException();
		}
	}

	@Override
	public Room get(Room room) throws RecordNotFoundException {
		List<Room> rooms = readAll();
		for (Room r : rooms) {
			if (r.equals(room)) {
				return r;
			}
		}
		throw new RecordNotFoundException();
	}

	@Override
	public void save(Room room) {
		List<Room> rooms = readAll();
		boolean updated = false;
		for (int i = 0; i < rooms.size(); i++) {
			if (rooms.get(i).equals(room)) {
				rooms.set(i, room);
				updated = true;
				break;
			}
		}
		if (!updated) {
			rooms.add(room);
		}
		writeAll(rooms);
	}

	@Override
	public List<Room> getAll() {
		return readAll();
	}

	@Override
	public List<Room> getFiltered(Predicate<Room> filter) {
		return readAll().stream().filter(filter).toList();
	}

	@Override
	public boolean add(Room room) {
		List<Room> rooms = readAll();
		if (rooms.contains(room)) {
			return false;
		}
		rooms.add(room);
		writeAll(rooms);
		return true;
	}

	@Override
	public void delete(Room room) throws RecordNotFoundException {
		List<Room> rooms = readAll();
		boolean removed = rooms.remove(room);
		if (!removed) {
			throw new RecordNotFoundException();
		}
		writeAll(rooms);
	}
}
