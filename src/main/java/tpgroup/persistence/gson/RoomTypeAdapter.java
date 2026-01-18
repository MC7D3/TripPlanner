package tpgroup.persistence.gson;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import tpgroup.model.domain.Room;
import tpgroup.model.domain.Trip;
import tpgroup.model.domain.User;

public class RoomTypeAdapter extends TypeAdapter<Room> {
    
    @Override
    public void write(JsonWriter out, Room room) throws IOException {
        if (room == null) {
            out.nullValue();
            return;
        }
        
        out.beginObject();
        out.name("code").value(room.getCode());
        out.name("name").value(room.getName());
        
        // Admin
        if (room.getAdmin() != null) {
            out.name("admin");
            new UserTypeAdapter().write(out, room.getAdmin());
        }
        
        // Members
        if (room.getMembers() != null && !room.getMembers().isEmpty()) {
            out.name("members").beginArray();
            for (User member : room.getMembers()) {
                new UserTypeAdapter().write(out, member);
            }
            out.endArray();
        }
        
        // Trip
        if (room.getTrip() != null) {
            out.name("trip");
            new TripTypeAdapter().write(out, room.getTrip());
        }
        out.endObject();
    }
    
    @Override
    public Room read(JsonReader in) throws IOException {
        if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        
        in.beginObject();
        String code = null;
        String name = null;
        User admin = null;
        Set<User> members = new HashSet<>();
        Trip trip = null;
        
        while (in.hasNext()) {
            String fieldName = in.nextName();
            switch (fieldName) {
                case "code":
                    code = in.nextString();
                    break;
                case "name":
                    name = in.nextString();
                    break;
                case "admin":
                    admin = new UserTypeAdapter().read(in);
                    break;
                case "members":
                    in.beginArray();
                    while (in.hasNext()) {
                        User member = new UserTypeAdapter().read(in);
                        if (member != null) {
                            members.add(member);
                        }
                    }
                    in.endArray();
                    break;
                case "trip":
                    trip = new TripTypeAdapter().read(in);
                    break;
                default:
                    in.skipValue();
            }
        }
        in.endObject();
        
        // Create Room based on what we have
        if (code != null && name != null && admin != null && trip != null) {
            return new Room(code, name, admin, members, trip);
        } else if (code != null) {
            // Partial room (constructor with just code)
            Room room = new Room(code);
            room.setName(name);
            room.setAdmin(admin);
            room.setMembers(members);
            room.setTrip(trip);
            return room;
        }
        
        return null;
    }
}
