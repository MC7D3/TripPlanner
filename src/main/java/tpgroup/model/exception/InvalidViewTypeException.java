package tpgroup.model.exception;

public class InvalidViewTypeException extends Exception{
	private static final String msg = "The view type provided is invalid";
	public InvalidViewTypeException(){
		super(msg);
	}

	public InvalidViewTypeException(Throwable cause){
		super(msg, cause);
	}
	
}
