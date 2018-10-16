import os
import subprocess
from decimal import Decimal

EVALUATIONS = ["SphereEvaluation", "BentCigarFunction",
               "SchaffersEvaluation", "KatsuuraEvaluation"]
# run the algorithm with several parameters

def run(evaluation, popsize, sigma):
    scores = 0
    times = 10

    for time in range(0, times):
        cmd = "bash run.sh %s %d %f" % (evaluation, popsize, sigma)
        p = subprocess.Popen(
            cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
        output = float(p.stdout.readlines()[0][7:])
        scores += output
        retval = p.wait()
    # return the average score over 5 runs
    return(scores/times)
# main function with compilation

def test_parameter():


def main():
    # no need to compile all the time
    os.system("bash run.sh compile")
    print(EVALUATIONS[1])
    print(run(EVALUATIONS[1], 20, 0.004))
    os.system("bash run.sh clean")
# if __name__ == '__main__':

main()
