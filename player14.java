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
                // Do sth with property values, e.g. specify relevant settings of your
                // algorithm
                if (isMultimodal) {
                        // Do sth
                } else {
                        // Do sth else
                }
        }

        // public void InitPopulation() {
        //         // init population and calculate fitness
        //         this.pop = new Population();
        //         for (int i = 0; i < this.popsize; i++) {
        //                 double child[] = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
        //                 for (int j = 0; j < 10; j++) {
        //                         child[j] = rnd_.nextDouble() * 10 - 5;
        //                 }
        //                 Individual newIndividual = new Individual(child);
        //                 newIndividual.setFitness((double) evaluation_.evaluate(child));
        //                 pop.addIndividual(newIndividual);
        //         }
        // }

        public void run() {
                // Run your algorithm here
                int evals = 0;
                int populationSize = 20;
                double topRatio = 0.2;
                double sigma=1.;
                if (!isMultimodal && hasStructure && isSeparable) {populationSize=20;topRatio=0.3; }        // Parameters for SphereEvaluation
                if (!isMultimodal && !hasStructure && !isSeparable) {populationSize=100;topRatio=0.25; }      // Parameters for BentCigarFunction
                if (isMultimodal && hasStructure && !isSeparable) {populationSize=20;topRatio=0.3; }        //Parameters for SchaffersEvaluation
                if (isMultimodal && !hasStructure && !isSeparable) {populationSize=20; topRatio=0.3;}       //Parameters for KatsuuraEvaluation

                EvoAlgorithm evo = new EvoAlgorithm(evaluation_, populationSize, topRatio, evaluations_limit_);
                evo.run();
        }
}
