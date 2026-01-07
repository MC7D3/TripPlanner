package tpgroup.model.bean;

import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import tpgroup.model.domain.Room;
import tpgroup.model.exception.InvalidBeanParamException;

public class RoomBean {
	private static final String NAME_REGEX = "^[a-zA-Z0-9_-]+$";
	private static final String CODE_REG = "^[a-z0-9]+(?:-[a-z0-9]+)*+-\\d{1,5}$";
	private final String code;
	private final String name;
	private final UserBean admin;
	private final Set<UserBean> members;
	private final TripBean trip;

	public RoomBean (String code) throws InvalidBeanParamException {
		if(!Pattern.matches(CODE_REG, code)){
			throw new InvalidBeanParamException("code");
		}
		this.code = code;
		this.name = null;
		this.admin = null;
		this.members = null;
		this.trip = null;
	}

	public RoomBean(String name, String country, String city) throws InvalidBeanParamException{	 
		if(!Pattern.matches(NAME_REGEX, name)){
			throw new InvalidBeanParamException("name");
		}
		this.code = null;
		this.name = name;
		this.admin = null;
		this.members = null;
		this.trip = new TripBean(country, city);
	}

	public RoomBean(Room room){
		this.code = room.getCode();
		this.name = room.getName();
		this.admin = new UserBean(room.getAdmin());
		this.members = room.getMembers().stream().map(member -> new UserBean(member)).collect(Collectors.toSet());
		this.trip = new TripBean(room.getTrip());
	}

	public String getCode(){
		return code;
	}

	public String getName() {
		return name;
	}

	public UserBean getAdmin() {
		return admin;
	}

	public Set<UserBean> getMembers() {
		return members;
	}

	public TripBean getTrip() {
		return trip;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		RoomBean other = (RoomBean) obj;
		return Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "RoomBean{code=" + code + ", name=" + name + ", admin=" + admin + ", members=" + members + ", trip="
				+ trip + "}";
	}
	
}
