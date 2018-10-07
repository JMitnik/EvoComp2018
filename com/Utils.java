package com;
import java.lang.Math;
public class Utils {
    public static double clamp(double x, double upperBound, double lowerBound) {
        return Math.min(Math.max(x, lowerBound), upperBound);
    }

    public static double exponentialDecay(double originValue, int timestep, int finalStep) {
        return (Math.pow(2.71, -timestep / finalStep) - 1 / 2.71) / (1 - 1 / 2.71) * originValue;
    }

    public static double linearDecay(double originValue, int timestep, int finalStep) {
        return (1 - timestep / finalStep) * originValue;
    }

    public static Individual partner(Individual[] dadAndMom, Individual ppl) {
        if (ppl == dadAndMom[0])
            return dadAndMom[1];
        else
            return dadAndMom[0];
    }
    public static String FormatGene(double[] child){
        String geneString="";
        for(int i=0;i<10;i++){
           geneString+=(String.format("%.4g%n",child[i]));
           // formated_child[i]=child[i];
        }
        return geneString;
    }
}
