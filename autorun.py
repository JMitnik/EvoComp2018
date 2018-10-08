import os
import subprocess
from decimal import Decimal
Evaluations=["SphereEvaluation","BentCigarFunction","SchaffersEvaluation","KatsuuraEvaluation"]
# run the algorithm with several parameters
def run(evaluation,popsize,tsize,pm,pc):
    scores=0
    for time in range(0,5):
        cmd="bash run.sh %s %d %d %f %f" % (evaluation,popsize,tsize,pm,pc)
        p = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
        scores+=Decimal(p.stdout.readlines()[0][7:])
        retval = p.wait()
    # return the average score over 5 runs
    return(scores/5)
#main function with compilation
def main():
    os.system("bash run.sh compile")
    print(Evaluations[0])
    print(run(Evaluations[0],100,5,0.1,1))
    print(Evaluations[1])
    print(run(Evaluations[1],500,25,0.5,1))
    print(Evaluations[2])
    print(run(Evaluations[2],1000,10,0.2,1))
    # print(Evaluations[3])
    # print(run(Evaluations[3],1000,5,0.1,1))
    os.system("bash run.sh clean")
if __name__ == '__main__':
    main()
