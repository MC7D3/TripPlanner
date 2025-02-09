package tpgroup.persistence.demo;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Predicate;

import tpgroup.model.domain.PointOfInterest;
import tpgroup.model.exception.RecordNotFoundException;
import tpgroup.persistence.DAO;

public class POIDAODemo implements DAO<PointOfInterest>{

	private static final Set<PointOfInterest> poiSet = new HashSet<>();

	public POIDAODemo() {
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
		throw new UnsupportedOperationException("no 'save' required");
	}

	@Override
	public boolean add(PointOfInterest obj) {
		throw new UnsupportedOperationException("no 'add' required");
	}

	@Override
	public void delete(PointOfInterest obj) throws RecordNotFoundException {
		throw new UnsupportedOperationException("no 'delete' required");
	}

}
