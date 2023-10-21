package ru.mpei;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class QueueChangeBehaviour extends OneShotBehaviour {
    double newValueX;
    double newDeltaX;
    public QueueChangeBehaviour(double newValueX, double newDeltaX) {
        this.newValueX = newValueX;
        this.newDeltaX = newDeltaX;
    }

    @Override
    public void action() {
        ACLMessage sendMessage = new ACLMessage(ACLMessage.REQUEST);

        List<AID> agents = AgentService.findAgent(myAgent, "Agents");
        agents.stream()
                .filter(agent -> agents.indexOf(agent) != AgentService.indexAgent)
                .forEach(aid -> sendMessage.addReceiver(aid));
        int newIndexInit = AgentService.indexAgent;
        while (agents.indexOf(myAgent.getAID()) == newIndexInit){
            newIndexInit = (int) (Math.random() * 3);
        }
        AgentService.indexAgent = newIndexInit;
        log.info("Система: Cмена инициатора, новый инициатор: "+agents.get(newIndexInit).getLocalName());
        sendMessage.setContent(newValueX+" "+newDeltaX);
        myAgent.send(sendMessage);
    }
}
