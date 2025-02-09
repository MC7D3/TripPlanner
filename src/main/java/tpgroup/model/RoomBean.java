package tpgroup.model;

import java.util.regex.Pattern;

import tpgroup.controller.POIController;
import tpgroup.model.exception.InvalidBeanParamException;

public class RoomBean {
	private static final String NAME_REGEX = "^[a-zA-Z0-9_-]+$";
	private final String name;
	private final String destination;

	public RoomBean(String name, String destination) throws InvalidBeanParamException{	 
		if(!Pattern.matches(NAME_REGEX, name)){
			throw new InvalidBeanParamException("name");
		}
		this.name = name;
		if(POIController.isValidDestination(destination)){
			this.destination = destination;
		}else{
			throw new InvalidBeanParamException("destination");
		}
	}

	public String getName() {
		return name;
	}

	public String getDestination() {
		return destination;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
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
		RoomBean other = (RoomBean) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		return true;
	}
	
}
