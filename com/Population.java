package com;

import com.Individual;
import java.util.ArrayList;

public class Population {
        private ArrayList<Individual> population = null;

        public Population() {
                this.population = new ArrayList<Individual>();
        }

        public void addIndividual(Individual newIndividual) {
                this.population.add(newIndividual);
        }

        public ArrayList<Individual> getPopulation() {
                return population;
        }

        public Individual get(int i) {
                return population.get(i);
        }

        public double getFitnessFori(int i) {
                return get(i).getFitness();
        }

        public double[] getGeneFori(int i) {
                return get(i).getGenes();
        }

        public int size() {
                return population.size();
        }

        public void setPopulation(ArrayList<Individual> population) {
                this.population = population;
        }

        public void setFitnessFori(int i, double fitness) {
                get(i).setFitness(fitness);
                return;
        }
}
