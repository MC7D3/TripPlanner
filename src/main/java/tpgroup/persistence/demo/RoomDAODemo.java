package tpgroup.persistence.demo;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Predicate;

import tpgroup.model.domain.Room;
import tpgroup.model.exception.RecordNotFoundException;
import tpgroup.persistence.DAO;

public class RoomDAODemo implements DAO<Room> {

	private final Set<Room> roomList = new HashSet<>();

	public RoomDAODemo() {
		super();
	}

	@Override
	public Room get(Room obj) throws RecordNotFoundException {
		try {
			return roomList.stream().filter((elem) -> elem.equals(obj)).findFirst().orElseThrow();
		} catch (NoSuchElementException e) {
			throw new RecordNotFoundException(e);
		}
	}

	@Override
	public void save(Room obj) {
		if (!roomList.add(obj)) {
			roomList.remove(obj);
			roomList.add(obj);
		}
	}

	@Override
	public void delete(Room obj) throws RecordNotFoundException {
		if (!roomList.remove(obj))
			throw new RecordNotFoundException();
	}

	@Override
	public boolean add(Room obj) {
		return roomList.add(obj);
	}

	@Override
	public List<Room> getAll() {
		return roomList.stream().toList();
	}

	@Override
	public List<Room> getFiltered(Predicate<Room> filter) {
		return roomList.stream().filter(filter).toList();
	}

}
