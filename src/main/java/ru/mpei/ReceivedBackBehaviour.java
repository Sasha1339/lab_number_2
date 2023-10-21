package ru.mpei;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ReceivedBackBehaviour extends Behaviour {
    public static double resultYMinesDelta = 0.0;
    public static double resultY = 0.0;
    public static double resultYPlusDelta = 0.0;
    public static int countAgent = 0;
    double valueX;
    double deltaX;

    boolean isDone = false;
    public ReceivedBackBehaviour(double valueX, double deltaX) {
        this.valueX = valueX;
        this.deltaX = deltaX;
    }



    @Override
    public void action() {
        ACLMessage message = myAgent.receive();
        if (message != null){
            String[] valueXString = message.getContent().split(" ");
            resultYMinesDelta += Double.parseDouble(valueXString[5]);
            resultY += Double.parseDouble(valueXString[6]);
            resultYPlusDelta += Double.parseDouble(valueXString[7]);
            countAgent += 1;

            if (countAgent == 3){
                log.info("Суммарные данные по функциям: "+resultYMinesDelta+" "+resultY+" "+resultYPlusDelta);
                if (resultYMinesDelta >= resultY && resultYMinesDelta >= resultYPlusDelta){
                    myAgent.addBehaviour(new QueueChangeBehaviour(valueX-deltaX, deltaX));
                    myAgent.addBehaviour(new ReceivedRequestBehaviour());
                    log.warn("Новые вводные данные по Х: "+(valueX-deltaX)+", по сдвигу: "+deltaX);
                    isDone = true;
                } else if (resultYPlusDelta >= resultY && resultYPlusDelta >= resultYMinesDelta) {
                    myAgent.addBehaviour(new QueueChangeBehaviour(valueX+deltaX, deltaX));
                    myAgent.addBehaviour(new ReceivedRequestBehaviour());
                    log.warn("Новые вводные данные по Х: "+(valueX+deltaX)+", по сдвигу: "+deltaX);
                    isDone = true;
                } else if (resultY >= resultYMinesDelta && resultY >= resultYPlusDelta && (deltaX*1000)%10 == 0){
                    myAgent.addBehaviour(new SendRequestBehaviour(valueX, deltaX/2));
                    myAgent.addBehaviour(new ReceivedBackBehaviour(valueX, deltaX/2));
                    log.warn("Обновление сдвига по Х (уменьшение в два раза) = "+deltaX/2);
                    isDone = true;
                } else {
                    log.info("Результат при котором наблюдается максимальный экстремум при х = "+valueX);
                    isDone = true;
                }
                System.out.println("//////////////////////////////////////////////////////////" +
                        "//////////////////////////////////////////////////////////////////////");
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
