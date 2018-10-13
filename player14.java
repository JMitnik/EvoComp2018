import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;
import org.ejml.simple.*;
import java.util.Random;
import java.util.Properties;
public class player14 implements ContestSubmission
{
Random rnd_;
ContestEvaluation evaluation_;
private int evaluations_limit_;
private int popSize;
private double mu_ratio;
private int index;
private double sigma;
boolean isMultimodal;
boolean hasStructure;
boolean isSeparable;
public player14()
{
        rnd_ = new Random();
}

public void setSeed(long seed)
{
        // Set seed of algortihms random process
        rnd_.setSeed(seed);
}

public void setEvaluation(ContestEvaluation evaluation)
{
        // Set evaluation problem used in the run
        evaluation_ = evaluation;

        // Get evaluation properties
        Properties props = evaluation.getProperties();
        // Get evaluation limit
        evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));
        // Property keys depend on specific evaluation
        // E.g. double param = Double.parseDouble(props.getProperty("property_name"));
        isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
        hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
        isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));

        // Do sth with property values, e.g. specify relevant settings of your algorithm
        if(isMultimodal) {
                // Do sth
        }else{
                // Do sth else
        }
}

public void run()
{
        if (!isMultimodal && hasStructure && isSeparable) {popSize=20; mu_ratio=0.25; sigma=1;index=0; }        // Parameters for SphereEvaluation
        if (!isMultimodal && !hasStructure && !isSeparable) {popSize=20; mu_ratio=0.5; sigma=0.004;index=1;  }      // Parameters for BentCigarFunction
        if (isMultimodal && hasStructure && !isSeparable) {popSize=100; mu_ratio=0.6; sigma=0.1;index=2;  }        //Parameters for SchaffersEvaluation
        if (isMultimodal && !hasStructure && !isSeparable) {popSize=100; mu_ratio=0.5; sigma=0.001;index=3;  }       //Parameters for KatsuuraEvaluation
        try{
          popSize=(int)Double.parseDouble(System.getProperty("popsize"));
          sigma=Double.parseDouble(System.getProperty("sigma"));
        }
        catch (Exception e) {
        }
        EvoAlgorithm evo=new EvoAlgorithm(rnd_,evaluation_,evaluations_limit_,popSize,mu_ratio,sigma,index);
        evo.run();
}
}
