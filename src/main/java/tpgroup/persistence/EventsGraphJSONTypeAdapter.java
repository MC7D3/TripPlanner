package tpgroup.persistence;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import tpgroup.model.Event;
import tpgroup.model.EventsGraph;
import tpgroup.model.EventsNode;
import tpgroup.model.domain.Coordinates;
import tpgroup.model.domain.PointOfInterest;
import tpgroup.model.domain.Rating;
import tpgroup.model.domain.Tag;
import tpgroup.model.exception.MalformedJSONException;

public class EventsGraphJSONTypeAdapter extends TypeAdapter<EventsGraph> {

    @Override
    public void write(JsonWriter out, EventsGraph value) throws IOException {
        out.beginObject();
        
        out.name("rootId").value(value.getRoot().getId().toString());
        
        out.name("nodes");
        out.beginArray();
        for (EventsNode node : value.getAllNodes()) {
            serializeNode(out, node);
        }
        out.endArray();
        
        out.name("edges");
        out.beginArray();
        Map<EventsNode, Set<EventsNode>> connections = value.getConnectionsMapping();
        for (Map.Entry<EventsNode, Set<EventsNode>> entry : connections.entrySet()) {
            EventsNode parent = entry.getKey();
            for (EventsNode child : entry.getValue()) {
                serializeEdge(out, parent, child);
            }
        }
        out.endArray();
        
        out.name("stagingNodeIds");
        out.beginArray();
        for (EventsNode node : value.getStagingNodes()) {
            out.value(node.getId().toString());
        }
        out.endArray();
        
        out.endObject();
    }

    private void serializeNode(JsonWriter out, EventsNode node) throws IOException {
        out.beginObject();
        out.name("id").value(node.getId().toString());
        out.name("events");
        out.beginArray();
        for (Event event : node.allEvents()) {
            serializeEvent(out, event);
        }
        out.endArray();
        out.endObject();
    }

    private void serializeEvent(JsonWriter out, Event event) throws IOException {
        out.beginObject();
        
        PointOfInterest poi = event.getInfo();
        out.name("poi");
        out.beginObject();
        out.name("name").value(poi.getName());
        out.name("description").value(poi.getDescription());
        out.name("city").value(poi.getCity());
        out.name("country").value(poi.getCountry());
        
        out.name("coordinates");
        out.beginObject();
        out.name("latitude").value(poi.getCoordinates().getLatitude());
        out.name("longitude").value(poi.getCoordinates().getLongitude());
        out.endObject();
        
        out.name("rating").value(poi.getRating().name());
        
        out.name("tags");
        out.beginArray();
        for (Tag tag : poi.getTags()) {
            out.value(tag.name());
        }
        out.endArray();
        out.endObject();
        
        out.name("start").value(event.getStart().toString());
        out.name("end").value(event.getEnd().toString());
        out.endObject();
    }

    private void serializeEdge(JsonWriter out, EventsNode from, EventsNode to) throws IOException {
        out.beginObject();
        out.name("from").value(from.getId().toString());
        out.name("to").value(to.getId().toString());
        out.endObject();
    }

    @Override
    public EventsGraph read(JsonReader in) throws IOException {
        String rootId = null;
        Map<String, NodeData> nodeDataMap = new HashMap<>();
        List<EdgeData> edges = new ArrayList<>();
        Set<String> stagingNodeIds = new HashSet<>();

        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "rootId":
                    rootId = in.nextString();
                    break;
                    
                case "nodes":
                    in.beginArray();
                    while (in.hasNext()) {
                        NodeData nodeData = deserializeNodeData(in);
                        nodeDataMap.put(nodeData.id, nodeData);
                    }
                    in.endArray();
                    break;
                    
                case "edges":
                    in.beginArray();
                    while (in.hasNext()) {
                        edges.add(deserializeEdgeData(in));
                    }
                    in.endArray();
                    break;
                    
                case "stagingNodeIds":
                    in.beginArray();
                    while (in.hasNext()) {
                        stagingNodeIds.add(in.nextString());
                    }
                    in.endArray();
                    break;
                    
                default:
                    in.skipValue();
            }
        }
        in.endObject();

        if (rootId == null) {
            throw new MalformedJSONException("Root ID is required in JSON");
        }

        if (!nodeDataMap.containsKey(rootId)) {
            throw new MalformedJSONException("Root node not found in nodes list");
        }

        Map<String, EventsNode> createdNodes = new HashMap<>();
        
        for (Map.Entry<String, NodeData> entry : nodeDataMap.entrySet()) {
            String nodeId = entry.getKey();
            NodeData nodeData = entry.getValue();
            
            EventsNode node = new EventsNode(UUID.fromString(nodeId), nodeData.events);
            createdNodes.put(nodeId, node);
        }
        
        EventsNode root = createdNodes.get(rootId);
        Set<EventsNode> nodes = new HashSet<>();
        Set<EventsNode> stagingArea = new HashSet<>();
        Map<EventsNode, Set<EventsNode>> connectionsMapping = new HashMap<>();
        
        for (Map.Entry<String, EventsNode> entry : createdNodes.entrySet()) {
            String nodeId = entry.getKey();
            EventsNode node = entry.getValue();
            
            if (stagingNodeIds.contains(nodeId)) {
                stagingArea.add(node);
            } else {
                nodes.add(node);
                connectionsMapping.put(node, new HashSet<>());
            }
        }
        
        if (!nodes.contains(root)) {
            nodes.add(root);
            connectionsMapping.put(root, new HashSet<>());
        }
        
        for (EdgeData edge : edges) {
            EventsNode fromNode = createdNodes.get(edge.from);
            EventsNode toNode = createdNodes.get(edge.to);
            
            if (fromNode == null || toNode == null) {
                throw new MalformedJSONException("Edge references non-existent node: " + 
                    edge.from + " -> " + edge.to);
            }
            
            if (nodes.contains(fromNode) && nodes.contains(toNode)) {
                connectionsMapping.get(fromNode).add(toNode);
            }
        }
        
        return new EventsGraph(root, connectionsMapping, nodes, stagingArea);
    }

    private NodeData deserializeNodeData(JsonReader in) throws IOException {
        in.beginObject();
        String id = null;
        NavigableSet<Event> events = new TreeSet<>();

        while (in.hasNext()) {
            String fieldName = in.nextName();
            switch (fieldName) {
                case "id":
                    id = in.nextString();
                    break;
                case "events":
                    in.beginArray();
                    while (in.hasNext()) {
                        events.add(deserializeEvent(in));
                    }
                    in.endArray();
                    break;
                default:
                    in.skipValue();
            }
        }
        in.endObject();

        if (id == null) {
            throw new MalformedJSONException("Node missing ID");
        }

        return new NodeData(id, events);
    }

    private Event deserializeEvent(JsonReader in) throws IOException {
        in.beginObject();
        PointOfInterest poi = null;
        LocalDateTime start = null;
        LocalDateTime end = null;

        while (in.hasNext()) {
            String fieldName = in.nextName();
            switch (fieldName) {
                case "poi":
                    poi = deserializePOI(in);
                    break;
                case "start":
                    start = LocalDateTime.parse(in.nextString());
                    break;
                case "end":
                    end = LocalDateTime.parse(in.nextString());
                    break;
                default:
                    in.skipValue();
            }
        }
        in.endObject();

        if (poi == null || start == null || end == null) {
            throw new MalformedJSONException("Missing required event fields");
        }

        return new Event(poi, start, end);
    }

    private PointOfInterest deserializePOI(JsonReader in) throws IOException {
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
                    coordinates = deserializeCoordinates(in);
                    break;
                case "rating":
                    rating = Rating.valueOf(in.nextString());
                    break;
                case "tags":
                    in.beginArray();
                    while (in.hasNext()) {
                        tags.add(Tag.valueOf(in.nextString()));
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

    private Coordinates deserializeCoordinates(JsonReader in) throws IOException {
        in.beginObject();
        double latitude = 0;
        double longitude = 0;

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

    private EdgeData deserializeEdgeData(JsonReader in) throws IOException {
        in.beginObject();
        String from = null;
        String to = null;

        while (in.hasNext()) {
            String fieldName = in.nextName();
            switch (fieldName) {
                case "from":
                    from = in.nextString();
                    break;
                case "to":
                    to = in.nextString();
                    break;
                default:
                    in.skipValue();
            }
        }
        in.endObject();

        return new EdgeData(from, to);
    }

    private static class NodeData {
        String id;
        NavigableSet<Event> events;
        
        NodeData(String id, NavigableSet<Event> events) {
            this.id = id;
            this.events = events;
        }
    }
    
    private static class EdgeData {
        String from;
        String to;
        
        EdgeData(String from, String to) {
            this.from = from;
            this.to = to;
        }
    }
}
