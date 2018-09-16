import org.vu.contest.ContestEvaluation;
import java.util.Random;
import Population;

public interface Strategy {
    final static int DIM = 10;
    public void selection();

    public void mutation();

    public void crossover();

    public void propagate();
    
    public bool isTerminated();

    public void run();
}