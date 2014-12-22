package com.interview.paxos.message;

import com.interview.paxos.Message;

public class LearnerMessage implements Message {

    private String sourceId;
    private String acceptedValue;
    public LearnerMessage(String acceptedValue, String sourceId) {
        this.acceptedValue = acceptedValue;
        this.sourceId = sourceId;
    }
    
    @Override
    public MessageType getMessageType() {
        return MessageType.LearnerMessage;
    }
    
    public String getAcceptedValue(){
        return acceptedValue;
    }

    public String getSourceId(){
        return sourceId;
    }

    @Override
    public String toString() {
        return "LearnerMessage [sourceId=" + sourceId + ", acceptedValue="
                + acceptedValue + "]";
    }
    
}
