package com.interview.paxos.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.interview.paxos.Learner;
import com.interview.paxos.Message;
import com.interview.paxos.message.LearnerMessage;
import com.interview.paxos.message.MessageType;
import com.interview.paxos.message.SourceDestination;

public class LearnerImpl implements Learner{

    private final int numberOfAcceptors;
    private String id;
    private Map<String, Set<String>> acceptorsMap = new HashMap<String, Set<String>>();
    public LearnerImpl(String id, int numberOfAcceptors){
        this.id = id;
        this.numberOfAcceptors = numberOfAcceptors;
    }
    
    @Override
    public synchronized void receiveMessage(SourceDestination sd, Message m) {
        if(MessageType.LearnerMessage.equals(m.getMessageType())){
            learnProposedValue((LearnerMessage)m);
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public synchronized void learnProposedValue(LearnerMessage m) {
        System.out.println("Learner Acceptor map " + acceptorsMap);
        if(acceptorsMap.containsKey(m.getAcceptedValue())){
            Set<String> acceptorSet = acceptorsMap.get(m.getAcceptedValue());
            acceptorSet.add(m.getSourceId());
            if(acceptorSet.size() > numberOfAcceptors/2){
                System.out.println("Final accepted value " + m.getAcceptedValue());
            }
        }else{
            Set<String> acceptorSet = new HashSet<String>();
            acceptorSet.add(m.getSourceId());
            acceptorsMap.put(m.getAcceptedValue(), acceptorSet);
            if(acceptorSet.size() > numberOfAcceptors/2){
                System.out.println("Final accepted value " + m.getAcceptedValue());
            }
        }
    }

}
