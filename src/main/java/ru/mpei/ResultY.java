package ru.mpei;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResultY {

    private double resultMinusY;
    private double resultY;
    private double resultPlusY;
    private int countAgent;

}
