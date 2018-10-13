<<<<<<< HEAD
import os 
import subprocess 
from decimal import Decimal 
Evaluations=["SphereEvaluation","BentCigarFunction","SchaffersEvaluation","KatsuuraEvaluation"] 

# run the algorithm with several parameters 
def run(evaluation,popsize,tsize): 
    scores=0 
    times=2 
    for time in range(0,times): 
        cmd="bash run.sh %s %d %d" % (evaluation,popsize,tsize) 
        p = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT) 
        scores+=Decimal(p.stdout.readlines()[0][7:]) 
        retval = p.wait() 
    # return the average score over 5 runs 
    return(scores/times) 

#main function with compilation 
def main(): 
    os.system("bash run.sh compile") 
    print(Evaluations[2]) 
    # for popsize in [50,100,200,500,1000,2000]: 
    #     print("popsize:%d"%popsize) 
    #     print(run(Evaluations[2],popsize,100)) 
    for tsize in range(80,120,10): 
        print("tsize:%d"%tsize) 
        print(run(Evaluations[2],2000,100)) 
    # print(Evaluations[1])5 
    # print(run(Evaluations[1],500,25,0.5,1)) 
    # print(Evaluations[2]) 
    # print(run(Evaluations[2],1000,10,0.2,1)) 
    # print(Evaluations[3]) 
    # print(run(Evaluations[3],1000,5,0.1,1)) 
    os.system("bash run.sh clean") 
if __name__ == '__main__': 
    main() 
=======
import os
import subprocess
from decimal import Decimal
Evaluations=["SphereEvaluation","BentCigarFunction","SchaffersEvaluation","KatsuuraEvaluation"]
# run the algorithm with several parameters
def run(evaluation,popsize,sigma):
    scores=0
    times=10
    for time in range(0,times):
        cmd="bash run.sh %s %d %f" % (evaluation,popsize,sigma)
        p = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
        scores+=Decimal(p.stdout.readlines()[0][7:])
        retval = p.wait()
    # return the average score over 5 runs
    return(scores/times)
#main function with compilation
def main():
    # no need to compile all the time
    os.system("bash run.sh compile")
    print(Evaluations[1])
    print(run(Evaluations[1],20,0.004))
    os.system("bash run.sh clean")
if __name__ == '__main__':
    main()
>>>>>>> c38021122c83666df3c83ad837683bd99217092a
