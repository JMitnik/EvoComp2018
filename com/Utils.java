package com;

import java.lang.Math;

class Utils{
    public static double clamp(double x, double upperBound, double lowerBound) {
        return Math.min(Math.max(x, lowerBound), upperBound);        
    }

    public static double exponentialDecay(double originValue, int timestep, int finalStep) {
        return (Math.pow(2.71, -timestep / finalStep) - 1 / 2.71) / (1 - 1 / 2.71) * originValue;
    }
    public static double linearDecay(double originValue, int timestep, int finalStep) {
        return (1-timestep/finalStep)*originValue;
    }
}