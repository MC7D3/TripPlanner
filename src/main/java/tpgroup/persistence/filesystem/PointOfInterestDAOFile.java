package tpgroup.persistence.filesystem;

import java.util.List;
import java.util.function.Predicate;

import com.google.gson.GsonBuilder;

import tpgroup.model.domain.PointOfInterest;
import tpgroup.model.exception.RecordNotFoundException;

public class PointOfInterestDAOFile extends FileDAO<PointOfInterest>{ 

    public PointOfInterestDAOFile(String baseDir) {
		super(baseDir, new GsonBuilder());
    }

	@Override
	public List<PointOfInterest> getAll() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getAll'");
	}

	@Override
	public List<PointOfInterest> getFiltered(Predicate<PointOfInterest> filter) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getFiltered'");
	}

	@Override
	public PointOfInterest get(PointOfInterest obj) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'get'");
	}

	@Override
	public void save(PointOfInterest obj) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'save'");
	}

	@Override
	public boolean add(PointOfInterest obj) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'add'");
	}

	@Override
	public void delete(PointOfInterest obj) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'delete'");
	}

}
