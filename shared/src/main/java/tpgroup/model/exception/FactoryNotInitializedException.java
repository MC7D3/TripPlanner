package tpgroup.model.exception;

public class FactoryNotInitializedException extends RuntimeException{
	public FactoryNotInitializedException() {
		super("Factory not initialized");
	}
}
