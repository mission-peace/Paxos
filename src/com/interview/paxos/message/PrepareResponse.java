package com.interview.paxos.message;

import com.interview.paxos.Message;

public class PrepareResponse implements Message{

    private String proposalValue;
    private long proposalNumber;
    private long proposalNumberInPrepare;
    private String sourceId;
    
    @Override
    public MessageType getMessageType() {
        return MessageType.PrepareResponse;
    }
    
    public PrepareResponse(String proposalValue, long proposalNumber, long proposalNumberInPrepare, String sourceId){
        this.proposalNumber = proposalNumber;
        this.proposalValue = proposalValue;
        this.proposalNumberInPrepare = proposalNumberInPrepare;
        this.sourceId = sourceId;
    }

    public String getProposalValue() {
        return proposalValue;
    }

    public long getProposalNumber() {
        return proposalNumber;
    }
    
    public long getProposalNumberInPrepare(){
        return proposalNumberInPrepare;
    }
    
    public String getSourceId(){
        return sourceId;
    }

    @Override
    public String toString() {
        return "PrepareResponse [proposalValue=" + proposalValue
                + ", proposalNumber=" + proposalNumber
                + ", proposalNumberInPrepare=" + proposalNumberInPrepare
                + ", sourceId=" + sourceId + "]";
    }

}
