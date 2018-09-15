import java.util.Random;

import Strategy;
import utils;

public class SimpleStrategy implements Strategy {
    private Population p;
    private int populationSize;
    private int childrenSize;
    private double mutationIndProb;
    private double crossoverProb;
    private double mutationDimProb;
    private double mutationSigma;

    private double[] s = p.getScores();
    private double[][] children = p.getChildren();
    private double[][] parents = p.getParents();
    private double[] selected = p.getSelected();
    private Random rnd_;

    public simpleStrategy(Population p, double mutationIndProb, double mutationDimProb, double mutationSigma,
            double crossoverProb) {
        populationSize = p.getPopulationSize();
        childrenSize = p.getchildrenSize();
        this.p = p;
        this.mutationIndProb = mutationIndProb;
        this.mutationDimProb = mutationDimProb;
        this.crossoverProb = crossoverProb;
        this.mutationSigma = mutationSigma;
        rnd_ = new Random();
        rnd_.setSeed(42);
        s = p.getScores();
        children = p.getChildren();
        parents = p.getParents();
        selected = p.getSelected();
    }

    // selection does two things:
    // 1. fill the selected int array with indices of selected children in the
    // parents array
    // 2. fill the children array with selected rows in the parents array
    // using SUS algo to select, details
    // in:https://www.wikiwand.com/en/Stochastic_universal_sampling
    @Override
    public void selection() {

        double fitness_sum = utils.sum(s);
        double slice = fitness_sum / childrenSize;
        // using start as the start point and the accumulate var in the loop below
        double start = rnd_.nextDouble() * slice;
        double tempSum = 0;
        int j = 0;
        for (int i = 0; i < populationSize;) {
            // while the accumulated fitness is still in current slice
            while (tempSum < start) {
                tempSum += s[i];
                i++;
            }
            if (i >= populationSize)
                break;
            selected[j] = i;
            children[j] = parents[i];
            j += 1;
            start += slice;
        }
        return;
    }

    // mutation adds a random gaussian noise whose sigma is provided to the
    // chosen children[i][j]. Then the number is clamped to [-5,5]. i is chosen by
    // mutationProb, j is chosen by
    // mutationDimProb, I want the randomness not only reflect on the individual
    // level but also on dim
    @Override
    public void mutation() {
        for (int i = 0; i < childrenSize; i++) {
            if (rnd_.nextDouble() < mutationIndProb) {
                for (int j = 0; j < DIM; j++) {
                    if (rnd_.nextDouble() < mutationDimProb) {
                        children[i][j] = utils.clamp(children[i][j] + rnd_.nextGaussian() * mutationSigma);
                    }
                }
            }
        }
    }

}