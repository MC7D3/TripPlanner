package tpgroup.persistence.gson;

import java.io.IOException;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.UUID;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import tpgroup.model.Event;
import tpgroup.model.EventsNode;

public class EventsNodeTypeAdapter extends TypeAdapter<EventsNode> {
    
    @Override
    public void write(JsonWriter out, EventsNode node) throws IOException {
        if (node == null) {
            out.nullValue();
            return;
        }
        
        out.beginObject();
        out.name("id").value(node.getId().toString());
        
        // Events
        if (node.getEvents() != null && !node.getEvents().isEmpty()) {
            out.name("events").beginArray();
            for (Event event : node.getEvents()) {
                new EventTypeAdapter().write(out, event);
            }
            out.endArray();
        }
        out.endObject();
    }
    
    @Override
    public EventsNode read(JsonReader in) throws IOException {
        if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        
        in.beginObject();
        UUID id = null;
        NavigableSet<Event> events = new TreeSet<>();
        
        while (in.hasNext()) {
            String fieldName = in.nextName();
            switch (fieldName) {
                case "id":
                    id = UUID.fromString(in.nextString());
                    break;
                case "events":
                    in.beginArray();
                    while (in.hasNext()) {
                        Event event = new EventTypeAdapter().read(in);
                        if (event != null) {
                            events.add(event);
                        }
                    }
                    in.endArray();
                    break;
                default:
                    in.skipValue();
            }
        }
        in.endObject();
        
        if (id != null) {
            return new EventsNode(id, events);
        }
        
        return null;
    }
}
