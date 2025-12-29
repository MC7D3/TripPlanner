package tpgroup.model;

import java.util.Objects;
import java.util.regex.Pattern;

import tpgroup.controller.POIController;
import tpgroup.model.domain.Room;
import tpgroup.model.exception.InvalidBeanParamException;

public class RoomBean {
	private static final String NAME_REGEX = "^[a-zA-Z0-9_-]+$";
	private static final String CODE_REG = "^[a-z0-9]+(?:-[a-z0-9]+)*+-\\d{1,5}$";
	private final Room room;

	public RoomBean (String code) throws InvalidBeanParamException {
		if(!Pattern.matches(CODE_REG, code)){
			throw new InvalidBeanParamException("code");
		}
		this.room = new Room(code);
	}

	public RoomBean(String name, String country, String city) throws InvalidBeanParamException{	 
		if(!Pattern.matches(NAME_REGEX, name)){
			throw new InvalidBeanParamException("name");
		}
		if(POIController.isValidCountry(country) && POIController.isValidCity(city)){
		}else{
			throw new InvalidBeanParamException("country");
		}
		room = new Room(name, null, null, country, city);
	}

	public RoomBean(Room room){
		this.room = room;
	}

	public String getName() {
		return room.getName();
	}

	public String getCountry() {
		return room.getTrip().getCountry();
	}

	public String getCity() {
		return room.getTrip().getMainCity();
	}

	public String getCode() {
		return room.getCode();
	}

	public Room getRoom() {
		return room;
	}

	@Override
	public int hashCode() {
		return Objects.hash(room);
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
		return Objects.equals(room, other.room);
	}
	
}
