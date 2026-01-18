package tpgroup.persistence.gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import tpgroup.model.domain.Coordinates;
import tpgroup.model.domain.PointOfInterest;
import tpgroup.model.domain.Rating;
import tpgroup.model.domain.Tag;
import tpgroup.model.exception.EnumNotFoundException;

public class PointOfInterestTypeAdapter extends TypeAdapter<PointOfInterest> {
    
    @Override
    public void write(JsonWriter out, PointOfInterest poi) throws IOException {
        if (poi == null) {
            out.nullValue();
            return;
        }
        
        out.beginObject();
        out.name("name").value(poi.getName());
        out.name("description").value(poi.getDescription());
        out.name("city").value(poi.getCity());
        out.name("country").value(poi.getCountry());
        
        out.name("coordinates");
        new CoordinatesTypeAdapter().write(out, poi.getCoordinates());
        
        if (poi.getRating() != null) {
            out.name("rating").value(poi.getRating().name());
        }
        
        if (poi.getTags() != null && !poi.getTags().isEmpty()) {
            out.name("tags").beginArray();
            for (Tag tag : poi.getTags()) {
                out.value(tag.name());
            }
            out.endArray();
        }
        out.endObject();
    }
    
    @Override
    public PointOfInterest read(JsonReader in) throws IOException {
        if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        
        in.beginObject();
        String name = null;
        String description = null;
        String city = null;
        String country = null;
        Coordinates coordinates = null;
        Rating rating = null;
        List<Tag> tags = new ArrayList<>();
        
        while (in.hasNext()) {
            String fieldName = in.nextName();
            switch (fieldName) {
                case "name":
                    name = in.nextString();
                    break;
                case "description":
                    description = in.nextString();
                    break;
                case "city":
                    city = in.nextString();
                    break;
                case "country":
                    country = in.nextString();
                    break;
                case "coordinates":
                    coordinates = new CoordinatesTypeAdapter().read(in);
                    break;
                case "rating":
                    String ratingStr = in.nextString();
                    try {
                        rating = Rating.getRatingFromName(ratingStr);
                    } catch (EnumNotFoundException e) {
						throw new IllegalStateException(e);
                    }
                    break;
                case "tags":
                    in.beginArray();
                    while (in.hasNext()) {
                        String tagStr = in.nextString();
                        try {
                            tags.add(Tag.getTagFromName(tagStr));
                        } catch (EnumNotFoundException e) {
							throw new IllegalStateException(e);
                        }
                    }
                    in.endArray();
                    break;
                default:
                    in.skipValue();
            }
        }
        in.endObject();
        
        return new PointOfInterest(name, description, city, country, coordinates, rating, tags);
    }
}
