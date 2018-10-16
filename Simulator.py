import os
import subprocess

class Simulator:

    def __init__(self, evaluations, parameter_setups, runs=10):
        self.runs = runs
        self.pop_size_parameters = pop_size_parameters
        self.momentum_beta_parameters = momentum_beta_parameters

    def run(self):
        # TODO: Run for each evaluation an x number of tests
        # Each test gets to run on all of the different parameters, as given to the simulator
        # Perhaps somehow give seed to the Java program

        for evaluation in EVALUATIONS:
            self.run_evals_for_evaluation(evaluation,)

    def run_evals_for_evaluation(self, evaluation, popsize, sigma):
        scores = 0
        
        for i in range(self.runs):
            cmd = "bash run.sh %s %d %f" % (evaluation, popsize, sigma)
            p = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
            output = float(p.stdout.readlines()[0][7:])
            scores += output
            retval = p.wait()
            self.scores.append()

    # TODO: Write plot method for a given array of {parameter-size, evaluation-score}
    # Possibly include different lines with different values of Beta already
    # Sigma does not matter then as much, I suppose