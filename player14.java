/*
File : player14.java
Submission for Evolutionary Computing Group Assignment, Group 14.
This file contains code which implementes a main function to initilise parameters
and start simulation for the evolution algorithms. It will set the seed, initialise
population, set mutation and crossover probabilities and evaluate evolution algorithms.


Authors: Weitao Luo, Liang Huang
Version: 1.1
Date: 25 Sep 2018

Recent changes: Clean up codes, add comments and explaination, make the code easier to read.
 */

import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;
import java.util.Random;
import java.util.Properties;
import com.Individual;
import com.Population;
import com.EvoAlgorithm;


public class player14 implements ContestSubmission
{
  //Declare variables.
  Random rnd_;
  ContestEvaluation evaluation_;
  private int evaluations_limit_;
  private Population pop;
  private int popsize;

  //Default Constructor method for player14.
  public player14()
  {
    rnd_ = new Random();
  }//player14

  //Set seed of algortihms random process.
  //Each time we run the program, it will generate different random numbers.
  public void setSeed(long seed)
  {
    rnd_.setSeed(seed);
  }//setSeed

  /*
  This function is to set specific parameters and mode which can be
  used for evaluation process.
   */
  public void setEvaluation(ContestEvaluation evaluation)
  {
    // Set evaluation problem used in the run
    evaluation_ = evaluation;
    // Get evaluation properties
    Properties props = evaluation.getProperties();
    // Get evaluation limit
    evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));
    // Property keys depend on specific evaluation
    // E.g. double param = Double.parseDouble(props.getProperty("property_name"));
    boolean isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
    boolean hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
    boolean isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));
    // Do sth with property values, e.g. specify relevant settings of your algorithm
    if (isMultimodal)
    {
      // Do sth
    } else
    {
      // Do sth else
    }
  }//setEvaluation

  /*
  This method is to random initialise individuals and add them to the population.
  For each individuals, their fitness will be evaluated and set.
  Each individual object has two variables: gene matrix and fitness value.
  Genotype: Each individual's gene is an array consisting of 10 double numbers.
  Fitness value: Double value evaluated by evaluation_.evaluate() method.
   */
  public void InitPopulation()
  {
    // init population and calculate fitness
    this.pop = new Population();

    //For each child, random initialise them and set their fitness value.
    for (int i = 0; i < this.popsize; i++)
    {
      double child[] = {
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0
      };

      for (int j = 0; j < 10; j++)
      {
        child[j] = rnd_.nextDouble() * 10 - 5;
      }//for

      Individual newIndividual = new Individual(child);
      newIndividual.setFitness((double) evaluation_.evaluate(child));
      pop.addIndividual(newIndividual);
    }//for-loop
  }

  /*
  Run all the methods in this function.
   */
  public void run()
  {
    //Initialise parameters.
    int evals = 0;

    //Set population size equal to 10.
    this.popsize = 10;
    this.InitPopulation();

    //Probability of mutation and crossover
    double p_mutation = 0.2, p_crossover = 0.8;

    //Create evolutation algorithm with given population object, mutation and crosser rates.
    EvoAlgorithm evo = new EvoAlgorithm(pop, p_mutation, p_crossover);

    //Runing evolutation algorithm until reach the population limit.
    while (evals < evaluations_limit_ - this.popsize)
    {
      evo.ShowBestFitness();
      // Select parents
      // evo.Select_RW();			//Roulette Wheel Selection
      evo.Select_TS(3); // Tournament Selection(size of the Tournament)
      // Apply crossover / mutation operators
      evo.Crossover();
      evo.Mutation(0.1); //randomGauss(sigma)

      // Check fitness of unknown fuction
      Population offspring = evo.getOffspring();

      for (int i = 0; i < offspring.getPopulation().size(); i++)
      {
        Individual child = offspring.getPopulation().get(i);
        double fitness = (double) this.evaluation_.evaluate(child.getGenes());
        offspring.getPopulation().get(i).setFitness(fitness);
        evals++;
      }//for

      evo.setOffspring(offspring);
      // Select survivors
      evo.Survive();
    }//while-loop
  }//run
}//player14
