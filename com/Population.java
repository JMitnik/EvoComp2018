package com;
import com.Individual;
import java.util.ArrayList;
public class Population {
private ArrayList<Individual> population = null;
public Population(){
        this.population = new ArrayList<Individual>();
}
public void addIndividual(Individual newIndividual){
        this.population.add(newIndividual);
}
public ArrayList<Individual> getPopulation() {
        return population;
}
public void setPopulation(ArrayList<Individual> population) {
        this.population = population;
}
}
