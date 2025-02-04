package tpgroup.model.exception;

public class RoomGenConflictException extends Exception{
	private static final String MSG = "unable to create a room";

	public RoomGenConflictException(Throwable cause) {
		super(MSG, cause);
	}

	public RoomGenConflictException() {
		super(MSG);
	}

}

