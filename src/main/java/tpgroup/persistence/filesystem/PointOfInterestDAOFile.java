package tpgroup.persistence.filesystem;

import java.util.List;
import java.util.function.Predicate;

import com.google.gson.GsonBuilder;

import tpgroup.model.domain.PointOfInterest;
import tpgroup.model.exception.RecordNotFoundException;

public class PointOfInterestDAOFile extends FileDAO<PointOfInterest>{ 

    public PointOfInterestDAOFile(String filePath) {
		super(filePath, new GsonBuilder());
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
		for(PointOfInterest poi : pois){
			if(poi.equals(obj)){
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
		if(pois.contains(obj)){
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
		if(!removed){
			throw new RecordNotFoundException();
		}
		writeAll(pois);
	}

}
