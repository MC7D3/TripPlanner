package tpgroup.persistence.filesystem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;

import com.google.gson.GsonBuilder;

import tpgroup.model.EventsGraph;
import tpgroup.model.domain.Room;
import tpgroup.model.exception.RecordNotFoundException;
import tpgroup.persistence.EventsGraphJSONTypeAdapter;
import tpgroup.persistence.LocalDateTimeJSONTypeAdaper;

public class RoomDAOFile extends FileDAO<Room> {

	public RoomDAOFile(String filePath) {
		super(filePath, new GsonBuilder().registerTypeAdapter(EventsGraph.class, new EventsGraphJSONTypeAdapter()).registerTypeAdapter(LocalDateTime.class, new LocalDateTimeJSONTypeAdaper()));
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
