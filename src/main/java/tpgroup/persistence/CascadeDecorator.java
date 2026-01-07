package tpgroup.persistence;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import tpgroup.model.exception.RecordNotFoundException;

public class CascadeDecorator<T> implements DAO<T>{
	private final DAO<T> decorated;
	private final Set<Cascade<T, ?>> cascadePolicies;

	public CascadeDecorator(DAO<T> decorated) {
		super();
		this.decorated = decorated;
		cascadePolicies = new HashSet<>();
	}

	public CascadeDecorator<T> addCascadePolicy(Cascade<T, ?> cascadePolicy){
		cascadePolicies.add(cascadePolicy);
		return this;
	}

	@Override
	public List<T> getAll() {
		List<T> res = decorated.getAll();
		cascadePolicies.forEach(policy -> policy.propagateGetAll(res));
		return res;
	}

	@Override
	public List<T> getFiltered(Predicate<T> filter) {
		List<T> res = decorated.getFiltered(filter);
		cascadePolicies.forEach(policy -> policy.propagateGetAll(res));
		//getAll va bene in quanto lavora su una lista che è un subset dell'originale
		return res;
	}

	@Override
	public T get(T obj) throws RecordNotFoundException {
		T res = decorated.get(obj);
		cascadePolicies.forEach(policy -> policy.propagateGet(res));
		return res;
	}

	@Override
	public void save(T obj) {
		decorated.save(obj);
		cascadePolicies.forEach(policy -> policy.propagateUpdate(obj));
	}

	@Override
	public boolean add(T obj) {
		Boolean res = decorated.add(obj);
		if(res){
			for(Cascade<T, ?> policy : cascadePolicies){
				res = res && policy.propagateAdd(obj);
			}
		}
		return res;
	}

	@Override
	public void delete(T obj) throws RecordNotFoundException {
		decorated.delete(obj);
		cascadePolicies.forEach(policy -> policy.propagateDelete(obj));
	}

}
