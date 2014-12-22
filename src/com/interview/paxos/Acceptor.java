package com.interview.paxos;

import com.interview.paxos.message.AcceptRequest;
import com.interview.paxos.message.PrepareRequest;
import com.interview.paxos.message.PrepareResponse;

public interface Acceptor extends Component{

    public PrepareResponse prepare(PrepareRequest m);

    public void accept(AcceptRequest m);

    public void sentLearntValue();

}
