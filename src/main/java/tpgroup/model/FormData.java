package tpgroup.model;

import java.util.HashMap;
import java.util.Map;

public class FormData {
	private final Map<String, Object> data;

	public FormData(){
		data = new HashMap<>();
	}

	public void put(String key, Object value){
		data.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String key){
		return (T) data.get(key);
	}

	public void remove(String key){
		data.remove(key);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
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
		FormData other = (FormData) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		return true;
	}
}
