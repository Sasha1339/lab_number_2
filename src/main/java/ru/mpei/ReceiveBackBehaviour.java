package ru.mpei;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ReceiveBackBehaviour extends Behaviour {

    double valueX;
    double deltaX;
    ResultY dataY = new ResultY();

    boolean isDone = false;

    public ReceiveBackBehaviour(double valueX, double deltaX) {
        this.valueX = valueX;
        this.deltaX = deltaX;
    }

    @Override
    public void onStart() {
        dataY.setResultY(0);
        dataY.setResultPlusY(0);
        dataY.setResultMinusY(0);
        dataY.setCountAgent(0);
        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
        message.setContent(valueX+" "+deltaX);
        List<AID> agents = AgentService.findAgent(myAgent, "Agents");
        agents.forEach(aid -> message.addReceiver(aid));
        log.info(myAgent.getLocalName() + ": Вводные данные по Х: " + valueX + ", по сдвигу: " + deltaX);
        log.info(myAgent.getLocalName() + ": Данные по Х: " + (valueX-deltaX) + " "+valueX+" "+(valueX+deltaX));
        myAgent.send(message);
    }

    @Override
    public void action() {
        ACLMessage backMessage =  myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.AGREE));
        if (backMessage != null) {
            String[] messageString = backMessage.getContent().split(" ");
            dataY.setResultMinusY(dataY.getResultMinusY() + Double.parseDouble(messageString[0]));
            dataY.setResultY(dataY.getResultY() + Double.parseDouble(messageString[1]));
            dataY.setResultPlusY(dataY.getResultPlusY() + Double.parseDouble(messageString[2]));
            dataY.setCountAgent(dataY.getCountAgent() + 1);
            if (dataY.getCountAgent() == AgentService.findAgent(myAgent, "Agents").size()){

                log.info("Суммарные данные по функциям: "+dataY.getResultMinusY()+
                        " "+dataY.getResultY()+
                        " "+dataY.getResultPlusY());
                System.out.println("/////////////////////////////////////////////////////////////" +
                        "///////////////////////////////////////////////////////////");
                if (dataY.getResultMinusY() >= dataY.getResultY()
                        && dataY.getResultMinusY() >= dataY.getResultPlusY()){
                    log.warn("Новые вводные данные по Х: "+(valueX-deltaX)+", по сдвигу: "+deltaX);
                    myAgent.addBehaviour(new QueueChangeBehaviour(myAgent.getName(), valueX-deltaX, deltaX));
                    isDone = true;
                } else if (dataY.getResultPlusY() >= dataY.getResultY()
                        && dataY.getResultPlusY() >= dataY.getResultMinusY()) {
                    log.warn("Новые вводные данные по Х: "+(valueX+deltaX)+", по сдвигу: "+deltaX);
                    myAgent.addBehaviour(new QueueChangeBehaviour(myAgent.getName(), valueX+deltaX, deltaX));
                    isDone = true;
                } else if (dataY.getResultY() >= dataY.getResultMinusY()
                        && dataY.getResultY() >= dataY.getResultPlusY()  && (deltaX*1000)%10 == 0){
                    log.warn("Обновление сдвига по Х (уменьшение в два раза) = "+deltaX/2);
                    deltaX /= 2;
                    this.onStart();
                } else {
                    log.info("Результат при котором наблюдается максимальный экстремум при х = "+valueX);
                    isDone = true;
                }

            }
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return isDone;
    }
}
