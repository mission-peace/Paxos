package com.interview.paxos.impl;

import java.util.Set;

import com.interview.paxos.Acceptor;
import com.interview.paxos.Component;
import com.interview.paxos.ComponentType;
import com.interview.paxos.Message;
import com.interview.paxos.NetworkDelivery;
import com.interview.paxos.message.AcceptRequest;
import com.interview.paxos.message.LearnerMessage;
import com.interview.paxos.message.MessageType;
import com.interview.paxos.message.PrepareRequest;
import com.interview.paxos.message.PrepareResponse;
import com.interview.paxos.message.SourceDestination;

public class AcceptorImpl implements Acceptor {

    private long proposalNumberAccepted = Long.MIN_VALUE;
    private long lastPrepareProposalNumber = Long.MIN_VALUE;
    private String proposalValueAccepted = null;
    private NetworkDelivery networkDelivery;
    private String id;
    
    public AcceptorImpl(NetworkDelivery delivery, String id) {
        this.networkDelivery = delivery;
        this.id = id;
    }
    
    @Override
    public String getId(){
        return id;
    }
    
    @Override
    public synchronized void receiveMessage(SourceDestination sd, Message m) {
        if(MessageType.PrepareRequest.equals(m.getMessageType())){
            PrepareResponse r = prepare((PrepareRequest)m);
            if(r == null){
                //do nothing and let requester time out
            }else{
                //else deliver the message to source of this message
                SourceDestination sd1 = new SourceDestination(sd.getDestinationId(), sd.getSourceId());
                networkDelivery.deliverMessage(sd1, r);
            }
        }else if(MessageType.AcceptRequest.equals(m.getMessageType())){
            accept((AcceptRequest)m);
        }else{
            throw new IllegalArgumentException("Do not know the message type");
        }
    }

    @Override
    public synchronized PrepareResponse prepare(PrepareRequest m) {
//      System.out.println("Acceptor Received prepare request " + m);
        long proposalNumber = m.getProposalNumber();
        if(proposalNumber <= lastPrepareProposalNumber){
//          System.out.println("Acceptor rejecting request " + m);
            return null;
        }
        lastPrepareProposalNumber = proposalNumber;
        PrepareResponse response = new PrepareResponse(proposalValueAccepted, proposalNumberAccepted, proposalNumber, id);
//      System.out.println("Acceptor Returnig response " + response);
        return response;
    }

    @Override
    public synchronized void accept(AcceptRequest m) {
//      System.out.println("Acceptor Received accept request " + m.getProposalNumber() + " " + m.getValue());
        long proposalNumber = m.getProposalNumber();
        if(proposalNumber < lastPrepareProposalNumber){
            return;
        }
        if(proposalValueAccepted == null){
            proposalValueAccepted = m.getValue();
        }
//      System.out.println("Acceptor Value accepted by " + id + " " + proposalValueAccepted);
    }

    @Override
    public void sentLearntValue() {
        if(proposalValueAccepted != null){
            Set<Component> learners = networkDelivery.getComponents(ComponentType.LEARNER);
            for(Component learner : learners){
//              System.out.println("Acceptor Sending learner " + learner.getId() + " " + proposalValueAccepted);
                networkDelivery.deliverMessage(new SourceDestination(id, learner.getId()), new LearnerMessage(proposalValueAccepted, id));
            }
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AcceptorImpl other = (AcceptorImpl) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    
    
}
