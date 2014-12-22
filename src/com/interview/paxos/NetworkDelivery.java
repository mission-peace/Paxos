package com.interview.paxos;

import java.util.Set;

import com.interview.paxos.message.SourceDestination;

public interface NetworkDelivery {
    void registerComponent(String id, ComponentType type, Component component);
    Set<Component> getComponents(ComponentType type);
    void deliverMessage(SourceDestination id, Message m);
}
