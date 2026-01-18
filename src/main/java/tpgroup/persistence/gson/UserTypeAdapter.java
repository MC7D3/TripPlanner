package tpgroup.persistence.gson;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import tpgroup.model.domain.User;

public class UserTypeAdapter extends TypeAdapter<User> {

	@Override
	public void write(JsonWriter out, User user) throws IOException {
		if (user == null) {
			out.nullValue();
			return;
		}

		out.beginObject();
		out.name("email").value(user.getEmail());
		if (user.getPassword() != null) {
			out.name("password").value(user.getPassword());
		}
		out.endObject();
	}

	@Override
	public User read(JsonReader in) throws IOException {
		if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
			in.nextNull();
			return null;
		}

		in.beginObject();
		String email = null;
		String password = null;

		while (in.hasNext()) {
			String fieldName = in.nextName();
			switch (fieldName) {
				case "email":
					email = in.nextString();
					break;
				case "password":
					password = in.nextString();
					break;
				default:
					in.skipValue();
			}
		}
		in.endObject();

		return new User(email, password);
	}
}
