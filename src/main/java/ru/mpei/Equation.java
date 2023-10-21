package ru.mpei;

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

    public static double getValueY(double valueX, int indexAgent){
        if (indexAgent == 0)
            return Equation.equationAgentOne(valueX);
        else if (indexAgent == 1)
            return Equation.equationAgentTwo(valueX);
        else
            return Equation.equationAgentThree(valueX);
    }

}
