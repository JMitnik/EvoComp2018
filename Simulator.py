import os
import subprocess

STANDARD_MUTATION = 0.5
STANDARD_MOMENTUM = 0
STANDARD_POPSIZE = 100
STANDARD_SIGMA = 0.004

class Simulator:

    def __init__(self, evaluations, parameter_setups, runs=10):
        self.evaluations = evaluations
        self.parameter_setups = parameter_setups
        self.runs = runs

    def run(self):
        for setup in self.parameter_setups:
            self.runEvalsForSetup(setup)

    def constructParams(self, setup, setup_param):
        mutation = STANDARD_MUTATION
        momentum = STANDARD_MOMENTUM
        sigma = STANDARD_SIGMA
        popsize = STANDARD_POPSIZE

        if setup.name == "Mutation":
            mutation = setup_param

        if setup.name == "Momentum":
            momentum = setup_param

        if setup.name == "Sigma":
            sigma = setup_param
        
        if setup.name == "Population Size":
            popsize = setup_param

        return {
            'mutation': mutation,
            'momentum': momentum,
            'sigma': sigma,
            'popsize': popsize
        }

    def runEvalsForSetup(self, setup):
        for evaluation in self.evaluations:
            for param in setup.parameter_values:
                params = self.constructParams(setup, param)

                self.runEval(evaluation, setup, params)

    def runEval(self, evaluation, setup_params ,params):
        scores = 0
        # Perhaps somehow give seed to the Java program
        
        for i in range(self.runs):
            cmd = "bash run.sh %s %d %f" % (evaluation, params['popsize'], params['sigma'])
            p = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
            output = float(p.stdout.readlines()[0][7:])
            scores += output
            retval = p.wait()
            print(output)

    # TODO: Write plot method for a given array of {parameter-size, evaluation-score}
    # Possibly include different lines with different values of Beta already
    # Sigma does not matter then as much, I suppose