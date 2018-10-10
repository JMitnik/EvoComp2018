package com;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import org.vu.contest.ContestEvaluation;
import org.ejml.simple.*;
import com.MUtils.*;
import com.auxComparator;

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
        // mean of cov of each ind, for rank-\mu update
        private SimpleMatrix rankmuUpdate;
        
        // of length topmu, indices of the topmu inds in the sorted population according
        // to scores
        private Integer[] indices;
        // of length populationSize, probably used
        private double[] scores;

        private SimpleMatrix[] yiCache;

        private int populationSize;
        private int topmu;

        private Random rnd_;

        public Population(int populationSize, int topmu, ContestEvaluation e, SimpleMatrix m, SimpleMatrix C,
                        double sigma) {
                this.populationSize = populationSize;
                this.topmu = topmu;
                this.rnd_ = new Random(42);
                yi = new SimpleMatrix(populationSize, DIM);
                xi = new SimpleMatrix(populationSize, DIM);
                yiCache = new SimpleMatrix[populationSize];
                // 1. sample the matrix yi, xi, first yi then xi
                scores = new double[populationSize];
                for (int i = 0; i < populationSize; i++) {
                        SimpleMatrix tempV = MUtils.randomNormal(C, rnd_);
                        MUtils.setValuesInBulk(i, 0, yi, tempV);
                        yiCache[i] = tempV;
                        SimpleMatrix tempXi = MUtils.plus(sigma, tempV, m);
                        MUtils.setValuesInBulk(i, 0, xi, tempXi);
                        double c = (double) e.evaluate(tempXi.getDDRM().getData());
                        scores[i] = c;
                }

                // 2. evaluate the matrix xi, get the `score`, then sort it to
                // `indices`, probably use the heap sort since we only need
                // the topK
                indices = sortByIndex(scores);
                yw = new SimpleMatrix(1, DIM);
                rankmuUpdate = new SimpleMatrix(DIM, DIM);
                

                // 3. calc the `yw` to be the mean of `yimu`, and rankmuUpdate
                for (int i = 0; i < topmu; i++) {
                        SimpleMatrix pres = yiCache[indices[populationSize - 1 - i]];
                        yw = meanIncremental(yw, pres, i);
                        rankmuUpdate = meanIncremental(rankmuUpdate, MUtils.getSymmetricM(pres), i);
                }

        }

        public SimpleMatrix getYw() {
                return yw;
        }

        @Deprecated
        public SimpleMatrix getYimu() {
                return yimu;
        }

        public SimpleMatrix getRankmuUpdate() {
                return rankmuUpdate;
        }

        // mean vector along the first dim
        @Deprecated
        private SimpleMatrix mean(SimpleMatrix a) {
                SimpleMatrix ones = new SimpleMatrix(1, a.numRows());
                ones.fill(1.0);
                SimpleMatrix sum = ones.mult(a);
                return sum.divide(a.numRows());
        }

        private SimpleMatrix meanIncremental(SimpleMatrix a, SimpleMatrix b, double i) {
                return a.scale(i / (i + 1)).plus(1 / (i + 1), b);
        }

        private Integer[] sortByIndex(double[] scores) {
                Integer[] idx = range(0, scores.length);
                Arrays.sort(idx, new auxComparator(scores));
                return idx;
        }

        private Integer[] range(int start, int stop) {
                Integer[] result = new Integer[stop - start];

                for (int i = 0; i < stop - start; i++)
                        result[i] = start + i;

                return result;
        }

        public static void main(String args[]) {
                Random rnd_ = new Random(42);
                SimpleMatrix mm = MUtils.randomDDRM(1, DIM, -5, 5, rnd_);
                SimpleMatrix mC = MUtils.diag(DIM);
                double sigma = 1.0;
                // Population test = new Population(3, 2, mm, mC, sigma);
        }
}
