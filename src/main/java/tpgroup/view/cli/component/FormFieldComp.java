package tpgroup.view.cli.component;

import tpgroup.model.exception.FormFieldIOException;

public interface FormFieldComp<T> {
	public T get() throws FormFieldIOException;
}
