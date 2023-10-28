package ru.mpei;

import jade.core.AID;

import java.util.List;

public class Equation {
    public static double equationAgentOne(double valueX){
        return -1*Math.pow(valueX, 2)+5;
    }
    public static double equationAgentTwo(double valueX){
        return 2*valueX+2;
    }
    public static double equationAgentThree(double valueX){
        return Math.sin(valueX);
    }

    public static double getValueY(double valueX, String nameAgent, List<AID> agents){
            if (agents.get(0).getLocalName().equals(nameAgent))
                return Equation.equationAgentOne(valueX);
            else if (agents.get(1).getLocalName().equals(nameAgent))
                return Equation.equationAgentTwo(valueX);
            else
                return Equation.equationAgentThree(valueX);
    }

}
