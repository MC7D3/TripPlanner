package tpgroup.model;

import java.util.Objects;

public class User {
	String email;
	String password;

	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public int hashCode() {
	    return Objects.hash(this.email, this.password);
	}

	@Override
	public boolean equals(Object obj) {
	    if (obj == null || getClass() != obj.getClass()) {
	        return false;
	    }

	    User other = (User) obj;
	    return Objects.equals(this.email, other.email) && Objects.equals(this.password, other.password);
	}
	
}
