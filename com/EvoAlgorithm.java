package com;

import java.util.*;
import com.*;
import org.vu.contest.ContestEvaluation;

public class EvoAlgorithm {
        private ContestEvaluation e;
        private double p_mutation;
        private double p_crossover;
        private int popsize;
        private int dim;
        private Population population;
        private Population parent1;
        private Population parent2;
        private Population offspring;
        private Population survivors;
        private int curEvals;
        private int sizeOfT;
        private int evals;
        private int eval_limits;
        private double msigma;
        private double mixRate;

        public EvoAlgorithm(Population population, ContestEvaluation e, double p_mutation, double p_crossover,
                        int eval_limits, int sizeOfT, double mixRate, double msigma) {
                this.population = population;
                this.e = e;
                this.p_mutation = p_mutation;
                this.p_crossover = p_crossover;
                this.popsize = population.getPopulation().size();
                this.dim = population.getGeneFori(0).length;
                this.eval_limits = eval_limits;
                this.sizeOfT = sizeOfT;
                this.mixRate = mixRate;
                this.msigma = msigma;
                evals = 0;
                curEvals = 0;
        }

        // Proportionate Roulette Wheel Selection
        public void Select_RW() {

                // get the minimum fitness
                double min = population.getFitnessFori(0);
                for (int i = 1; i < popsize; i++) {
                        Individual ind = population.get(i);
                        if (ind.getFitness() < min) {
                                min = ind.getFitness();
                        }
                }
                // get the summation
                double sum = 0;
                for (int i = 0; i < popsize; i++) {
                        Individual ind = population.get(i);
                        sum += (ind.getFitness() - min);
                }
                if (sum == 0)
                        return;
                this.parent1 = new Population();
                this.parent2 = new Population();
                Random random = new Random();
                for (int i = 0; i < popsize / 2; i++) {
                        double rnd = random.nextDouble();
                        double sum_p = 0.0;
                        for (int j = 0; j < popsize; j++) {
                                Individual ind = this.population.get(j);
                                sum_p += (ind.getFitness() - min) / sum;
                                if (sum_p >= rnd) {
                                        Individual tmp = new Individual();
                                        tmp.setGenes(ind.getGenes());
                                        tmp.setFitness(ind.getFitness());
                                        parent1.addIndividual(tmp);
                                        break;
                                }
                        }
                }
                for (int i = 0; i < popsize / 2; i++) {
                        double rnd = random.nextDouble();
                        double sum_p = 0.0;
                        for (int j = 0; j < popsize; j++) {
                                Individual ind = population.get(j);
                                sum_p += (ind.getFitness() - min) / sum;
                                if (sum_p >= rnd) {
                                        Individual tmp = new Individual();
                                        tmp.setGenes(ind.getGenes());
                                        tmp.setFitness(ind.getFitness());
                                        parent2.addIndividual(tmp);
                                        break;
                                }
                        }
                }
        }

        // Tournament Selection
        public void Select_TS(int tsize) {
                Random random = new Random();
                parent1 = new Population();
                parent2 = new Population();
                for (int i = 0; i < popsize / 2; i++) {
                        Population Tournament = new Population();
                        for (int j = 0; j < tsize; j++) {
                                Individual rnd = this.population.get(random.nextInt(popsize));
                                Tournament.addIndividual(rnd);
                        }
                        Collections.sort(Tournament.getPopulation(), Collections.reverseOrder());
                        Individual tmp = new Individual();
                        tmp.setGenes(Tournament.getGeneFori(0));
                        tmp.setFitness(Tournament.getFitnessFori(0));
                        parent1.addIndividual(tmp);
                }
                for (int i = 0; i < popsize / 2; i++) {
                        Population Tournament = new Population();
                        for (int j = 0; j < tsize; j++) {
                                Individual rnd = this.population.get(random.nextInt(popsize));
                                Tournament.addIndividual(rnd);
                        }
                        Collections.sort(Tournament.getPopulation(), Collections.reverseOrder());
                        Individual tmp = new Individual();
                        tmp.setGenes(Tournament.getGeneFori(0));
                        tmp.setFitness(Tournament.getFitnessFori(0));
                        parent2.addIndividual(tmp);
                }
        }

        // Simple arithmetic crossover
        public void Crossover_SA(double alpha) {
                Random random = new Random();
                offspring = new Population();
                for (int p = 0; p < popsize / 2; p++) {
                        Individual dad = parent1.get(p);
                        Individual mom = parent2.get(p);
                        if (random.nextDouble() > p_crossover) {
                                Individual offspring1 = new Individual(dad.getGenes());
                                Individual offspring2 = new Individual(mom.getGenes());
                                offspring.addIndividual(offspring1);
                                offspring.addIndividual(offspring2);
                        } else {
                                double genes1[] = dad.getGenes();
                                double genes2[] = mom.getGenes();
                                int pos = random.nextInt(dim);
                                // generate new genes
                                double new_genes1[] = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
                                double new_genes2[] = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
                                for (int i = 0; i < dim; i++) {
                                        if (i < pos) {
                                                new_genes1[i] = genes1[i];
                                                new_genes2[i] = genes2[i];
                                        } else {
                                                new_genes1[i] = alpha * genes2[i] + (1 - alpha) * genes1[i];
                                                new_genes2[i] = alpha * genes1[i] + (1 - alpha) * genes2[i];
                                        }
                                }
                                Individual offspring1 = new Individual(new_genes1);
                                Individual offspring2 = new Individual(new_genes2);
                                offspring.addIndividual(offspring1);
                                offspring.addIndividual(offspring2);
                        }
                }
        }

        // Whole arithmetic crossover
        public void Crossover_WA(double alpha) {
                Random random = new Random();
                offspring = new Population();
                for (int p = 0; p < popsize / 2; p++) {
                        Individual dad = parent1.get(p);
                        Individual mom = parent2.get(p);
                        if (random.nextDouble() > p_crossover) {
                                Individual offspring1 = new Individual(dad.getGenes());
                                Individual offspring2 = new Individual(mom.getGenes());
                                offspring.addIndividual(offspring1);
                                offspring.addIndividual(offspring2);
                        } else {
                                double genes1[] = dad.getGenes();
                                double genes2[] = mom.getGenes();
                                int pos = random.nextInt(dim);
                                // generate new genes
                                double new_genes1[] = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
                                double new_genes2[] = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
                                for (int i = 0; i < dim; i++) {
                                        new_genes1[i] = alpha * genes2[i] + (1 - alpha) * genes1[i];
                                        new_genes2[i] = alpha * genes1[i] + (1 - alpha) * genes2[i];
                                }
                                Individual offspring1 = new Individual(new_genes1);
                                Individual offspring2 = new Individual(new_genes2);
                                offspring.addIndividual(offspring1);
                                offspring.addIndividual(offspring2);
                        }
                }
        }

        public void Mutation(double sigma) {
                Random random = new Random();
                for (int i = 0; i < popsize; i++) {
                        if (random.nextDouble() <= p_mutation) {
                                Individual ind = offspring.get(i);
                                double genes[] = ind.getGenes();
                                for (int j = 0; j < dim; j++) {
                                  genes[j] = Utils.clamp(
                                      genes[j] + random.nextGaussian() * sigma, 5, -5);
                                }
                                offspring.get(i).setGenes(genes);
                        }
                }
        }

        public void Survive() {
                Population pop_all = new Population();
                survivors = new Population();
                for (int i = 0; i < population.getPopulation().size(); i++) {
                        Individual tmp = new Individual();
                        tmp.setGenes(population.getGeneFori(i));
                        tmp.setFitness(population.getFitnessFori(i));
                        pop_all.addIndividual(tmp);
                }
                for (int i = 0; i < offspring.getPopulation().size(); i++) {
                        Individual tmp = new Individual();
                        tmp.setGenes(offspring.getGeneFori(i));
                        tmp.setFitness(offspring.getFitnessFori(i));
                        pop_all.addIndividual(tmp);
                }
                Collections.sort(pop_all.getPopulation(), Collections.reverseOrder());
                for (int i = 0; i < population.getPopulation().size(); i++) {
                        survivors.addIndividual(pop_all.get(i));
                }
                this.population = survivors;
        }

        public void run() {
                double realSigma = msigma;
                while (evals < eval_limits) {
                        // ShowBestFitness();
                        // Select parents
                        // Select_RW(); //Roulette Wheel Selection
                        Select_TS(sizeOfT); // Tournament Selection(size of the Tournament)
                        // Apply crossover / mutation operators
                        // Crossover_SA(0.5); // Simple arithmetic crossover
                        Crossover_WA(mixRate); // Whole arithmetic crossover
                        Mutation(realSigma);   // randomGauss(sigma)
                        realSigma = Utils.exponentialDecay(msigma, evals, eval_limits);
                        // Check fitness of unknown fuction
                        for (int i = 0; i < offspring.size(); i++) {
                                Individual child = offspring.get(i);
                                double fitness = (double) e.evaluate(child.getGenes());
                                offspring.setFitnessFori(i, fitness);
                                evals++;
                        }
                        setOffspring(offspring);
                        // Select survivors
                        Survive();
                }
        }

        public void ShowFitness(Population pop, String s) {
                int popsize = pop.getPopulation().size();
                System.out.println(s);
                for (int i = 0; i < popsize; i++) {
                        System.out.print(pop.get(i).getFitness() + " ");
                }
                System.out.println();
        }

        public void ShowBestFitness() {
                double max = population.getFitnessFori(0);
                for (int i = 1; i < popsize; i++) {
                        double tmp = population.get(i).getFitness();
                        if (tmp > max) {
                                max = tmp;
                        }
                }
                System.out.println("Max:" + max);
        }

        public Population getPopulation() {
                return population;
        }

        public void setPopulation(Population population) {
                this.population = population;
        }

        public Population getOffspring() {
                return offspring;
        }

        public void setOffspring(Population offspring) {
                this.offspring = offspring;
        }

        public Population getSurvivors() {
                return survivors;
        }

        public void setSurvivors(Population survivors) {
                this.survivors = survivors;
        }
}