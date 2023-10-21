package ru.mpei;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.List;

public class SendAnswerBehaviour extends OneShotBehaviour {

    double resultY;
    double resultYMinesDelta;
    double resultYPlusDelta;

    public SendAnswerBehaviour(double resultYMinesDelta, double resultY, double resultYPlusDelta) {
        this.resultYMinesDelta = resultYMinesDelta;
        this.resultY = resultY;
        this.resultYPlusDelta = resultYPlusDelta;
    }

    @Override
    public void action() {
        ACLMessage sendMessage = new ACLMessage(ACLMessage.REQUEST);
        List<AID> agents = AgentService.findAgent(myAgent, "Agents");
        agents.stream()
                .filter(agent -> agents.indexOf(agent) == AgentService.indexAgent)
                .forEach(aid -> sendMessage.addReceiver(aid));
        sendMessage.setContent(myAgent.getLocalName()+" function of y : "+resultYMinesDelta+" "+resultY+" "+resultYPlusDelta);
        myAgent.send(sendMessage);

    }
}
