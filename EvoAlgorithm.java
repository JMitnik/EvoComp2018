import java.util.*;
import org.vu.contest.ContestEvaluation;
import org.ejml.simple.*;
public class EvoAlgorithm {
private static int DIM = 10;
private static double gammaExp = 3.084327759799864;
private ContestEvaluation e;

//lambda-->populaton size/offspring number
private int lambda;
//mu-->number of parets/points for recombination
private int mu;
//weights
private double[] weights;
// $\mu_w$
private double mu_w;
// sigma
private double sigma;

// c_c
private double c_c = 4 / DIM;
// c_sigma
private double c_sigma = 4 / DIM;
// c_1
private double c_1 = 2 / DIM / DIM;
// c_mu
private double c_mu;
// d_\sigma
private double dsigma;


// fitness
private double[] fitness;
private double[] genes;

private int evals;
private int eval_limits;

private SimpleMatrix cov;
private SimpleMatrix xmean;

private SimpleMatrix y;
private SimpleMatrix y_topmu;
private SimpleMatrix y_w;

private SimpleMatrix populaton;
private SimpleMatrix p_c;
private SimpleMatrix p_sigma;
private Random rnd;

public EvoAlgorithm(Random rnd,ContestEvaluation e,int eval_limits,int popSize) {
        this.rnd=rnd;
        this.e = e;
        evals = 0;
        this.eval_limits = eval_limits;
        this.lambda = popSize;
}
public void Initialize(){
        cov=SimpleMatrix.identity(DIM);                 //cov is initialized into identity matrix
        sigma=0.3;                                        //stepsize equals to 1 at the beginning
        xmean=SimpleMatrix.random_DDRM(DIM,1,-5,5,rnd); //mean vector is initialized randomly

        // Parameter setting for Selection
        mu=(int)Math.floor(lambda/2);
        //calc weights
        weights=new double[mu];
        double sum=0;
        for(int i=0;i<mu;i++){
          weights[i]=Math.log(mu/(i+1));
          sum+=weights[i];
        }
        //Normalize weights
        for(int i=0;i<mu;i++){
          weights[i]=weights[i]/sum;
        }
        //calc mu_w=1/sum{w^2}
        mu_w=0;
        for(int i=0;i<mu;i++){
          mu_w+=(weights[i]*weights[i]);
        }
        mu_w=1/mu_w;

        // Parameter setting for Evolution
        p_c=new SimpleMatrix(10,1);
        c_mu=mu_w/DIM/DIM;
}

// xi = m + σ yi, yi ∼ Ni(0, C), for i = 1, . . . , λ sampling
public void SampleNewGeneration() {
        y=SimpleMatrix.randomNormal(cov,rnd);                  //y ~ N(0, C)  first element
        populaton=xmean.plus(y.scale(sigma));                        //x = m + sigma * y   first element
        for(int i=1; i<lambda; i++) {                                         //pop [rows = DIM , cols = lambda ]
                SimpleMatrix y_i=SimpleMatrix.randomNormal(cov,rnd);
                SimpleMatrix x_i=xmean.plus(y_i.scale(sigma));
                y=y.combine(0,y.numCols(),y_i);
                populaton=populaton.combine(0,populaton.numCols(),x_i);
        }
}
public void CalculateFitness(){
        fitness =new double[lambda];
        genes=new double[DIM];
        for(int i=0; i<lambda; i++) {
                for(int j=0; j<DIM; j++) {
                        genes[j]=populaton.get(j,i);      //get the genes from column i
                }
                fitness[i]=(double)e.evaluate(genes);      //calculate the fitness of column i
        }
}
// m ← \sum_ {i=1}^\mu wi * x_{i:λ} = m + σy_w where y_w = \sum_
// {i=1}^\mu*wi y_{i:λ}
// update mean
public void updateMean() {
  //Sort the fitness
  //very basic bubble sort but easy to get the index
  int index[]=new int[lambda];int tmp_index;
  double data[]=new double[lambda];double tmp;
  for(int i=0; i<lambda; i++){
    index[i]=i;
    data[i]=fitness[i];
  }
  for(int i=0; i<lambda-1; i++) {
          for(int j=0; j<lambda-1-i; j++) {
                  if(data[j]<data[j+1]) {
                          tmp=data[j];
                          data[j]=data[j+1];
                          data[j+1]=tmp;
                          tmp_index=index[j];
                          index[j]=index[j+1];
                          index[j+1]=tmp_index;
                  }
          }
  }
  // System.out.println("max:"+fitness[index[0]]);
  //extract the y vectors for the top mu individuals
  y_topmu=y.extractVector(false,index[0]);
  for(int i=1; i<mu; i++) {
          SimpleMatrix y_i=y.extractVector(false,index[i]);
          y_topmu=y_topmu.combine(0,y_topmu.numCols(),y_i);
  }
  //calc the y_w, which will be used multiple times below
  y_w=new SimpleMatrix(DIM,1);
  //y_w=sum{y_i*weights[i]}
  for(int i=0;i<mu;i++){
    SimpleMatrix y_i=y_topmu.extractVector(false,i);
    y_w=y_w.plus(y_i.scale(weights[i]));
  }
  //xmean=xmean+sigma*y_w
  xmean=xmean.plus(y_w.scale(sigma));

}

// cumulation for C
public void evolutionPathForC() {



}
// cumulation for \sigma
public void evolutionPathForSigma() {
        // the same as before, the only thing you are gonna use about the present population is y_w
        // might need to use the C^{\frac{1}{2}}, since we are not gonna use it anymore in this iter,
        // I guess we dont need to store the result as a special variable
        // TODO: Implement an operator to calc C^{\frac{1}{2}} in utility class
}

// update C
public void updateCovariance() {
        // will mainly use three operator, transpose, matrix product, and mean wrt the first axi

        // tasi 4 for population: extract the first topmu inds, which has been done in tasi 2

}
// update of σ
public void updateSigma() {
  p_c=p_c.scale(1-c_c).plus(y_w.scale(Math.sqrt(1-(1-c_c)*(1-c_c))*Math.sqrt(mu_w)));
  SimpleMatrix ppt=p_c.mult(p_c.transpose());
  //Rank mu update
  SimpleMatrix Cov_mu=new SimpleMatrix(DIM,DIM);
  for(int i=0;i<mu;i++){
    SimpleMatrix y_i=y_topmu.extractVector(false,i);
    SimpleMatrix yiyit=y_i.mult(y_i.transpose());
    Cov_mu=Cov_mu.plus(yiyit.scale(weights[i]));
  }
  cov=cov.scale(1-c_1-c_mu).plus(ppt.scale(c_1)).plus(Cov_mu.scale(c_mu));
        // will use the norm operator
        // a side note: E\left\norm \mathscr{N}(0, I) \right\norm is √2 Γ((n+1)/2)/Γ(n/2),
        // the val has been calced beforehand, stored in `gammaExp`
}
public void run() {
        Initialize();
        while (evals < eval_limits) {
                evals+=lambda;
                SampleNewGeneration();
                CalculateFitness();
                updateMean();
                evolutionPathForC();
                evolutionPathForSigma();
                updateCovariance();
                updateSigma();
        }
}

}
