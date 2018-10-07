package com;

public class Individual implements Comparable<Individual> {
private double genes[];
private double sigmas[];
private double fitness;
private boolean changed=false;
public Individual() {
        double sigmas[] = {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
        this.sigmas = sigmas;
}

public Individual(double[] genes) {
        this.genes = genes;
        double sigmas[] = {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
        this.sigmas = sigmas;
}

public double[] getGenes() {
        return genes;
}

public void setGenes(double[] genes) {
        this.genes = genes;
}

public double[] getSigmas() {
        return sigmas;
}

public void setSigmas(double[] sigmas) {
        this.sigmas = sigmas;
}

public double getFitness() {
        return fitness;
}

public void setFitness(double fitness) {
        this.fitness = fitness;
}
public void setChanged() {
        changed = true;
}
@Override
public int compareTo(Individual ind) {
        if (ind.getFitness() > this.getFitness()) {
                return -1;
        } else if (ind.getFitness() == this.getFitness()) {
                return 0;
        } else {
                return 1;
        }
}
}
