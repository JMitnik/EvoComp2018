package com;

import java.util.*;
import com.*;
import org.vu.contest.ContestEvaluation;
import org.ejml.simple.*;
import com.sampleUtilityClassForEJML.*;

public class EvoAlgorithm {
        private static int DIM = 10;
        private static double gammaExp = 3.084327759799864;
        private ContestEvaluation e;
        // \lambda
        private int populationSize;
        private Population population;

        // c_c
        private double cc = 4 / DIM;
        // c_sigma
        private double csigma = 4 / DIM;
        // c_1
        private double c1 = 2 / DIM / DIM;

        // c_\mu
        private double cmu;
        // d_\sigma
        private double dsigma;
        // sigma
        private double sigma;
        // $\mu_w$
        private double muw;

        // \mu
        private int topmu;
        private int evals;
        private int eval_limits;
        private SimpleMatrix m;
        private SimpleMatrix pc;
        private SimpleMatrix psigma;
        private SimpleMatrix C;

        private Random rnd_;

        public EvoAlgorithm(ContestEvaluation e, int populationSize, double topRatio, int eval_limits) {
                this.e = e;
                this.populationSize = populationSize;
                this.eval_limits = eval_limits;
                this.topmu = populationSize * topRatio;
                evals = 0;
                this.eval_limits = eval_limits;

                rnd_ = new Random(42);
                m = randomDDRM(1, DIM, -5, 5, rnd_);
                pc = new SimpleMatrix(1, DIM);
                muw = topmu;
                psigma = new SimpleMatrix(1, DIM);
                cmu = muw / DIM / DIM;
                dsigma = 1 + Math.sqrt(muw / DIM);
                C = diag(DIM);
                sigma = 1.;

                population = null;
        }

        // xi = m + σ yi, yi ∼ Ni(0, C), for i = 1, . . . , λ sampling
        // the side effect is the population will do the sort automatically
        public void sampleNewGen() {
                // all the calc about the population, such the statistics, should be calculated eagerly when it is constructed
                // the reason to do so is to keep the tidyness of the population api and the fact that the population
                // will not be modified in this iter and will be substituted the next iter
                
                // task 1 for population: calc the y_i, cache it, then calc the x_i
                population = new Population(populationSize, topmu, e, m, C, sigma);
        }

        // m ← \sum_ {i=1}^\mu wi * x_{i:λ} = m + σy_w where y_w = \sum_
        // {i=1}^\mu*wi y_{i:λ} 
        // update mean
        public void updateMean() {
                // extract the topmu inds from `population`
                // I personally recommend to use the y_w, instead of use the original x_{1:\lambda}
                // because the later calculation only requires y_w
                // since we adopt simple w_i, i.e. \frac{1}{\mu} for the topmu inds and 0 for others.
                // we can simply use the mean of the first internel y_{i:\lambda}, 
                // and the mean should be calculated in the construction phase of population

                // task 2 for population: evaluate the pop, then sort it, extract the first
                // topmu inds, we are gonna use the topmu ind matrix later
                // task 3 for population: calc the y_w, which will be used multiple times below
        }

        // cumulation for C
        public void evolutionPathForC() {
                // the same as before, the only thing you are gonna use about the present population is y_w
                // might need to use the norm of the matrix, the method is SimpleMatrix.normF()
        }
        // cumulation for \sigma
        public void evolutionPathForSigma() {
                // the same as before, the only thing you are gonna use about the present population is y_w
                // might need to use the C^{\frac{1}{2}}, since we are not gonna use it anymore in this iter,
                // I guess we dont need to store the result as a special variable
                // TODO: Implement an operator to calc C^{\frac{1}{2}} in utility class
        }

        // update C
        public void updateCovariance() {
                // will mainly use three operator, transpose, matrix product, and mean wrt the first axi

                // task 4 for population: extract the first topmu inds, which has been done in task 2
                
        }
        // update of σ
        public void updateSigma() {
                // will use the norm operator
                // a side note: E\left\norm \mathscr{N}(0, I) \right\norm is √2 Γ((n+1)/2)/Γ(n/2), 
                // the val has been calced beforehand, stored in `gammaExp`
        }


        public void run() {
                while (evals < eval_limits) {
                        sampleNewGen();
                        evals -= populationSize;
                        updateMean();
                        evolutionPathForC();
                        evolutionPathForSigma();
                        updateCovariance();
                        updateSigma();
                }
        }

        public Population getPopulation() {
                return population;
        }

        public void setPopulation(Population population) {
                this.population = population;
        }

}
