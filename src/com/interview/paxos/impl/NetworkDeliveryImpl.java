package com.interview.paxos.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.interview.paxos.Component;
import com.interview.paxos.ComponentType;
import com.interview.paxos.Message;
import com.interview.paxos.NetworkDelivery;
import com.interview.paxos.message.SourceDestination;

public class NetworkDeliveryImpl implements NetworkDelivery {

    private double networkReliability;
    private Map<String, Component> idToComponentMap = new HashMap<String, Component>();
    private Map<ComponentType, Set<Component>> typeToComponentMap = new HashMap<ComponentType, Set<Component>>();
    public NetworkDeliveryImpl(double networkReliability){
        if(networkReliability <=0 || networkReliability >=1){
            throw new IllegalArgumentException();
        }
        this.networkReliability = networkReliability;
    }
    @Override
    public synchronized void registerComponent(String id, ComponentType type,
            Component component) {
        idToComponentMap.put(id, component);
        if(typeToComponentMap.containsKey(type)){
            Set<Component> components = typeToComponentMap.get(type);
            components.add(component);
        }else{
            Set<Component> components = new HashSet<Component>();
            components.add(component);
            typeToComponentMap.put(type, components);
        }
    }
    
    @Override
    public synchronized Set<Component> getComponents(ComponentType type) {
        Set<Component> components = typeToComponentMap.get(type);
        return Collections.unmodifiableSet(components);
    }
    
    @Override
    public synchronized void deliverMessage(SourceDestination id, Message m) {
        // TODO Auto-generated method stub
        AsyncMesssageDelivery amd = new AsyncMesssageDelivery(id, m);
        amd.start();
    }
    
    private class AsyncMesssageDelivery extends Thread{
    
        private SourceDestination sd;
        private Message m;
        public AsyncMesssageDelivery(SourceDestination sd, Message m) {
            this.sd = sd;
            this.m = m;
        }
        public void run(){
            double rand = Math.random();
            if(rand < networkReliability){
                if(idToComponentMap.containsKey(sd.getDestinationId())){
                    Component comp = idToComponentMap.get(sd.getDestinationId());
                    comp.receiveMessage(sd, m);
                }else{
                    throw new IllegalArgumentException("No such destination exists");
                }
            }else{
                //pretend like network failed to deliver and dont even tell source that it failed
            }

        }
    }
    
}
