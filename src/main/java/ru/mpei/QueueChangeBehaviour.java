package ru.mpei;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;

@Slf4j
@AllArgsConstructor
public class QueueChangeBehaviour extends OneShotBehaviour {

    String oldInit;

    double newValueX;

    double newDeltaX;

    @Override
    public void action() {
        ACLMessage cancelMessage = new ACLMessage(ACLMessage.CANCEL);
        List<AID> agents = AgentService.findAgent(myAgent, "Agents");
        agents.forEach(aid -> cancelMessage.addReceiver(aid));
        Random randomizer = new Random();
        String newNameInit = oldInit;
        String localName = "";
        while (oldInit.equals(newNameInit)){
            AID aid = agents.get(randomizer.nextInt(agents.size()));
            newNameInit = aid.getName();
            localName = aid.getLocalName();
        }
        oldInit = newNameInit;
        log.info("System: смена инициатора на: "+localName);
        cancelMessage.setContent(newValueX+" "+newDeltaX+" "+oldInit);
        myAgent.send(cancelMessage);
    }
}
