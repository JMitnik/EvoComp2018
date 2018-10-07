import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;
import java.util.Random;
import java.util.Properties;
import com.Individual;
import com.Population;
import com.EvoAlgorithm;
import com.Utils;
public class player14 implements ContestSubmission {
Random rnd_;
ContestEvaluation evaluation_;
private int evaluations_limit_;
private Population pop;
private int popsize;
private boolean isMultimodal;
private boolean hasStructure;
private boolean isSeparable;

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
        // E.g. double param =
        // Double.parseDouble(props.getProperty("property_name"));
        isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
        hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
        isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));
        // System.out.println("isMultimodal:"+isMultimodal);
        // System.out.println("hasStructure:"+hasStructure);
        // System.out.println("isSeparable:"+isSeparable);
        // Do sth with property values, e.g. specify relevant settings of your
        // algorithm
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
                double child[] = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
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
        int sizeOfT = 10;
        int popsize= 100;        
        System.out.print(test);
        double pIndMutationProb = 0.45, pDimMutationProb = 0.3, crossoverIndProb = 1.0, crossoverDimProb = 0.9,
               mixRate = 0.5, msigma = 2;                  // Parameters
        if (!isMultimodal && hasStructure && isSeparable) {           // Parameters for SphereEvaluation
                popsize=200;
                sizeOfT=100;
                pIndMutationProb = 0.8;
                pDimMutationProb = 1;
                crossoverIndProb = 1.0;
                crossoverDimProb = 0.8;
                mixRate = 0.5;
                msigma = 0.1;
        }
        if (!isMultimodal && !hasStructure && !isSeparable) {         // Parameters for BentCigarFunction
                popsize=500;
                sizeOfT=25;
                pIndMutationProb = 0.5;
                pDimMutationProb = 1.0;
                crossoverIndProb = 1.0;
                crossoverDimProb = 0.8;
                mixRate = 0.5;
                msigma = 0.02;
        }
        if (isMultimodal && hasStructure && !isSeparable) {         //Parameters for SchaffersEvaluation
                popsize=2000;
                sizeOfT=10;
                pIndMutationProb = 0.5;
                pDimMutationProb = 1.0;
                crossoverIndProb = 1.0;
                crossoverDimProb = 0.8;
                mixRate = 0.5;
                msigma = 0.1;
        }
        if (isMultimodal && !hasStructure && !isSeparable) {         //Parameters for KatsuuraEvaluation
                popsize=2000;
                sizeOfT=200;
                pIndMutationProb =0.5;
                pDimMutationProb = 0.5;
                crossoverIndProb = 1;
                crossoverDimProb = 1;
                mixRate = 0.5;
                msigma = 0.5;
        }
        this.popsize = popsize;
        this.InitPopulation();
        EvoAlgorithm evo = new EvoAlgorithm(pop, evaluation_, pIndMutationProb, pDimMutationProb,
                                    crossoverIndProb, crossoverDimProb, evaluations_limit_ - this.popsize-1, sizeOfT, mixRate,
                                    msigma);
        evo.run();
}
}
