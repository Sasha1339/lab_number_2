package ru.mpei;

import jade.core.AID;
import jade.core.Agent;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class FunctionAgent extends Agent {

    @Override
    protected void setup() {

        System.out.println("Агент " + this.getLocalName() + " родился");
        AgentService.registerAgent(this, "Agents");

        List<AID> agents = AgentService.findAgent(this, "Agents");
        this.addBehaviour(new ReceiveRequestCalculateBehaviour(agents.get(0).getName()));
        if (agents.get(0).getName().equals(this.getName())) {
            double valueXRandom = Math.round((-5 + Math.random() * 5) * 10.0) / 10.0;
            //double valueXRandom = -0.3;
            double deltaXRandom = Math.round((0 + Math.random() * 2) * 10.0) / 10.0;
            //double deltaXRandom = 1.7;
            this.addBehaviour(new ReceiveBackBehaviour(valueXRandom, deltaXRandom));
        }
    }
}


