package tpgroup.persistence;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import tpgroup.model.exception.RecordNotFoundException;

public class CacheDecorator<T> implements DAO<T> {
	private class CachedElement {
		private final T elem;
		private int ttl;

		public CachedElement(T elem) {
			this.elem = elem;
			this.ttl = 5;
		}

		public T getElem() {
			T ret = this.elem;
			ttl--;
			if (ttl == 0) {
				notifyDeath(this);
			}
			return ret;
		}

		public int getTtl() {
			return ttl;
		}

		public void resetTtl() {
			this.ttl = 5;
		}

		private void notifyDeath(CachedElement elem) {
			CacheDecorator.this.cache.remove(elem);
		}

	}

	private final DAO<T> decorated;
	private final Set<CachedElement> cache;

	public CacheDecorator(DAO<T> decorated) {
		super();
		this.decorated = decorated;
		cache = new HashSet<>();
	}

	@Override
	public List<T> getAll() {
		List<T> all = decorated.getAll();
		for(CachedElement cached : cache){
			if(all.contains(cached.getElem())){
				cached.resetTtl();
			}
		}
		cache.addAll(all.stream().map(item -> new CachedElement(item)).toList());
		return all;
	}

	@Override
	public List<T> getFiltered(Predicate<T> filter) {
		List<T> items = new ArrayList<>();
		for (CachedElement item : cache) {
			if (filter.test(item.getElem())) {
				item.resetTtl();
				items.add(item.getElem());
			} else {
				item.getElem();
			}
		}
		List<T> notCached = decorated.getFiltered(item -> !cache.contains(item) && filter.test(item));
		cache.addAll(notCached.stream().map(item -> new CachedElement(item)).toList());
		items.addAll(notCached);
		return items;
	}

	@Override
	public T get(T obj) throws RecordNotFoundException {
		if (cache.contains(obj)) {
			for (CachedElement item : cache) {
				if (item.getElem().equals(obj)) {
					item.resetTtl();
					return item.getElem();
				} else {
					if (item.getTtl() == 0) {
						cache.remove(item);
					}
				}
			}
		}
		T item = decorated.get(obj);
		cache.add(new CachedElement(item));
		return item;
	}

	@Override
	public void save(T obj) {
		decorated.save(obj);
		cache.remove(obj);
		cache.add(new CachedElement(obj));
	}

	@Override
	public boolean add(T obj) {
		boolean res = decorated.add(obj);
		if (res) {
			cache.add(new CachedElement(obj));
		}
		return res;
	}

	@Override
	public void delete(T obj) throws RecordNotFoundException {
		decorated.delete(obj);
		cache.remove(obj);
	}

}
