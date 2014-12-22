package com.interview.paxos.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import com.interview.paxos.Component;
import com.interview.paxos.ComponentType;
import com.interview.paxos.Message;
import com.interview.paxos.NetworkDelivery;
import com.interview.paxos.Proposer;
import com.interview.paxos.message.AcceptRequest;
import com.interview.paxos.message.MessageType;
import com.interview.paxos.message.PrepareRequest;
import com.interview.paxos.message.PrepareResponse;
import com.interview.paxos.message.SourceDestination;

public class ProposerImpl implements Proposer {

    private final int totalAcceptors;
    private final NetworkDelivery delivery;
    private final String id;
    private AtomicLong currentProposalNumber = new AtomicLong(0);
    private AtomicLong highestProposalNumberAccepted = new AtomicLong(Long.MIN_VALUE);
    private AtomicReference<String> valueOfHigestProper = new AtomicReference<String>();
    private AtomicInteger numberOfResponseSeenSoFar = new AtomicInteger(0);
    private Set<String> acceptsToSend = new HashSet<String>();
    public ProposerImpl(int totalAcceptors, NetworkDelivery delivery, String id){
        this.totalAcceptors = totalAcceptors;
        this.delivery = delivery;
        this.id = id;
    }
    
    @Override
    public String getId(){
        return id;
    }
    
    @Override
    public void receiveMessage(SourceDestination sd, Message m) {
        if(MessageType.PrepareResponse.equals(m.getMessageType())){
            receivePrepare((PrepareResponse)m);
        }
    }

    
    @Override
    public void propose(String proposedValue) {
        Set<Component> acceptors = delivery.getComponents(ComponentType.ACCEPTOR);
        currentProposalNumber.set(generateUniqueProposalNumber());
//      System.out.println("Size of acceptors " + acceptors.size());
        Set<Component> randomMajorityAcceptors = new HashSet<Component>();
        while(randomMajorityAcceptors.size() <= acceptors.size()/2){
            for(Component acceptor : acceptors){
                if(Math.random() > 0.5){
                    randomMajorityAcceptors.add(acceptor);
                }
            }
        }
//      System.out.println("Proposer Sending proposal to " + randomMajorityAcceptors.size());
        for(Component acceptor : randomMajorityAcceptors){
            String destinationId = acceptor.getId();
//          System.out.println("Sending proposal number " + currentProposalNumber + " " + destinationId);
            PrepareRequest pr = new PrepareRequest(currentProposalNumber.get(), id);
            delivery.deliverMessage(new SourceDestination(id, destinationId), pr);
        }
        int retry = 0;
        while(numberOfResponseSeenSoFar.get() <= totalAcceptors/2){
            try {
//              System.out.println("Proposer " + id + " sleeping and waiting for majority of acceptors");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            retry++;
            if(retry > 10){
                reset();
                return;
            }
        }
//      System.out.println("Proposer Sending accepts to " + acceptsToSend);
        for(String destinationId : acceptsToSend){
            AcceptRequest r = new AcceptRequest();
            r.setProposalNumber(currentProposalNumber.get());
            if(valueOfHigestProper.get() != null){
                r.setValue(valueOfHigestProper.get());
            }
            r.setValue(proposedValue);
//          System.out.println("Proposer Sending accept request to " + r);
            delivery.deliverMessage(new SourceDestination(id, destinationId), r);
        }
        reset();
    }
    
    private synchronized void reset(){
        acceptsToSend.clear();
        numberOfResponseSeenSoFar.getAndSet(0);
        valueOfHigestProper.getAndSet(null);
        currentProposalNumber.getAndSet(0);
        highestProposalNumberAccepted.getAndSet(Long.MIN_VALUE);
    }

    @Override
    public synchronized void receivePrepare(PrepareResponse r) {
//      System.out.println("Proposer Received response " + r + " " + currentProposalNumber.get());
        if(currentProposalNumber.get() != r.getProposalNumberInPrepare()){
//          System.out.println("Proposer Rejecting response from older request " + r);
            return;
        }
        if(acceptsToSend.contains(r.getSourceId())){
//          System.out.println("Proposer Duplicate message from " + r.getSourceId());
            return;
        }
        if(numberOfResponseSeenSoFar.get() > totalAcceptors/2){
//          System.out.println("Proposer Rejecting request since we have majority already spoken");
            return;
        }
        
        if(r.getProposalValue() != null){
            if(highestProposalNumberAccepted.get() < r.getProposalNumber()){
                highestProposalNumberAccepted.getAndSet(r.getProposalNumber());
                valueOfHigestProper.getAndSet(r.getProposalValue());
            }
        }else{
            acceptsToSend.add(r.getSourceId());
        }
        numberOfResponseSeenSoFar.incrementAndGet();
    }


    private long generateUniqueProposalNumber(){
        long currentTime = System.currentTimeMillis()*10000;
        int random = (int)(Math.random()*10000);
        return currentTime + random;
    }
}
