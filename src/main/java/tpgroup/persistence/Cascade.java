package tpgroup.persistence;

import java.util.List;

public abstract class Cascade<T1, T2> {
	private final DAO<T2> to;

	protected Cascade(DAO<T2> to) {
		this.to = to;
	}

	public abstract List<T1> propagateGetAll(List<T1> toGet);

	public abstract T1 propagateGet(T1 toGet);

	public abstract boolean propagateAdd(T1 toAdd);

	public abstract boolean propagateUpdate(T1 toUpdate);

	public abstract boolean propagateDelete(T1 toDelete);

	public DAO<T2> getTo() {
		return to;
	}

}
