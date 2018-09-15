import org.vu.contest.ContestEvaluation;
import java.util.Random;

public class Population {
    public static final int DIM = 10;
    // temporarily using native double [][]
    // the design is based on this fact: using childrenMatrix to collect the
    // selected arrays in parentMatrix by children[j]=parent[i]
    // and the crossover and mutation changes on children would reflect to parents
    // correct me if im wrong
    private double[][] parentMatrix;
    private double[][] childrenMatrix = null;
    private double[] scores;
    private int[] selected = null;
    private ContestEvaluation eval;

    private int populationSize;
    private int childrenSize;

    public Population(int populationSize, int childrenSize, ContestEvaluation eval) {
        populationSize = populationSize;
        childrenSize = childrenSize;
        parentMatrix = new double[populationSize][DIM];
        scores = new double[populationSize];
        eval = eval;
        rnd_ = new Random();
        rnd_.setSeed(42);
        initSampling();
        initScores();
    }

    private initSampling(){
        for (double[] ind : parentMatrix) {
            for(int i=0;i<ind.length;i++){
                ind[i] = rnd_.nextDouble()*10-5;
            }
        }
    }

    public void setSeed(long seed) {
        rnd_.setSeed(seed);
    }

    // evaluate the newly generated children
    // I feel like we should add a phase var to determine whether its the time to
    // update score
    // But it should be ok if we write the strategy right
    public void scoresUpdate() {
        if (this.selected==null)
            return;
        for (int i : this.selected) {
            scores[i] = this.eval.evaluate(parentMatrix[i]);
        }
    }

    private void initScores() {
        for (int i = 0; i < this.populationSize; i++) {
            scores[i] = this.eval.evaluate(parentMatrix[i]);
        }
    }

    public double[][] getParents() {
        return parentMatrix;
    }

    public double[][] getChildren() {
        return parentMatrix;
    }

    public int[] getSelected() {
        return selected;
    }

    public double[] getScores() {
        return scores;
    }
}