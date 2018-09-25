/*
File : Population.java
Submission for Evolutionary Computing Group Assignment, Group 14.
This file contains code which implementes functions for population object to declare object,
adding individuals to the population and setting population.

Authors: Weitao Luo
Version: 1.1
Date: 25 Sep 2018

Recent changes: Clean up codes, add comments and explaination, make the code easier to read.
 */
package com;
import com.Individual;
import java.util.ArrayList;
public class Population
{
        //Each population is an arraylist, because it is flexible to add, indexing and delete individuals.
        private ArrayList<Individual> population = null;

        //Constructor method to create new population ArrayList.
        public Population()
        {
                this.population = new ArrayList<Individual>();
        }//Population

        public void addIndividual(Individual newIndividual)
        {
                this.population.add(newIndividual);
        }

        public ArrayList<Individual> getPopulation()
        {
                return population;
        }

        public void setPopulation(ArrayList<Individual> population)
        {
                this.population = population;
        }
}//Population
