package tpgroup.persistence;

import java.util.List;
import java.util.function.Predicate;

import tpgroup.model.exception.RecordNotFoundException;

public abstract class DAODecorator<T> implements DAO<T>{
	
	private DAO<T> decorated;

	protected DAODecorator(DAO<T> decorated){
		this.decorated = decorated;
	}

	@Override
	public boolean add(T obj) {
		return decorated.add(obj);
	}

	@Override
	public void delete(T obj) throws RecordNotFoundException {
		decorated.delete(obj);
	}

	@Override
	public T get(T obj) throws RecordNotFoundException {
		return decorated.get(obj);
	}

	@Override
	public List<T> getAll() {
		return decorated.getAll();
	}

	@Override
	public List<T> getFiltered(Predicate<T> filter) {
		return decorated.getFiltered(filter);
	}

	@Override
	public void save(T obj) {
		decorated.save(obj);
		
	}



}
