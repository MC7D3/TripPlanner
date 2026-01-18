package tpgroup.persistence.gson;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import tpgroup.model.Event;
import tpgroup.model.EventsNode;
import tpgroup.model.domain.Proposal;
import tpgroup.model.domain.ProposalType;
import tpgroup.model.domain.User;

public class ProposalTypeAdapter extends TypeAdapter<Proposal> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    @Override
    public void write(JsonWriter out, Proposal proposal) throws IOException {
        if (proposal == null) {
            out.nullValue();
            return;
        }
        
        out.beginObject();
        out.name("proposalType").value(proposal.getProposalType().name());
        
        // EventsNode - we'll store just the ID since it's graph-dependent
        if (proposal.getNodeName() != null) {
            out.name("nodeId").value(proposal.getNodeName().getId().toString());
        }
        
        // Event
        if (proposal.getEvent() != null) {
            out.name("event");
            new EventTypeAdapter().write(out, proposal.getEvent());
        }
        
        // Update Event
        if (proposal.getUpdateEvent() != null && proposal.getUpdateEvent().isPresent()) {
            out.name("updateEvent");
            new EventTypeAdapter().write(out, proposal.getUpdateEvent().get());
        }
        
        // Creator
        if (proposal.getCreator() != null) {
            out.name("creator");
            new UserTypeAdapter().write(out, proposal.getCreator());
        }
        
        // Likes
        if (proposal.getLikesList() != null && !proposal.getLikesList().isEmpty()) {
            out.name("likes").beginArray();
            for (User user : proposal.getLikesList()) {
                new UserTypeAdapter().write(out, user);
            }
            out.endArray();
        }
        
        // Creation Time
        if (proposal.getCreationTime() != null) {
            out.name("creationTime").value(proposal.getCreationTime().format(formatter));
        }
        out.endObject();
    }
    
    @Override
    public Proposal read(JsonReader in) throws IOException {
        if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        
        in.beginObject();
        ProposalType proposalType = null;
        UUID nodeId = null;
        Event event = null;
        Event updateEvent = null;
        User creator = null;
        Set<User> likes = new HashSet<>();
        LocalDateTime creationTime = LocalDateTime.now();
        
        while (in.hasNext()) {
            String fieldName = in.nextName();
            switch (fieldName) {
                case "proposalType":
                    try {
                        proposalType = ProposalType.valueOf(in.nextString());
                    } catch (IllegalArgumentException e) {
                        proposalType = null;
                    }
                    break;
                case "nodeId":
                    nodeId = UUID.fromString(in.nextString());
                    break;
                case "event":
                    event = new EventTypeAdapter().read(in);
                    break;
                case "updateEvent":
                    updateEvent = new EventTypeAdapter().read(in);
                    break;
                case "creator":
                    creator = new UserTypeAdapter().read(in);
                    break;
                case "likes":
                    in.beginArray();
                    while (in.hasNext()) {
                        User user = new UserTypeAdapter().read(in);
                        if (user != null) {
                            likes.add(user);
                        }
                    }
                    in.endArray();
                    break;
                case "creationTime":
                    creationTime = LocalDateTime.parse(in.nextString(), formatter);
                    break;
                default:
                    in.skipValue();
            }
        }
        in.endObject();
        
        if (proposalType != null && event != null && creator != null) {
            EventsNode node = nodeId != null ? new EventsNode(nodeId, new TreeSet<>()) : null;
            
            Optional<Event> updateEventOpt = updateEvent != null ? Optional.of(updateEvent) : Optional.empty();
            return new Proposal(proposalType, node, event, updateEventOpt, creator, likes, creationTime);
        }
        
        return null;
    }
}
