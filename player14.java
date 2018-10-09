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
        double pIndMutationProb=1,pDimMutationProb=1,crossoverIndProb=1,crossoverDimProb=1,mixRate=1, msigma=1;     // Parameters
        if (!isMultimodal && hasStructure && isSeparable) {           // Parameters for SphereEvaluation
                popsize=500;
                sizeOfT=100;
                crossoverIndProb = 1.0;
                crossoverDimProb = 0.8;
                mixRate = 0.5;
                msigma = 0.1;
        }
        if (!isMultimodal && !hasStructure && !isSeparable) {         // Parameters for BentCigarFunction              //
                popsize=500;
                sizeOfT=100;
                crossoverIndProb = 1.0;
                crossoverDimProb = 0.8;
                mixRate = 0.5;
                msigma = 0.02;
        }
        if (isMultimodal && hasStructure && !isSeparable) {         //Parameters for SchaffersEvaluation
                popsize=2000;
                sizeOfT=100;
                crossoverIndProb = 1.0;
                crossoverDimProb = 0.8;
                mixRate = 0.5;
                msigma = 0.1;
        }
        if (isMultimodal && !hasStructure && !isSeparable) {         //Parameters for KatsuuraEvaluation
                popsize=500;
                sizeOfT=200;
                pIndMutationProb =0.5;
                pDimMutationProb = 0.5;
                crossoverIndProb = 1;
                crossoverDimProb = 1;
                mixRate = 0.5;
                msigma = 0.5;
        }
        try{
          popsize=(int)Double.parseDouble(System.getProperty("popsize"));

        }
        catch (Exception e) {
        }
        try{
          sizeOfT=(int)Double.parseDouble(System.getProperty("tsize"));
        }
        catch (Exception e) {}
        this.popsize = popsize;
        this.InitPopulation();
        EvoAlgorithm evo = new EvoAlgorithm(pop, evaluation_, pIndMutationProb, pDimMutationProb,
                                    crossoverIndProb, crossoverDimProb, evaluations_limit_ - this.popsize-1, sizeOfT, mixRate,
                                    msigma);
        evo.run();
        // System.out.print(pIndMutationProb);
}
}
