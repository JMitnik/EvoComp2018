package com;

import java.util.ArrayList;
import org.vu.contest.ContestEvaluation;
import org.ejml.simple.*;


public class Population {
        private static int DIM = 10;
        // (populationSize, DIM), normalized samples
        private SimpleMatrix yi;
        // (populationSize, DIM), unnormalized samples
        private SimpleMatrix xi;
        // (topmu, DIM), topmu scored inds
        private SimpleMatrix yimu;
        // (1, DIM), mean of the topmu scored inds
        private SimpleMatrix yw;
        // of length topmu, indices of the topmu inds in the sorted population according to scores
        private int[] indices;
        // of length populationSize, probably used
        private double[] scores;

        private int populationSize;
        private int topmu;
        public Population(int populationSize, int topmu, ContestEvaluation e, SimpleMatrix m, SimpleMatrix C,
                        double sigma) {
                this.populationSize = populationSize;
                this.topmu = topmu;
                // 1. sample the matrix yi, xi, first yi then xi

                // 2. evaluate the matrix xi, get the `score`, then sort it to `indices`, 
                // probably use the heap sort since we only need the topK

                // 3. extract the topK inds into `yimu` for later use

                // 4. calc the `yw` to be the mean of `yimu`
        }

        public SimpleMatrix getYw() {
                return yw;
        }

        public SimpleMatrix getYimu() {
                return yimu;
        }
        // mean vector along the first dim
        private SimpleMatrix mean(SimpleMatrix a) {
                SimpleMatrix ones = new SimpleMatrix(a.numCols, 1);
                SimpleMatrix sum = a.mult(ones);
                return sum.divide(a.getRows);
        }
}
