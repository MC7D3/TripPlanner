package tpgroup.persistence.gson;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import tpgroup.model.EventsGraph;
import tpgroup.model.EventsNode;
import tpgroup.model.domain.Proposal;
import tpgroup.model.domain.Trip;

public class TripTypeAdapter extends TypeAdapter<Trip> {
    
    @Override
    public void write(JsonWriter out, Trip trip) throws IOException {
        if (trip == null) {
            out.nullValue();
            return;
        }
        
        out.beginObject();
        out.name("country").value(trip.getCountry());
        out.name("mainCity").value(trip.getMainCity());
        
        if (trip.getProposals() != null && !trip.getProposals().isEmpty()) {
            out.name("proposals").beginArray();
            for (Proposal proposal : trip.getProposals()) {
                new ProposalTypeAdapter().write(out, proposal);
            }
            out.endArray();
        }
        
        if (trip.getTripGraph() != null) {
            out.name("tripGraph");
            new EventsGraphJSONTypeAdapter().write(out, trip.getTripGraph());
        }
        
        out.endObject();
    }
    
    @Override
    public Trip read(JsonReader in) throws IOException {
        if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        
        in.beginObject();
        String country = null;
        String mainCity = null;
        Set<Proposal> proposals = new HashSet<>();
        EventsGraph tripGraph = null;
        
        while (in.hasNext()) {
            String fieldName = in.nextName();
            switch (fieldName) {
                case "country":
                    country = in.nextString();
                    break;
                case "mainCity":
                    mainCity = in.nextString();
                    break;
                case "proposals":
                    proposals = parseProposalsArray(in);
                    break;
                case "tripGraph":
                    tripGraph = new EventsGraphJSONTypeAdapter().read(in);
                    break;
                default:
                    in.skipValue();
            }
        }
        in.endObject();
        
        if (tripGraph == null) {
            tripGraph = new EventsGraph();
        }
        
        Set<Proposal> reconnectedProposals = reconnectProposals(proposals, tripGraph);
        
        return new Trip(country, mainCity, reconnectedProposals, tripGraph);
    }
    
    private Set<Proposal> parseProposalsArray(JsonReader in) throws IOException {
        Set<Proposal> proposals = new HashSet<>();
        in.beginArray();
        while (in.hasNext()) {
            Proposal proposal = new ProposalTypeAdapter().read(in);
            if (proposal != null) {
                proposals.add(proposal);
            }
        }
        in.endArray();
        return proposals;
    }
    
    private Set<Proposal> reconnectProposals(Set<Proposal> proposals, EventsGraph tripGraph) {
        if (proposals.isEmpty() || tripGraph == null) {
            return proposals;
        }
        
        Set<Proposal> reconnected = new HashSet<>();
        for (Proposal proposal : proposals) {
            reconnected.add(reconnectProposal(proposal, tripGraph));
        }
        return reconnected;
    }
    
    private Proposal reconnectProposal(Proposal proposal, EventsGraph tripGraph) {
        if (proposal.getNodeName() == null) {
            return proposal;
        }
        
        EventsNode actualNode = tripGraph.getAllNodes().stream()
            .filter(node -> node.getId().equals(proposal.getNodeName().getId()))
            .findFirst()
            .orElse(null);
        
        if (actualNode == null) {
            return proposal;
        }
        
        return new Proposal(
            proposal.getProposalType(),
            actualNode,
            proposal.getEvent(),
            proposal.getUpdateEvent(),
            proposal.getCreator(),
            proposal.getLikesList(),
            proposal.getCreationTime()
        );
    }
}
