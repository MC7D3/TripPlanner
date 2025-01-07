package tpgroup.persistence;
 
import java.util.List;

import tpgroup.model.exception.RecordNotFoundException;

public interface DAO<T> {

	public List<T> getAll();

	public T get(T obj) throws RecordNotFoundException;

	public void save(T obj);

	public boolean add(T obj);

	public void delete(T obj) throws RecordNotFoundException;
}
