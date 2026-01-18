package tpgroup.persistence.gson;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import tpgroup.model.domain.Coordinates;

public class CoordinatesTypeAdapter extends TypeAdapter<Coordinates> {
    
    @Override
    public void write(JsonWriter out, Coordinates coord) throws IOException {
        if (coord == null) {
            out.nullValue();
            return;
        }
        
        out.beginObject();
        out.name("latitude").value(coord.getLatitude());
        out.name("longitude").value(coord.getLongitude());
        out.endObject();
    }
    
    @Override
    public Coordinates read(JsonReader in) throws IOException {
        if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        
        in.beginObject();
        double latitude = 0.0;
        double longitude = 0.0;
        
        while (in.hasNext()) {
            String fieldName = in.nextName();
            switch (fieldName) {
                case "latitude":
                    latitude = in.nextDouble();
                    break;
                case "longitude":
                    longitude = in.nextDouble();
                    break;
                default:
                    in.skipValue();
            }
        }
        in.endObject();
        
        return new Coordinates(latitude, longitude);
    }
}
