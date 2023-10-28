package ru.mpei;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.List;

public class SendMyAnswerBehaviour extends OneShotBehaviour {

    double resultMinusY;
    double resultY;
    double resultPlusY;

    String nameInit;

    public SendMyAnswerBehaviour(double resultMinusY, double resultY, double resultPlusY, String nameInit) {
        this.resultMinusY = resultMinusY;
        this.resultY = resultY;
        this.resultPlusY = resultPlusY;
        this.nameInit = nameInit;
    }

    @Override
    public void action() {
        ACLMessage message = new ACLMessage(ACLMessage.AGREE);
        List<AID> agents = AgentService.findAgent(myAgent, "Agents");
        agents.stream()
                .filter(aid -> aid.getName().equals(nameInit))
                .forEach(aid -> message.addReceiver(aid));
        message.setContent(resultMinusY+" "+resultY+" "+resultPlusY);
        myAgent.send(message);
    }
}
