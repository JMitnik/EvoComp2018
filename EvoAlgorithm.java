import java.util.*;
import org.vu.contest.ContestEvaluation;
import org.ejml.simple.*;
import org.ejml.data.*;
public class EvoAlgorithm {
private static int DIM = 10;
// private static double gammaExp = 3.084327759799864;
private ContestEvaluation e;

//lambda-->populaton size/offspring number
private int lambda;
//mu-->number of parets/points for recombination
private int mu;
private double mu_ratio;
private int index;
//weights
private double[] weights;
// $\mu_w$
private double mu_w;
// sigma
private double sigma;

// c_c
private double c_c;
// c_sigma
private double c_sigma;
// c_1
private double c_1;
// c_mu
private double c_mu;
// d_\sigma
private double d_sigma;
//E(N(0,I))
private double E;

private double epsilon;

private double beta;

// fitness
private double[] fitness;
private double[] genes;
private int evals;
private int eval_limits;

private SimpleMatrix cov;
private SimpleMatrix cov_old;
private SimpleMatrix cov_mu;
private SimpleMatrix cov_invsqrt;
private SimpleMatrix xmean;

private SimpleMatrix y;
private SimpleMatrix y_topmu;
private SimpleMatrix y_w;

private SimpleMatrix populaton;
private SimpleMatrix p_c;
private SimpleMatrix p_sigma;

private SimpleMatrix meanUpdate;
private Random rnd;
private List<Double> fitnessValues;

public EvoAlgorithm(Random rnd,ContestEvaluation e,int eval_limits,int popSize,double mu_ratio,double sigma,int index, double beta) {
        this.rnd=rnd;
        this.e = e;
        evals = 0;
        this.eval_limits = eval_limits;
        this.lambda = popSize;
        this.mu_ratio=mu_ratio;
        this.sigma=sigma;
        this.index=index;
        this.beta = beta;
}
public void Initialize(){
        cov=SimpleMatrix.identity(DIM);                 //cov is initialized into identity matrix
        cov_old=SimpleMatrix.identity(DIM);
        c_sigma=(4.0/DIM);
        d_sigma=3.0;
        rnd=new Random();
        // without hidden labour
        // xmean=SimpleMatrix.random_DDRM(DIM,1,-5,5,rnd); //mean vector is initialized randomly
        // xmean=new SimpleMatrix(DIM,1);
        // with hidden labour
        double data[][]={
                {0,0,0,0,0,0,0,0,0,0},
                {-0.892,3.991,0.171,-3.80,-0.469,-2.0887,1.3839,-0.7344,1.1456,-0.304},
                {3.65,2.54,-1.52,1.46,1.39,-1.90,3.50,-2.35,-0.38,-2.03},
                {-0.04262,-0.19543,-0.02582,-0.11764,0.16344,0.05481,0.11488,-0.11648,0.02446,-0.03941}
        };
        // xmean=new SimpleMatrix(10,1,false,data[index]);
        xmean=new SimpleMatrix(DIM,1);
        // Parameter setting form  Selection
        mu=(int)Math.floor(lambda*mu_ratio);
        //calc weights
        weights=new double[mu];
        double sum=0;
        for(int i=0; i<mu; i++) {
                weights[i]=Math.log(mu/(i+1));
                sum+=weights[i];
        }
        //Normalize weights
        for(int i=0; i<mu; i++) {
                weights[i]=weights[i]/sum;
        }
        //calc mu_w=1/sum{w^2}
        mu_w=0;
        for(int i=0; i<mu; i++) {
                mu_w+=(weights[i]*weights[i]);
        }
        mu_w=1/mu_w;

        // Parameter setting for Evolution
        p_c=new SimpleMatrix(10,1);
        p_sigma=new SimpleMatrix(10,1);
        c_c= 4.0 / DIM;
        c_1= 2.0/DIM/DIM;
        c_mu=mu_w/DIM/DIM;
        E=Math.sqrt(DIM)*(1-1/(4*DIM)+1/(22*DIM*DIM));
        
        meanUpdate=new SimpleMatrix(DIM,1);
        epsilon = 0.01;
        fitnessValues = new ArrayList<Double>();
}

public void SampleNewGeneration() {
        cov=cov.plus(cov.transpose()).scale(0.5);

        try{
                y=SimpleMatrix.randomNormal(cov,rnd);
        }
        catch (Exception e) {
                //sample with the cov_old
                y=SimpleMatrix.randomNormal(cov_old,rnd);
                cov.set(cov_old);
        }

        populaton=xmean.plus(meanUpdate).plus(y.scale(sigma));                            //x = m + sigma * y   first element
        for(int i=1; i<lambda; i++) {                                             //pop [rows = DIM , cols = lambda ]
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

        for (double f: fitness) {
                fitnessValues.add(f);
        }
}
// update mean
public void updateMean() {
        xmean=xmean.plus(meanUpdate.scale(sigma));        
}


// cumulation for C
public void evolutionPathForC() {
        //calculate p_c
        p_c=p_c.scale(1-c_c).plus(y_w.scale(Math.sqrt(1-(1-c_c)*(1-c_c))*Math.sqrt(mu_w)));
        //calculate C_mu for Rank mu update
        cov_mu=new SimpleMatrix(DIM,DIM);
        for(int i=0; i<mu; i++) {
                SimpleMatrix y_i=y_topmu.extractVector(false,i);
                SimpleMatrix yiyit=y_i.mult(y_i.transpose());
                cov_mu=cov_mu.plus(yiyit.scale(weights[i]));
        }
}

// momentum for mean
public void evolutionPathForMean() {
        //Sort the fitness
        //very basic bubble sort but easy to get the index
        int index[]=new int[lambda]; int tmp_index;
        double data[]=new double[lambda]; double tmp;
        for(int i=0; i<lambda; i++) {
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
        for(int i=0; i<mu; i++) {
                SimpleMatrix y_i=y_topmu.extractVector(false,i);
                y_w=y_w.plus(y_i.scale(weights[i]));
        }
        meanUpdate = y_w.plus(beta, meanUpdate);
}
//calc C^(-1/2)
public void Calc_Cov_Invsqrt(){
        // calc cov_invsqrt
        SimpleEVD<SimpleMatrix> Eigen=cov.eig();
        List<Complex_F64> Eigenvalues=Eigen.getEigenvalues();
        //B is the matrix of the eigenvectors
        SimpleMatrix B=Eigen.getEigenVector(0);
        for(int i=1; i<DIM; i++) {
                B=B.combine(0,B.numCols(),Eigen.getEigenVector(i));
        }
        //D is a diagonal matrix of the sqrt of eigenvalues
        double D_val[]=new double[DIM];
        for(int i=0; i<DIM; i++) {
                D_val[i]=1/Math.sqrt(Eigenvalues.get(i).getReal());
        }
        SimpleMatrix D=SimpleMatrix.diag(D_val);
        cov_invsqrt=B.mult(D).mult(B.transpose());  //BDB'
}
// update C
public void updateCovariance() {
        SimpleMatrix ppt=p_c.mult(p_c.transpose());
        //Rank-one update
        // cov=cov.scale(1-c_1).plus(ppt.scale(c_1));
        cov_old.set(cov);
        cov=cov.scale(1-c_1-c_mu).plus(ppt.scale(c_1)).plus(cov_mu.scale(c_mu));
        //enhance symmetry
        cov=cov.plus(cov.transpose()).scale(0.5);
}

public void updateSigma() {
        Calc_Cov_Invsqrt();
        SimpleMatrix tmp=cov_invsqrt.mult(y_w).scale(Math.sqrt(1-(1-c_sigma)*(1-c_sigma))*Math.sqrt(mu_w));
        p_sigma=p_sigma.scale(1-c_sigma).plus(tmp);
        double p_sigma_len=0;
        for(int i=0; i<DIM; i++) {
                p_sigma_len+=Math.pow(p_sigma.get(i,0),2);
        }
        p_sigma_len=Math.sqrt(p_sigma_len);
        sigma=sigma*Math.exp(c_sigma/d_sigma*(p_sigma_len/E-1));
}

public void run() {
        Initialize();
        while (evals < eval_limits-lambda) {
                evals+=lambda;
                SampleNewGeneration();
                CalculateFitness();
                evolutionPathForMean();
                updateMean();
                evolutionPathForC();
                updateCovariance();
                updateSigma();
        }

        // Important: nets result for the Python data analysis after the results are all done
        System.out.println(fitnessValues);
}

}
