package tpgroup.model;

import java.util.regex.Pattern;

import tpgroup.model.exception.InvalidBeanParamException;

public class RoomCodeBean {
	private static final String CODE_REG = "^[a-z0-9]+(?:-[a-z0-9]+)*-\\d{1,5}$";
	private final String code;

	public RoomCodeBean(String code) throws InvalidBeanParamException {
		if(!Pattern.matches(CODE_REG, code)){
			throw new InvalidBeanParamException("code");
		}
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
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
		RoomCodeBean other = (RoomCodeBean) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

}
