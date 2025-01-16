package tpgroup.model.domain;

public class RoomMember extends User{
	public RoomMember(String email, String password) {
		super(email, password);
	}

	public RoomMember(User user) {
		super(user.getEmail(), user.getPassword());
	}

	@Override
	public String toString() {
		return "RoomMember [email=" + this.getEmail() + ", password=" + this.getPassword() + "]";
	}



}

