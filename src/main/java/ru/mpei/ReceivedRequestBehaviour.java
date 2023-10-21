package ru.mpei;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
@Slf4j
public class ReceivedRequestBehaviour extends Behaviour {

    boolean isDone = false;

    @Override
    public void action() {

        double resultX;
        double resultXMinesDelta;
        double resultXPlusDelta;
        double resultY;
        double resultYMinesDelta;
        double resultYPlusDelta;
        double newValueX;
        double newDeltaX;

        ACLMessage message = myAgent.receive();
        if (message != null){
            String[] valueXString = message.getContent().split(" ");
            if (valueXString.length != 2){
                resultXMinesDelta = Double.parseDouble(valueXString[5]);
                resultX = Double.parseDouble(valueXString[6]);
                resultXPlusDelta = Double.parseDouble(valueXString[7]);

                List<AID> agents = AgentService.findAgent(myAgent, "Agents");
                int indexMyAgent = agents.indexOf(myAgent.getAID());

                resultYMinesDelta = Equation.getValueY(resultXMinesDelta, indexMyAgent);
                resultY = Equation.getValueY(resultX, indexMyAgent);
                resultYPlusDelta = Equation.getValueY(resultXPlusDelta, indexMyAgent);

                log.info(message.getSender().getLocalName()+" отправил сообщение "+myAgent.getLocalName());
                log.info(myAgent.getLocalName()+": Я рассчитал функцию по значениям Х:"+" "+resultYMinesDelta+" "+resultY+" "+resultYPlusDelta);

                myAgent.addBehaviour(new SendAnswerBehaviour(resultYMinesDelta, resultY, resultYPlusDelta));
            }else{
                newValueX = Double.parseDouble(valueXString[0]);
                newDeltaX = Double.parseDouble(valueXString[1]);
                List<AID> agents = AgentService.findAgent(myAgent, "Agents");
                if (agents.indexOf(myAgent.getAID()) == AgentService.indexAgent){
                    myAgent.addBehaviour(new SendRequestBehaviour(newValueX, newDeltaX));
                    myAgent.addBehaviour(new ReceivedBackBehaviour(newValueX, newDeltaX));
                }else{
                    myAgent.addBehaviour(new ReceivedRequestBehaviour());
                }
                isDone = true;
            }
        }else{
            block();
        }
    }

    @Override
    public boolean done() {
        return isDone;
    }

}
