package tpgroup.model.exception;

public class FactoryNotInitializedException extends RuntimeException {
	private static final String MSG = "factory not initialized";

	public FactoryNotInitializedException() {
		super(MSG);
	}

	public FactoryNotInitializedException(Throwable cause) {
		super(MSG, cause);
	}
}
