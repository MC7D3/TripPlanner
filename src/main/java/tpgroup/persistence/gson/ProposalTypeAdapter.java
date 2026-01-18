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
    private static final EventTypeAdapter eventAdapter = new EventTypeAdapter();
    private static final UserTypeAdapter userAdapter = new UserTypeAdapter();
    
    @Override
    public void write(JsonWriter out, Proposal proposal) throws IOException {
        if (proposal == null) {
            out.nullValue();
            return;
        }
        
        out.beginObject();
        writeProposalFields(out, proposal);
        out.endObject();
    }
    
    private void writeProposalFields(JsonWriter out, Proposal proposal) throws IOException {
        out.name("proposalType").value(proposal.getProposalType().name());
        
        if (proposal.getNodeName() != null) {
            out.name("nodeId").value(proposal.getNodeName().getId().toString());
        }
        
        if (proposal.getEvent() != null) {
            out.name("event");
            eventAdapter.write(out, proposal.getEvent());
        }
        
        if (proposal.getUpdateEvent().isPresent()) {
            out.name("updateEvent");
            eventAdapter.write(out, proposal.getUpdateEvent().get());
        }
        
        if (proposal.getCreator() != null) {
            out.name("creator");
            userAdapter.write(out, proposal.getCreator());
        }
        
        if (proposal.getLikesList() != null && !proposal.getLikesList().isEmpty()) {
            writeLikesArray(out, proposal.getLikesList());
        }
        
        if (proposal.getCreationTime() != null) {
            out.name("creationTime").value(proposal.getCreationTime().format(formatter));
        }
    }
    
    private void writeLikesArray(JsonWriter out, Set<User> likes) throws IOException {
        out.name("likes").beginArray();
        for (User user : likes) {
            userAdapter.write(out, user);
        }
        out.endArray();
    }
    
    @Override
    public Proposal read(JsonReader in) throws IOException {
        if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        
        ProposalParseResult parseResult = parseProposalJson(in);
        
        if (!isValidProposalData(parseResult)) {
            return null;
        }
        
        return buildProposalFromParseResult(parseResult);
    }
    
    private ProposalParseResult parseProposalJson(JsonReader in) throws IOException {
        ProposalParseResult result = new ProposalParseResult();
        
        in.beginObject();
        while (in.hasNext()) {
            parseField(in, in.nextName(), result);
        }
        in.endObject();
        
        return result;
    }
    
    private void parseField(JsonReader in, String fieldName, ProposalParseResult result) throws IOException {
        switch (fieldName) {
            case "proposalType":
                result.proposalType = parseProposalType(in);
                break;
            case "nodeId":
                result.nodeId = parseUUID(in);
                break;
            case "event":
                result.event = parseEvent(in);
                break;
            case "updateEvent":
                result.updateEvent = parseEvent(in);
                break;
            case "creator":
                result.creator = parseUser(in);
                break;
            case "likes":
                result.likes = parseLikesArray(in);
                break;
            case "creationTime":
                result.creationTime = parseCreationTime(in);
                break;
            default:
                in.skipValue();
        }
    }
    
    private ProposalType parseProposalType(JsonReader in) throws IOException {
        try {
            return ProposalType.valueOf(in.nextString());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    private UUID parseUUID(JsonReader in) throws IOException {
        try {
            return UUID.fromString(in.nextString());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    private Event parseEvent(JsonReader in) throws IOException {
        return eventAdapter.read(in);
    }
    
    private User parseUser(JsonReader in) throws IOException {
        return userAdapter.read(in);
    }
    
    private Set<User> parseLikesArray(JsonReader in) throws IOException {
        Set<User> likes = new HashSet<>();
        
        in.beginArray();
        while (in.hasNext()) {
            User user = userAdapter.read(in);
            if (user != null) {
                likes.add(user);
            }
        }
        in.endArray();
        
        return likes;
    }
    
    private LocalDateTime parseCreationTime(JsonReader in) throws IOException {
        try {
            return LocalDateTime.parse(in.nextString(), formatter);
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }
    
    private boolean isValidProposalData(ProposalParseResult parseResult) {
        return parseResult.proposalType != null && 
               parseResult.event != null && 
               parseResult.creator != null;
    }
    
    private Proposal buildProposalFromParseResult(ProposalParseResult parseResult) {
        EventsNode node = parseResult.nodeId != null ? 
            new EventsNode(parseResult.nodeId, new TreeSet<>()) : null;
        
        Optional<Event> updateEventOpt = parseResult.updateEvent != null ? 
            Optional.of(parseResult.updateEvent) : Optional.empty();
        
        return new Proposal(
            parseResult.proposalType,
            node,
            parseResult.event,
            updateEventOpt,
            parseResult.creator,
            parseResult.likes,
            parseResult.creationTime
        );
    }
    
    private static class ProposalParseResult {
        ProposalType proposalType;
        UUID nodeId;
        Event event;
        Event updateEvent;
        User creator;
        Set<User> likes = new HashSet<>();
        LocalDateTime creationTime = LocalDateTime.now();
    }
}
