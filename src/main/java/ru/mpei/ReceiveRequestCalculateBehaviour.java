package ru.mpei;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ReceiveRequestCalculateBehaviour extends Behaviour {

    String nameInit;

    boolean isDone = false;

    public ReceiveRequestCalculateBehaviour(String nameInit) {
        this.nameInit = nameInit;
    }

    @Override
    public void action() {
        ACLMessage changeMessage = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.CANCEL));
        ACLMessage receivedMessage =  myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
        if (receivedMessage != null) {
            String[] messageString = receivedMessage.getContent().split(" ");
            double valueX = Double.parseDouble(messageString[0]);
            double deltaX = Double.parseDouble(messageString[1]);
            List<AID> agents = AgentService.findAgent(myAgent, "Agents");
            double resultMinusY = Equation.getValueY(valueX - deltaX, myAgent.getLocalName(), agents);
            double resultY = Equation.getValueY(valueX, myAgent.getLocalName(), agents);
            double resultPlusY = Equation.getValueY(valueX + deltaX, myAgent.getLocalName(), agents);
            log.info(myAgent.getLocalName()+": Мои значения по y: "+resultMinusY+" "+resultY+" "+resultPlusY);
            myAgent.addBehaviour(new SendMyAnswerBehaviour(resultMinusY, resultY, resultPlusY, nameInit));
        } else if (changeMessage != null){
            String[] messageString = changeMessage.getContent().split(" ");
            double valueX = Double.parseDouble(messageString[0]);
            double deltaX = Double.parseDouble(messageString[1]);
            String newInit = messageString[2];
            if (myAgent.getName().equals(newInit)){
                myAgent.addBehaviour(new ReceiveBackBehaviour(valueX, deltaX));
            }
            myAgent.addBehaviour(new ReceiveRequestCalculateBehaviour(newInit));
            isDone = true;
        } else {
            block();
        }

    }

    @Override
    public boolean done() {
        return isDone;
    }

}
