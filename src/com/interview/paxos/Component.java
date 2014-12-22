package com.interview.paxos;

import com.interview.paxos.message.SourceDestination;

public interface Component {

    void receiveMessage(SourceDestination sd, Message m);
    String getId();
}
