
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

        public void addIndividuals(ArrayList<Individual> individuals) {
                for (Individual ind : individuals) {
                        this.addIndividual(ind);
                }
        }

        public void limitNPopulation(int limit) {
                this.population = new ArrayList<Individual>(this.population.subList(0, limit));
        }

        public Population clonePopulation() {
                Population clonePopulation = new Population();

                for (Individual ind : this.population) {
                        Individual tmp = new Individual();
                        tmp.setGenes(ind.getGenes());
                        tmp.setFitness(ind.getFitness());
                        clonePopulation.addIndividual(tmp);
                }

                return clonePopulation;
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
