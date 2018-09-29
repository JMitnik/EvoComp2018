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
                // E.g. double param =
                // Double.parseDouble(props.getProperty("property_name"));
                boolean isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
                boolean hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
                boolean isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));
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
                this.popsize = 10;
                this.InitPopulation();
                int sizeOfT = 4;
                double p_mutation = 0.45, pDimMutationProb=0.3, p_crossover = 1.0,
                       mixRate = 0.5, msigma = 0.02; // Parameters
                EvoAlgorithm evo = new EvoAlgorithm(
                    pop, evaluation_, p_mutation, pDimMutationProb, p_crossover,
                    evaluations_limit_ - this.popsize, sizeOfT, mixRate,
                    msigma);
                evo.run();
        }
}
