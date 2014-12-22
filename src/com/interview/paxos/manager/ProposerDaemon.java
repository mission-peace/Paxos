package com.interview.paxos.manager;

import java.util.UUID;

import com.interview.paxos.Proposer;

public class ProposerDaemon extends Thread {

    private Proposer proposer;

    public ProposerDaemon(Proposer proposer) {
        this.proposer = proposer;
    }

    @Override
    public void run() {
        String uuid = proposer.getId() + "-" + UUID.randomUUID();
        while (true) {
            try {
                proposer.propose(uuid);
                Thread.sleep((long) (5000 + (int) (5000 * Math.random())));
            } catch (Exception e) {
                e.printStackTrace();
                System.out
                        .println("Exception while proposing value. Swallowing");
            }
        }
    }
}
