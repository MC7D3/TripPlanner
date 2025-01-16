package tpgroup.model;
import tpgroup.model.domain.User;

public class Session {
	private static Session instance;

	private User logged;

	private Session(){}

	public User getLogged() {
		return logged;
	}

	public void setLogged(User logged) {
		this.logged = logged;
	}

	public static Session getInstance(){
		if(instance == null){
			instance = new Session();
		}
		return instance;
	}

	public void resetSession(){
		instance = new Session();
	}

}
