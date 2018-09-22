import java.util.Random;

public class SimpleStrategy implements Strategy {
    // dont pay attn to these craps, you will know what they are for when you read
    // the code.
    private Population p;
    private int populationSize;
    private int childrenSize;
    private int maxIterations;
    private double mutationIndProb;
    private double crossoverIndProb;
    private double crossoverDimProb;
    private double mutationDimProb;
    private double mutationSigma;
    private boolean isUsingMixInCrossover;
    private double mixRate;
    private int curIter = 0;

    private double[] scores = p.getScores();
    private double[][] children = p.getChildren();
    private double[][] parents = p.getParents();
    private int[] selected = p.getSelected();
    private int[] unselected;
    private Random rnd_;

  public SimpleStrategy(Population p, double mutationIndProb,
                        double mutationDimProb, double mutationSigma,
                        double crossoverIndProb, double crossoverDimProb,
                        boolean isUsingMixInCrossover, double mixRate, int maxIterations) {
    populationSize = p.getPopulationSize();
    childrenSize = p.getChildrenSize();
    this.p = p;
    this.mutationIndProb = mutationIndProb;
    this.mutationDimProb = mutationDimProb;
    this.crossoverIndProb = crossoverIndProb;
    this.crossoverDimProb = crossoverDimProb;
    this.mutationSigma = mutationSigma;
    this.isUsingMixInCrossover = isUsingMixInCrossover;
    this.mixRate = mixRate;
    this.maxIterations = maxIterations;
    rnd_ = new Random();
    rnd_.setSeed(42);
    scores = p.getScores();
    children = p.getChildren();
    parents = p.getParents();
    selected = p.getSelected();
    if(selected==null){
        selected=new int[childrenSize];
        unselected=new int[populationSize-childrenSize];
    }
  }

    // selection does two things:
    // 1. fill the selected int array with indices of selected children in the
    // parents array
    // 2. fill the children array with selected rows in the parents array
    // using SUS algo to select, details
    // in:https://www.wikiwand.com/en/Stochastic_universal_sampling
    // Note:
    // 1. the below impl depends on the fact that the fitness function returns
    // positive value,
    // if its not the case, plz dont use it.
    // 2. SUS actually can select same indices, which can be little bit buggy when
    // things going wild
    @Override
    public void selection() {

        double fitness_sum = utils.sum(scores);
        double slice = fitness_sum / childrenSize;
        // using start as the start point and the accumulate var in the loop below
        double start = rnd_.nextDouble() * slice;
        double tempSum = 0;
        int j = 0, k = 0;
        for (int i = 0; i < populationSize;) {
            // while the accumulated fitness is still in current slice
            tempSum += scores[i];
            while (tempSum < start) {
                unselected[k] = i;
                k++;
                i++;
                tempSum += scores[i];
            }
            if (i >= populationSize)
                break;
            if (j == childrenSize) {
                selected[j] = i;
                children[j] = parents[i];
                j += 1;
            }
            start += slice;
        }
        return;
    }

    // mutation adds a random gaussian noise whose sigma is provided to the
    // chosen children[i][j]. Then the number is clamped to [-5,5]. i is chosen by
    // mutationProb, j is chosen by
    // mutationDimProb, I want the randomness not only reflect on the individual
    // level but also on dim
    @Override
    public void mutation() {
        for (int i = 0; i < childrenSize; i++) {
            if (rnd_.nextDouble() < mutationIndProb) {
                for (int j = 0; j < Strategy.DIM; j++) {
                    if (rnd_.nextDouble() < mutationDimProb) {
                        children[i][j] = utils.clamp(children[i][j] + rnd_.nextGaussian() * mutationSigma);
                    }
                }
            }
        }
    }

    // crossover pretty much follow the same logic above, but we need to choose a
    // pair but not a single index.
    // so we set a var `prev`, when its -1, we havent found our first candidate,
    // when its positive, pair the second cand with it.
    @Override
    public void crossover() {
//      TODO: We need to properly define the max
        int max = 1000;
        int prev = -1;
        // looping on the first dim and roll the dice
        for (int i = 0; i < childrenSize; i++) {
            if (rnd_.nextDouble() < crossoverIndProb) {
                // if we have already got one candidate
                if (prev > -1) {
                    for (int j = 0; j < max; j++) {
                        // start cross over
                        if (rnd_.nextDouble() < crossoverDimProb) {
                            if (isUsingMixInCrossover) {
                                utils.swappingMix(children, prev, j, i, j, mixRate);
                            } else {
                                utils.swap(children, prev, j, i, j);
                            }
                        }
                        // end cross over
                    }
                    prev = -1;
                }
                // if we havent got one candidate, then the present i will be the first
                // candidate
                else {
                    prev = i;
                }
            }
        }
    }

    // randomly move a selected vec to unselected one, in order to get rid of the
    // possibly low score ones
    @Override
    public void propagate() {
        for (int i : unselected) {
            int ind = rnd_.nextInt(childrenSize);
            utils.copyVector(parents[selected[ind]], parents[i]);
        }
        return;
    }

    // currently only a simple one
    @Override
    public void run() {
        while(!isTerminated()) {
            selection();
            mutation();
            crossover();
            propagate();
        }
    }

    // currently only a simple one
    @Override
    public boolean isTerminated() {
        if (curIter > maxIterations) {
            return true;
        }
        else
            return false;
    }

    public void resetMaxIteration(int iter) {
        maxIterations = iter;
        curIter = 0;
    }
}