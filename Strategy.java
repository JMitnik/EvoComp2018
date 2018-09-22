import org.vu.contest.ContestEvaluation;

public interface Strategy {
    final static int DIM = 10;
    public void selection();

    public void mutation();

    public void crossover();

    public void propagate();
    
    public boolean isTerminated();

    public void run();
}