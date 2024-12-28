package tpgroup.model.domain;

public class RoomAdmin extends RoomMember{
	public RoomAdmin(String email, String password) {
		super(email, password);
	}
	public RoomAdmin(User user) {
		super(user);
	}

	@Override
	public String toString() {
		return "RoomAdmin [" + this.email + ", " + this.password + "]";
	}
}

