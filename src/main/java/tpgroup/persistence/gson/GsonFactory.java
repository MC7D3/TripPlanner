package tpgroup.persistence.gson;


import java.time.LocalDateTime;

import com.google.gson.GsonBuilder;

import tpgroup.model.Event;
import tpgroup.model.EventsGraph;
import tpgroup.model.domain.Coordinates;
import tpgroup.model.domain.PointOfInterest;
import tpgroup.model.domain.Proposal;
import tpgroup.model.domain.Room;
import tpgroup.model.domain.Trip;
import tpgroup.model.domain.User;

public class GsonFactory {
    
    public static GsonBuilder createDefaultBuilder() {
        return new GsonBuilder()
            .registerTypeAdapter(User.class, new UserTypeAdapter())
            .registerTypeAdapter(Coordinates.class, new CoordinatesTypeAdapter())
            .registerTypeAdapter(PointOfInterest.class, new PointOfInterestTypeAdapter())
            .registerTypeAdapter(Room.class, new RoomTypeAdapter())
            .registerTypeAdapter(Trip.class, new TripTypeAdapter())
            .registerTypeAdapter(Proposal.class, new ProposalTypeAdapter())
            .registerTypeAdapter(Event.class, new EventTypeAdapter())
            .setPrettyPrinting();
    }
    
    public static GsonBuilder createRoomBuilder() {
        return new GsonBuilder()
            .registerTypeAdapter(EventsGraph.class, new EventsGraphJSONTypeAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeJSONTypeAdaper())
            .registerTypeAdapter(User.class, new UserTypeAdapter())
            .registerTypeAdapter(Event.class, new EventTypeAdapter())
            .registerTypeAdapter(Proposal.class, new ProposalTypeAdapter())
            .registerTypeAdapter(Trip.class, new TripTypeAdapter())
            .registerTypeAdapter(Room.class, new RoomTypeAdapter())
            .setPrettyPrinting();
    }
}
