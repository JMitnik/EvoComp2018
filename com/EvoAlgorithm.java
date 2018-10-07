package com;

import java.util.*;
import com.*;
import org.vu.contest.ContestEvaluation;

public class EvoAlgorithm {
private static int DIM = 10;
private ContestEvaluation e;
private double pIndMutationProb;
private double pDimMutationProb;
private double crossoverIndProb;
private double crossoverDimProb;
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

public EvoAlgorithm(Population population, ContestEvaluation e, double pIndMutationProb,
                    double pDimMutationProb, double crossoverIndProb, double crossoverDimProb, int eval_limits,
                    int sizeOfT, double mixRate, double msigma) {
        this.population = population;
        this.e = e;
        this.pIndMutationProb = pIndMutationProb;
        this.pDimMutationProb = pDimMutationProb;
        this.crossoverIndProb = crossoverIndProb;
        this.crossoverDimProb = crossoverDimProb;
        this.popsize = population.getPopulation().size();
        this.dim = population.getGeneFori(0).length;
        this.eval_limits = eval_limits;
        this.sizeOfT = sizeOfT;
        this.mixRate = mixRate;
        this.msigma = msigma;
        evals = 0;
        curEvals = 0;
}
/**
 * Tournament Selection
 *
 * @pre -
 * @post Parent1 and parent2 have now received a selection of best-fit
 *       individuals
 * @param tsize
 */
public void Select_TS(int tsize) {
        Random random = new Random();
        parent1 = new Population();
        parent2 = new Population();

        // TODO: use heap sort instead of full qsort; merge two loop together
        Population[] tmpArray = { parent1, parent2 };

        // For each parent population as tmpPopulation
        for (Population tmpPopulation : tmpArray) {

                // For the half of the population size
                for (int i = 0; i < popsize / 2; i++) {
                        Population Tournament = new Population();

                        // TODO: Check if we take members without replacement always or also
                        // ocassionally could grab the same one for the same tournament
                        // Grab members for the Tournament
                        for (int j = 0; j < tsize; j++) {
                                Individual rnd = this.population.get(random.nextInt(popsize));
                                Tournament.addIndividual(rnd);
                        }

                        // Sort the tournament in reverse order, so that you have highest fitness first.
                        Collections.sort(Tournament.getPopulation(), Collections.reverseOrder());

                        // Create a temporary clone with the same genes and fitness to the parent
                        Individual tmp = new Individual();
                        tmp.setGenes(Tournament.getGeneFori(0));
                        tmp.setFitness(Tournament.getFitnessFori(0));
                        tmp.setSigmas(Tournament.getSigmasFori(0));
                        tmpPopulation.addIndividual(tmp);
                }
        }
}

public void Crossover_novel(double alpha) {
        Random random = new Random();
        offspring = new Population();
        Individual cand = null;
        for (int p = 0; p < popsize; p++) {
                if (random.nextDouble() > crossoverIndProb) {
                        if (cand == null)
                                cand = parent1.get(p);
                }

        }
}

// Simple arithmetic crossover
public void Crossover_SA(double alpha) {
        // Initialize random number
        Random random = new Random();
        offspring = new Population();
        for (int p = 0; p < popsize / 2; p++) {
                Individual dad = parent1.get(p);
                Individual mom = parent2.get(p);
                Individual[] dadAndMom = { dad, mom };
                if (random.nextDouble() > crossoverIndProb) {
                        for (Individual ind : dadAndMom) {
                                Individual offs = new Individual(ind.getGenes());
                                offspring.addIndividual(offs);
                        }
                        // Individual offspring2 = new Individual(mom.getGenes());
                        // offspring.addIndividual(offspring2);
                } else {
                        for (Individual ind : dadAndMom) {

                                double singleGenes1[] = ind.getGenes();
                                Individual partner = Utils.partner(dadAndMom, ind);
                                // int pos = random.nextInt(dim);
                                double new_genes1[] = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
                                for (int i = 0; i < DIM; i++) {

                                        if (random.nextDouble() < crossoverDimProb) {
                                                double rndNum = random.nextDouble();
                                                new_genes1[i] = singleGenes1[i] * rndNum
                                                                + partner.getGenes()[i] * (1 - rndNum);
                                        } else {
                                                new_genes1[i] = singleGenes1[i];
                                        }
                                }
                                Individual offspring1 = new Individual(new_genes1);
                                offspring.addIndividual(offspring1);
                        }
                }

        }
}

/**
 * Crossover_WA Gets offspring by averaging over parent's alleles using alpha
 *
 * @pre - Parents exist in population with defined gene-set.
 * @post New children have been added to the offspring population with a mixture
 *       genes of their parents.
 */
public void Crossover_WA(double alpha) {
        // TODO: Get rid of alpha?
        Random random = new Random();
        offspring = new Population();

        for (int p = 0; p < popsize / 2; p++) {
                Individual dad = parent1.get(p);
                Individual mom = parent2.get(p);

                if (random.nextDouble() > crossoverIndProb) {
                        Individual offspring1 = new Individual(dad.getGenes());
                        Individual offspring2 = new Individual(mom.getGenes());
                        offspring.addIndividual(offspring1);
                        offspring.addIndividual(offspring2);
                        // Else, apply crossover
                } else {
                        double genes1[] = dad.getGenes();
                        double genes2[] = mom.getGenes();
                        double sigmas1[]=dad.getSigmas();
                        double sigmas2[]=mom.getSigmas();

                        // Get random position of alleles
                        int pos = random.nextInt(dim);
                        // generate new genes
                        double new_genes1[] = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
                        double new_genes2[] = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
                        double new_sigmas1[] = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
                        double new_sigmas2[] = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
                        // For each allele, set genes of child 1 to be rndNum of parentX, and combined
                        // with 1-rndNum of parentY
                        for (int i = 0; i < dim; i++) {
                                double rndNum = random.nextDouble();
                                rndNum=alpha;               //seems like using alpha is better than random number
                                //espacially for Multimodals
                                new_genes1[i] = rndNum * genes2[i] + (1 - rndNum) * genes1[i];
                                new_genes2[i] = rndNum * genes1[i] + (1 - rndNum) * genes2[i];
                                // new_sigmas1[i] = rndNum * sigmas2[i] + (1 - rndNum) * sigmas1[i];
                                // new_sigmas2[i] = rndNum * sigmas1[i] + (1 - rndNum) * sigmas2[i];

                        }

                        // Create offsprings with these genes and add as offspring
                        Individual offspring1 = new Individual(new_genes1);
                        Individual offspring2 = new Individual(new_genes2);
                        offspring1.setSigmas(sigmas1);
                        offspring2.setSigmas(sigmas2);
                        offspring.addIndividual(offspring1);
                        offspring.addIndividual(offspring2);
                }
        }
}

/**
 * Applies Non-Unfirom Mutation
 *
 * @pre Current generation has spawned offspring through crossover (or cloned itself).
 * @post Offspring now have had a 'p_mutatuion' to mutate themselves based on a gaussian distribution
 */
public void nonUniformMutation(double sigma) {
        Random random = new Random();
        // For each individual
        for (int i = 0; i < popsize; i++) {
                if (random.nextDouble() <= pIndMutationProb) {
                        Individual ind = offspring.get(i);
                        ind.setChanged();
                        double genes[] = ind.getGenes();

                        // For all alleles of their chromosome
                        for (int j = 0; j < dim; j++) {
                                // If pDImensionMutation
                                if (random.nextDouble() < pDimMutationProb) {
                                        // Set their genes between 5, -5, and add to their genes some value from
                                        // gaussian distribution
                                        genes[j] = Utils.clamp(genes[j] + random.nextGaussian() * sigma, 5, -5);
                                }
                        }
                        ind.setGenes(genes);
                }
        }
}
/**
 * Applies Uncorrelated Mutation with n sigmas
 *
 * @author Robbie
 */
public void UncorrelatedMutation(){
        Random random = new Random();
        // For each individual
        for (int i = 0; i < popsize; i++) {
                if (random.nextDouble() <= pIndMutationProb) {
                        Individual ind = offspring.get(i);
                        ind.setChanged();
                        double genes[]  = ind.getGenes();
                        double sigmas[] = ind.getSigmas();
                        int n=popsize;
                        double T=1/Math.sqrt(2*Math.sqrt(n));
                        double T_prime=1/Math.sqrt(2*n);
                        // For all alleles of their chromosome
                        for (int j = 0; j < dim; j++) {
                                // If pDImensionMutation
                                if (random.nextDouble() < pDimMutationProb) {
                                        //Mutate sigma
                                        sigmas[j]=sigmas[j]*(Math.exp(T*random.nextGaussian()+T_prime*random.nextGaussian()));
                                        // Set their genes between 5, -5, and add to their genes some value from
                                        // gaussian distribution
                                        genes[j] = Utils.clamp(genes[j] + random.nextGaussian() * sigmas[j], 5, -5);
                                }
                        }
                        ind.setGenes(genes);
                        ind.setSigmas(sigmas);
                }
        }
}


/**
 * Merges two populations into one population (e.g. offspring and current
 * generation)
 *
 * @param populationA
 * @param populationB
 * @return Population
 */
private Population mergePopulations(Population populationA, Population populationB) {
        Population[] populations = { populationA, populationB };
        Population total_pop = new Population();

        for (Population pop : populations) {
                Population tmp_pop = pop.clonePopulation();
                total_pop.addIndividuals(tmp_pop.getPopulation());
        }

        return total_pop;
}

/**
 * Selects out of the pool of offspring and parents the best 'population-size'
 * survivors
 *
 * @pre Parents and offspring have been selected, each with their own fitness
 *      values, after potential crossover and mutation.
 *
 * @post The top population-size survivors are selected for the next generation.
 *
 */
public void selectSurvivors() {
        // Initializes containers for all of the population and survivors
        Population new_population = mergePopulations(population, offspring);
        // Sorts and limits nr of population
        Collections.sort(new_population.getPopulation(), Collections.reverseOrder());
        new_population.limitNPopulation(population.getPopulation().size());
        this.population = new_population;
}

public void run() {
        double realSigma = msigma;
        while (evals < eval_limits) {
                // ShowBestFitness();
                // Select parents
                // Select_RW(); //Roulette Wheel Selection
                Select_TS(sizeOfT);          // Tournament Selection(size of the Tournament)

                // Apply crossover / mutation operators
                Crossover_SA(mixRate);         // Simple arithmetic crossover
                // Crossover_WA(mixRate); // Whole arithmetic crossover
                nonUniformMutation(realSigma);         // randomGauss(sigma)
                // UncorrelatedMutation();
                realSigma = Utils.exponentialDecay(msigma, evals, eval_limits);
                // realSigma = Utils.linearDecay(msigma, evals, eval_limits);
                // Check fitness of unknown fuction

                evaluateOffspringPool();
                setOffspring(offspring);
                selectSurvivors();
        }
}

/**
 * Sets fitness values for all new offspring.
 *
 * @pre Offspring in this.offspring have been selected from existing parents.
 * @post 1. Offspring now have an associated fitness value. 2. Evaluation has
 *       gone up by nr in offspring.
 */
private void evaluateOffspringPool() {
        for (int i = 0; i < offspring.size(); i++) {
                Individual child = offspring.get(i);         // Get child
                double fitness = (double) e.evaluate(child.getGenes());         // Calculate fitness
                offspring.setFitnessFori(i, fitness);         //
                evals++;
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
        // Q: Is this necessary? offspring is now set to an instance-property, which is
        // how it is always referred to so far.
        this.offspring = offspring;
}

public Population getSurvivors() {
        return survivors;
}

public void setSurvivors(Population survivors) {
        this.survivors = survivors;
}
}
