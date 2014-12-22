package com.interview.paxos.manager;

import com.interview.paxos.Acceptor;
import com.interview.paxos.ComponentType;
import com.interview.paxos.Learner;
import com.interview.paxos.NetworkDelivery;
import com.interview.paxos.Proposer;
import com.interview.paxos.impl.AcceptorImpl;
import com.interview.paxos.impl.LearnerImpl;
import com.interview.paxos.impl.NetworkDeliveryImpl;
import com.interview.paxos.impl.ProposerImpl;

public class MainWorker {

    private static int NUMBER_OF_ACCEPTORS = 7;
    private static int NUMBER_OF_PROPOSERS = 3;
    private static int NUMBER_OF_LEARNERS = 3;
    public static void main(String args[]){
        
        NetworkDelivery networkDelivery = new NetworkDeliveryImpl(0.9);
        
        for(int i=0; i < NUMBER_OF_LEARNERS; i++){
            Learner learner = new LearnerImpl("Leaner"+i, NUMBER_OF_ACCEPTORS);
            networkDelivery.registerComponent(learner.getId(), ComponentType.LEARNER, learner);
        }
        
        for(int i=0; i < NUMBER_OF_ACCEPTORS ; i++){
            Acceptor acceptor = new AcceptorImpl(networkDelivery, "Acceptor" + i);
            networkDelivery.registerComponent(acceptor.getId(), ComponentType.ACCEPTOR, acceptor);
            AcceptorDaemon daemon = new AcceptorDaemon(acceptor);
            daemon.start();
        }

        for(int i=0; i < NUMBER_OF_PROPOSERS ; i++){
            Proposer proposer = new ProposerImpl(NUMBER_OF_ACCEPTORS, networkDelivery, "Proposer" + i);
            networkDelivery.registerComponent(proposer.getId(), ComponentType.PROPOSER, proposer);
            ProposerDaemon daemon = new ProposerDaemon(proposer);
            daemon.start();
        }
    }
}
