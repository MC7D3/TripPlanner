package tpgroup.persistence.filesystem;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import tpgroup.model.EventsGraph;
import tpgroup.model.domain.Room;
import tpgroup.model.exception.RecordNotFoundException;
import tpgroup.persistence.DAO;
import tpgroup.persistence.EventsGraphJSONTypeAdapter;
import tpgroup.persistence.LocalDateTimeJSONTypeAdaper;

public class RoomDAOFile implements DAO<Room> {
	private final File file;
	private final Gson gson;
	private final Type collectionType = new TypeToken<List<Room>>() {
	}.getType();

	public RoomDAOFile(String filePath) {
		this.file = new File(filePath);
		GsonBuilder bd = new GsonBuilder()
				.registerTypeAdapter(EventsGraph.class, new EventsGraphJSONTypeAdapter())
				.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeJSONTypeAdaper());
		this.gson = bd.create();
		if (!file.exists()) {
			try (Writer writer = new FileWriter(file)) {
				gson.toJson(new ArrayList<Room>(), collectionType, writer);
			} catch (IOException e) {
				throw new RuntimeException("failed to create the file", e);
			}
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
		return readAll().stream().filter(filter).collect(Collectors.toList());
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
