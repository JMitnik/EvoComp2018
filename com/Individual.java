package com;
public class Individual implements Comparable<Individual>{
private double genes[];
private double fitness;
public Individual(){

}
public Individual(double[] genes){
        this.genes=genes;
}
public double[] getGenes(){
        return genes;
}
public void setGenes(double[] genes){
        this.genes=genes;
}
public double getFitness() {
        return fitness;
}
public void setFitness(double fitness) {
        this.fitness = fitness;
}
@Override
public int compareTo(Individual ind) {
        if(ind.getFitness() > this.getFitness()) {
                return -1;
        }else if(ind.getFitness() == this.getFitness()) {
                return 0;
        }else{
                return 1;
        }
}
}
