/*
File : Population.java
Submission for Evolutionary Computing Group Assignment, Group 14.
This file contains code which implementes functions for Individual object to declare new object,
setting genes, fitness value to the Individual object and compare to other individuals.

Authors: Weitao Luo
Version: 1.1
Date: 25 Sep 2018

Recent changes: Clean up codes, add comments and explaination, make the code easier to read.
 */

package com;
public class Individual implements Comparable<Individual>
{
    /*
    Each individual object has two variables: gene matrix and fitness value.
    Genotype: Each individual's gene is an array consisting of 10 double numbers.
    Fitness value: Double value evaluated by evaluation_.evaluate() method.
    */
    private double genes[];
    private double fitness;

    //Empty constructor method.
    public Individual()
    {

    }

    //Create new Individual with given genes matrix.
    public Individual(double[] genes)
    {
        this.genes=genes;
    }

    public double[] getGenes()
    {
        return genes;
    }

    public void setGenes(double[] genes)
    {
        this.genes=genes;
    }

    public double getFitness()
    {
        return fitness;
    }

    public void setFitness(double fitness)
    {
        this.fitness = fitness;
    }

    @Override
    public int compareTo(Individual ind)
    {
        if(ind.getFitness() > this.getFitness())
        {
            return -1;
        }else if(ind.getFitness() == this.getFitness())
        {
            return 0;
        }else
        {
            return 1;
        }
    }//compareTo
}
