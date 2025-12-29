package tpgroup.persistence.demo;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Predicate;

import tpgroup.model.domain.PointOfInterest;
import tpgroup.model.exception.RecordNotFoundException;
import tpgroup.persistence.DAO;

public class PointOfInterestDAODemo implements DAO<PointOfInterest>{

	private static final Set<PointOfInterest> poiSet = new HashSet<>();

	public PointOfInterestDAODemo() {
		super();
	}

	@Override
	public List<PointOfInterest> getAll() {
		return poiSet.stream().toList();
	}

	@Override
	public List<PointOfInterest> getFiltered(Predicate<PointOfInterest> filter) {
		return poiSet.stream().filter(filter).toList();
	}

	@Override
	public PointOfInterest get(PointOfInterest obj) throws RecordNotFoundException {
		try{
			return poiSet.stream().filter(poi -> poi.equals(obj)).findFirst().orElseThrow();
		}catch(NoSuchElementException e) {
			throw new RecordNotFoundException(e);
		}
	}

	@Override
	public void save(PointOfInterest obj) {
		if(!poiSet.add(obj)){
			poiSet.remove(obj);
			poiSet.add(obj);
		}
	}

	@Override
	public boolean add(PointOfInterest obj) {
		return poiSet.add(obj);
	}

	@Override
	public void delete(PointOfInterest obj) throws RecordNotFoundException {
		throw new UnsupportedOperationException("no 'delete' required");
	}

}
