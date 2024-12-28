package tpgroup.model.domain;

public class RoomMember extends User{
	public RoomMember(String email, String password) {
		super(email, password);
	}

	public RoomMember(User user) {
		super(user.email, user.password);
	}

	@Override
	public String toString() {
		return "RoomMember [" + this.email + ", " + this.password + "]";
	}
}

