package tpgroup.persistence.gson;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import tpgroup.model.Event;
import tpgroup.model.domain.PointOfInterest;

public class EventTypeAdapter extends TypeAdapter<Event> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    @Override
    public void write(JsonWriter out, Event event) throws IOException {
        if (event == null) {
            out.nullValue();
            return;
        }
        
        out.beginObject();
        
        if (event.getInfo() != null) {
            out.name("poi");
            new PointOfInterestTypeAdapter().write(out, event.getInfo());
        }
        
        if (event.getStart() != null) {
            out.name("start").value(event.getStart().format(formatter));
        }
        
        if (event.getEnd() != null) {
            out.name("end").value(event.getEnd().format(formatter));
        }
        
        out.endObject();
    }
    
    @Override
    public Event read(JsonReader in) throws IOException {
        if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        
        in.beginObject();
        PointOfInterest info = null;
        LocalDateTime start = null;
        LocalDateTime end = null;
        
        while (in.hasNext()) {
            String fieldName = in.nextName();
            switch (fieldName) {
                case "poi":
                    info = new PointOfInterestTypeAdapter().read(in);
                    break;
                case "start":
                    start = LocalDateTime.parse(in.nextString(), formatter);
                    break;
                case "end":
                    end = LocalDateTime.parse(in.nextString(), formatter);
                    break;
                default:
                    in.skipValue();
            }
        }
        in.endObject();
        
        if (info != null && start != null && end != null) {
            return new Event(info, start, end);
        }
        
        return null;
    }
}
