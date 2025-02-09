package tpgroup.model;
import tpgroup.model.domain.Room;
import tpgroup.model.domain.User;

public class Session {
	private static Session instance;

	private User logged;
	private Room enteredRoom;

	private Session(){}

	public User getLogged() {
		return logged;
	}

	public void setLogged(User logged) {
		this.logged = logged;
	}

	public Room getEnteredRoom() {
		return enteredRoom;
	}

	public void setEnteredRoom(Room enteredRoom) {
		this.enteredRoom = enteredRoom;
	}

	public static Session getInstance(){
		if(instance == null){
			instance = new Session();
		}
		return instance;
	}

	public static void resetSession(){
		instance = new Session();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((logged == null) ? 0 : logged.hashCode());
		result = prime * result + ((enteredRoom == null) ? 0 : enteredRoom.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Session other = (Session) obj;
		if (logged == null) {
			if (other.logged != null)
				return false;
		} else if (!logged.equals(other.logged))
			return false;
		if (enteredRoom == null) {
			if (other.enteredRoom != null)
				return false;
		} else if (!enteredRoom.equals(other.enteredRoom))
			return false;
		return true;
	}

}
