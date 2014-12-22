package com.interview.paxos;

import com.interview.paxos.message.MessageType;

public interface Message {

    MessageType getMessageType();
}
