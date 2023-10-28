package ru.mpei;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.util.Arrays;
import java.util.List;

public class AgentService {
    public static void registerAgent(Agent agent, String serviceType){
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        dfAgentDescription.setName(agent.getAID());

        ServiceDescription description = new ServiceDescription();
        description.setName("Service");
        description.setType(serviceType);

        dfAgentDescription.addServices(description);

        try {
            DFService.register(agent, dfAgentDescription);
        } catch (FIPAException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<AID> findAgent(Agent agent, String serviceName){
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType(serviceName);
        dfAgentDescription.addServices(serviceDescription);

        try {
            return Arrays.stream(DFService.search(agent, dfAgentDescription))
                    .map(dfAgentDescription1 -> dfAgentDescription1.getName()).toList();
        } catch (FIPAException e) {
            throw new RuntimeException(e);
        }
    }


}
