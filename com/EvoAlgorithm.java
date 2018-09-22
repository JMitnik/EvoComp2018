package com;
import java.util.*;
import com.*;
public class EvoAlgorithm {
private double p_mutation;
private double p_crossover;
private int popsize;
private int dim;
private Population population;
private Population parent1;
private Population parent2;
private Population offspring;
private Population survivors;
public EvoAlgorithm(Population population,double p_mutation, double p_crossover) {
        this.population=population;
        this.p_mutation = p_mutation;
        this.p_crossover = p_crossover;
        this.popsize=population.getPopulation().size();
        this.dim=population.getPopulation().get(0).getGenes().length;
}
// Proportionate Roulette Wheel Selection
public void Select_RW(){
        //get the minimum fitness
        double min=population.getPopulation().get(0).getFitness();
        for(int i=1; i<popsize; i++) {
                Individual ind = population.getPopulation().get(i);
                if (ind.getFitness()<min) {
                        min=ind.getFitness();
                }
        }
        //get the summation
        double sum=0;
        for(int i=0; i<popsize; i++) {
                Individual ind = population.getPopulation().get(i);
                sum+=(ind.getFitness()-min);
        }
        if(sum==0) return;
        this.parent1=new Population();
        this.parent2=new Population();
        Random random = new Random();
        for(int i=0; i<popsize/2; i++) {
                double rnd=random.nextDouble();
                double sum_p=0.0;
                for(int j=0; j<popsize; j++) {
                        Individual ind = this.population.getPopulation().get(j);
                        sum_p+=(ind.getFitness()-min)/sum;
                        if(sum_p>=rnd) {
                                Individual tmp=new Individual();
                                tmp.setGenes(ind.getGenes());
                                tmp.setFitness(ind.getFitness());
                                parent1.addIndividual(tmp);
                                break;
                        }
                }
        }
        for(int i=0; i<popsize/2; i++) {
                double rnd=random.nextDouble();
                double sum_p=0.0;
                for(int j=0; j<popsize; j++) {
                        Individual ind = population.getPopulation().get(j);
                        sum_p+=(ind.getFitness()-min)/sum;
                        if(sum_p>=rnd) {
                                Individual tmp=new Individual();
                                tmp.setGenes(ind.getGenes());
                                tmp.setFitness(ind.getFitness());
                                parent2.addIndividual(tmp);
                                break;
                        }
                }
        }
}
// Tournament Selection
public void Select_TS(int tsize){
        Random random = new Random();
        parent1=new Population();
        parent2=new Population();
        for(int i=0; i<popsize/2; i++) {
                Population Tournament= new Population();
                for(int j=0; j<tsize; j++) {
                        Individual rnd=this.population.getPopulation().get(random.nextInt(popsize));
                        Tournament.addIndividual(rnd);
                }
                Collections.sort(Tournament.getPopulation(),Collections.reverseOrder());
                Individual tmp=new Individual();
                tmp.setGenes(Tournament.getPopulation().get(0).getGenes());
                tmp.setFitness(Tournament.getPopulation().get(0).getFitness());
                parent1.addIndividual(tmp);
        }
        for(int i=0; i<popsize/2; i++) {
                Population Tournament= new Population();
                for(int j=0; j<tsize; j++) {
                        Individual rnd=this.population.getPopulation().get(random.nextInt(popsize));
                        Tournament.addIndividual(rnd);
                }
                Collections.sort(Tournament.getPopulation(),Collections.reverseOrder());
                Individual tmp=new Individual();
                tmp.setGenes(Tournament.getPopulation().get(0).getGenes());
                tmp.setFitness(Tournament.getPopulation().get(0).getFitness());
                parent2.addIndividual(tmp);
        }
}
//single point crossover
public void Crossover(){
        Random random = new Random();
        offspring= new Population();
        for(int p=0; p<popsize/2; p++) {
                Individual dad=parent1.getPopulation().get(p);
                Individual mom=parent2.getPopulation().get(p);
                if(random.nextDouble()>p_crossover) {
                        Individual offspring1=new Individual(dad.getGenes());
                        Individual offspring2=new Individual(mom.getGenes());
                        offspring.addIndividual(offspring1);
                        offspring.addIndividual(offspring2);
                }
                else{
                        double genes1[]=dad.getGenes();
                        double genes2[]=mom.getGenes();
                        int pos=random.nextInt(dim);
                        //generate new genes
                        double new_genes1[]={0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
                        double new_genes2[]={0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
                        for(int i=0; i<dim; i++) {
                                if(i<=pos) {
                                        new_genes1[i]=genes1[i];
                                        new_genes2[i]=genes2[i];
                                }
                                else{
                                        new_genes1[i]=genes2[i];
                                        new_genes2[i]=genes1[i];
                                }
                        }
                        Individual offspring1=new Individual(new_genes1);
                        Individual offspring2=new Individual(new_genes2);
                        offspring.addIndividual(offspring1);
                        offspring.addIndividual(offspring2);
                }
        }        
}
public void Mutation(double sigma){
        Random random = new Random();
        for(int i=0; i<popsize; i++) {
                if(random.nextDouble()<=p_mutation) {
                        Individual ind=offspring.getPopulation().get(i);
                        double genes[]=ind.getGenes();
                        for(int j=0; j<dim; j++) {
                                genes[j]= Math.max(-5, Math.min(5, genes[j]+random.nextGaussian()*5*sigma));
                        }
                        offspring.getPopulation().get(i).setGenes(genes);
                }
        }
}
public void Survive(){
        Population pop_all=new Population();
        survivors=new Population();
        for(int i=0; i<population.getPopulation().size(); i++) {
                Individual tmp=new Individual();
                tmp.setGenes(population.getPopulation().get(i).getGenes());
                tmp.setFitness(population.getPopulation().get(i).getFitness());
                pop_all.addIndividual(tmp);
        }
        for(int i=0; i<offspring.getPopulation().size(); i++) {
                Individual tmp=new Individual();
                tmp.setGenes(offspring.getPopulation().get(i).getGenes());
                tmp.setFitness(offspring.getPopulation().get(i).getFitness());
                pop_all.addIndividual(tmp);
        }
        Collections.sort(pop_all.getPopulation(), Collections.reverseOrder());
        for(int i=0; i<population.getPopulation().size(); i++) {
                survivors.addIndividual(pop_all.getPopulation().get(i));
        }
        this.population=survivors;
}
public void ShowFitness(Population pop,String s){
        int popsize=pop.getPopulation().size();
        System.out.println(s);
        for(int i=0; i<popsize; i++) {
                System.out.print(pop.getPopulation().get(i).getFitness()+" ");
        }
        System.out.println();
}
public void ShowBestFitness(){
        double max=population.getPopulation().get(0).getFitness();
        for(int i=1; i<popsize; i++) {
                double tmp=population.getPopulation().get(i).getFitness();
                if(tmp>max) {
                        max=tmp;
                }
        }
        System.out.println("Max:"+max);
}
public Population getPopulation(){
        return population;
}
public void setPopulation(Population population){
        this.population=population;
}
public Population getOffspring() {
        return offspring;
}
public void setOffspring(Population offspring){
        this.offspring=offspring;
}
public Population getSurvivors() {
        return survivors;
}
public void setSurvivors(Population survivors){
        this.survivors=survivors;
}
}
