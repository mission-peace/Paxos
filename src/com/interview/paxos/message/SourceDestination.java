package com.interview.paxos.message;

public class SourceDestination {

    private String sourceId;
    private String destinationId;
    
    public SourceDestination(String sourceId, String destinationId){
        this.sourceId = sourceId;
        this.destinationId  = destinationId;
    }
    
    public String getSourceId() {
        return sourceId;
    }
    public String getDestinationId() {
        return destinationId;
    }
}
