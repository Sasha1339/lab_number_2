package ru.mpei;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class SendRequestBehaviour extends OneShotBehaviour {
    double valueX ;
    double deltaX ;
    public SendRequestBehaviour(double valueX, double deltaX) {
        this.valueX = valueX;
        this.deltaX = deltaX;
    }
    @Override
    public void action() {

        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setContent("Calculate function of x : "+(valueX-deltaX)+" "+valueX+" "+(valueX+deltaX));
        log.info(myAgent.getLocalName()+": Я рассчитал значения Х с отклонениями: "+(valueX-deltaX)+" "+valueX+" "+(valueX+deltaX));
        List<AID> agents = AgentService.findAgent(myAgent, "Agents");
        agents.stream()
                .filter(agent -> agents.indexOf(agent) != AgentService.indexAgent)
                .forEach(aid -> message.addReceiver(aid));

        ReceivedBackBehaviour.resultYMinesDelta = Equation.getValueY(valueX-deltaX, AgentService.indexAgent);
        ReceivedBackBehaviour.resultY = Equation.getValueY(valueX, AgentService.indexAgent);
        ReceivedBackBehaviour.resultYPlusDelta = Equation.getValueY(valueX+deltaX, AgentService.indexAgent);

        log.info(myAgent.getLocalName()+": Я рассчитал функцию по значениям Х:"+" "+ReceivedBackBehaviour.resultYMinesDelta+
                " "+ReceivedBackBehaviour.resultY+" "+ReceivedBackBehaviour.resultYPlusDelta);

        ReceivedBackBehaviour.countAgent = 1;

        myAgent.send(message);
    }


}
