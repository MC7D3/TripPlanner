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

import tpgroup.model.domain.PointOfInterest;
import tpgroup.model.exception.InvalidPathException;
import tpgroup.model.exception.RecordNotFoundException;
import tpgroup.persistence.DAO;
import tpgroup.persistence.gson.GsonFactory;

public class PointOfInterestDAOFile implements DAO<PointOfInterest> {

	private final File file;
	private final Gson gson;
	private final Type collectionType;

	public PointOfInterestDAOFile(String filePath) {
		this.file = new File(filePath);

		File parentDir = this.file.getParentFile();
		if (parentDir != null && !parentDir.exists()) {
			parentDir.mkdirs();
		}

		this.gson = GsonFactory.createDefaultBuilder().create();
		this.collectionType = new TypeToken<List<PointOfInterest>>() {
		}.getType();

		if (!file.exists()) {
			try (Writer wr = new FileWriter(file)) {
				wr.write("[]");
			} catch (IOException e) {
				throw new InvalidPathException();
			}
		}
	}

	private List<PointOfInterest> readAll() {
		try (Reader reader = new FileReader(file)) {
			List<PointOfInterest> list = gson.fromJson(reader, collectionType);
			return list != null ? list : new ArrayList<>();
		} catch (IOException e) {
			throw new InvalidPathException();
		}
	}

	private void writeAll(List<PointOfInterest> list) {
		try (Writer writer = new FileWriter(file)) {
			gson.toJson(list, collectionType, writer);
		} catch (IOException e) {
			throw new InvalidPathException();
		}
	}

	@Override
	public List<PointOfInterest> getAll() {
		return readAll();
	}

	@Override
	public List<PointOfInterest> getFiltered(Predicate<PointOfInterest> filter) {
		return readAll().stream().filter(filter).toList();
	}

	@Override
	public PointOfInterest get(PointOfInterest obj) throws RecordNotFoundException {
		List<PointOfInterest> pois = readAll();
		for (PointOfInterest poi : pois) {
			if (poi.equals(obj)) {
				return poi;
			}
		}
		throw new RecordNotFoundException();
	}

	@Override
	public void save(PointOfInterest obj) {
		List<PointOfInterest> pois = readAll();
		boolean updated = false;
		for (int i = 0; i < pois.size(); i++) {
			if (pois.get(i).equals(obj)) {
				pois.set(i, obj);
				updated = true;
				break;
			}
		}
		if (!updated) {
			pois.add(obj);
		}
		writeAll(pois);
	}

	@Override
	public boolean add(PointOfInterest obj) {
		List<PointOfInterest> pois = readAll();
		if (pois.contains(obj)) {
			return false;
		}
		pois.add(obj);
		writeAll(pois);
		return true;
	}

	@Override
	public void delete(PointOfInterest obj) throws RecordNotFoundException {
		List<PointOfInterest> pois = readAll();
		boolean removed = pois.remove(obj);
		if (!removed) {
			throw new RecordNotFoundException();
		}
		writeAll(pois);
	}
}
