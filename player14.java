import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;
import java.util.Random;
import java.util.Properties;
import com.Individual;
import com.Population;
import com.EvoAlgorithm;
public class player14 implements ContestSubmission {
Random rnd_;
ContestEvaluation evaluation_;
private int evaluations_limit_;
private Population pop;
private int popsize;
public player14() {
        rnd_ = new Random();
}

public void setSeed(long seed) {
        // Set seed of algortihms random process
        rnd_.setSeed(seed);
}

public void setEvaluation(ContestEvaluation evaluation) {
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
        if (isMultimodal) {
                // Do sth
        } else {
                // Do sth else
        }
}
public void InitPopulation() {
        // init population and calculate fitness
        this.pop = new Population();
        for (int i = 0; i < this.popsize; i++) {
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
                for (int j = 0; j < 10; j++) {
                        child[j] = rnd_.nextDouble() * 10 - 5;
                }
                Individual newIndividual = new Individual(child);
                newIndividual.setFitness((double) evaluation_.evaluate(child));
                pop.addIndividual(newIndividual);
        }
}
public void run() {
        // Run your algorithm here
        int evals = 0;
        // init population, population size is 10
        this.popsize = 10;
        this.InitPopulation();
        double p_mutation = 0.2, p_crossover = 1; //Probability of mutation and crossover
        EvoAlgorithm evo = new EvoAlgorithm(pop, p_mutation, p_crossover);
        while (evals < evaluations_limit_ - this.popsize) {
                // evo.ShowBestFitness();
                // Select parents
                // evo.Select_RW();			      //Roulette Wheel Selection
                evo.Select_TS(4);             // Tournament Selection(size of the Tournament)
                // Apply crossover / mutation operators
                //evo.Crossover_SA(0.5);        // Simple arithmetic crossover
                evo.Crossover_WA(0.5);      //Whole arithmetic crossover
                evo.Mutation(0.05); //randomGauss(sigma)
                // Check fitness of unknown fuction
                Population offspring = evo.getOffspring();
                for (int i = 0; i < offspring.getPopulation().size(); i++) {
                        Individual child = offspring.getPopulation().get(i);
                        double fitness = (double) this.evaluation_.evaluate(child.getGenes());
                        offspring.getPopulation().get(i).setFitness(fitness);
                        evals++;
                }
                evo.setOffspring(offspring);
                // Select survivors
                evo.Survive();
        }
}
}
