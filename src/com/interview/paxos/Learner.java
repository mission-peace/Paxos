package com.interview.paxos;

import com.interview.paxos.message.LearnerMessage;

public interface Learner extends Component{

    public void learnProposedValue(LearnerMessage m);
}
