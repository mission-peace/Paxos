package com.interview.paxos;

import com.interview.paxos.message.PrepareResponse;


public interface Proposer extends Component{

    public void propose(String proposedValue);
    public void receivePrepare(PrepareResponse r);
}
