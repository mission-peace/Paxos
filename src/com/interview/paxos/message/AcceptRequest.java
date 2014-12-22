package com.interview.paxos.message;

import com.interview.paxos.Message;

public class AcceptRequest implements Message{

    private long proposalNumber;
    private String value;
    public long getProposalNumber() {
        return proposalNumber;
    }
    public void setProposalNumber(long proposalNumber) {
        this.proposalNumber = proposalNumber;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    @Override
    public MessageType getMessageType() {
        return MessageType.AcceptRequest;
    }
    @Override
    public String toString() {
        return "AcceptRequest [proposalNumber=" + proposalNumber + ", value="
                + value + "]";
    }
    
}
