import pandas as pd
import numpy as np
import matplotlib as plot
import sklearn as sk
import importlib
import seaborn as sns
import os
import subprocess

from config import *
from helpers import *

class Simulator:
    """A simulator for a givenn number of setups, and runs to test on each parameter value of a setup.

    Params:
        - setups(list): A list of Setup to test on
        - runs(int): Number of runs per setup
    """
    
    def __init__(self, setups, runs=10):
        self.setups = setups
        self.runs = runs

    def run(self):
        for setup in self.setups:
            self.runEvalsForSetup(setup)

    def constructParams(self, setup, param):
        """Sets up default and preconfigured parameters."""
        setup.evaluation.parameters[setup.param_name] = param

    def runEvalsForSetup(self, setup):
        """Runs for the current Setup:setup of parameters to test. """
        for param in setup.parameter_values:
            self.runEval(setup, param)

    def runEval(self, setup, param):
        """Runs the EA on the igiven eval"""
        scores = 0
        print(scores)
        
        for i in range(self.runs):
            subprocess.run("bash run.sh clean", shell=True)
            subprocess.run("bash run.sh compile", shell=True)
            cmd = "bash run.sh %s %d %f %d" % (setup.evaluation.name, setup.evaluation.parameters[PARAMETER_POPSIZE], setup.evaluation.parameters[PARAMETER_SIGMA], setup.evaluation.parameters[PARAMETER_MOMENTUM])
            p = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
            output = p.stdout.readlines()[0]
            self.storeDataToFiles(output, setup, param, i)
            
    def storeDataToFiles(self, output, setup, param, run_nr):
        """Stores output to corresponding setup location."""
        path = PATH_TO_LOG + "{}/{}/{}-val-{}-run-{}.txt".format(setup.evaluation.name, setup.param_name, setup.param_name, param, run_nr)
        makeDirStructure(path)
        
        file = open(path, "w+")
        file.write(str(output.decode()))
        file.close()
    
    def readDataForSetup(self, setup):
        """Reads prior-run setup from filesystem."""
        end_df = pd.DataFrame()

        for index, val_metric in enumerate(setup.parameter_values):
            df = pd.DataFrame()
            
            for i in range(self.runs):
                path = PATH_TO_LOG + "{}/{}/{}-val-{}-run-{}.txt".format(setup.evaluation.name, setup.param_name, setup.param_name, val_metric, i)

                with open(path) as file:
                    output = file.readlines()

            output_reals = eval(output[0])
            df[index] = output_reals

            end_df[index] = df.mean(axis='columns')
            
        return end_df
    
    def generatePlotForSetup(self, setup, start_index=0, end_index=-1, threshold=10.0, output_data=pd.DataFrame()):
        """Generates a plot, assuming your setup has been run and stored in storage."""
        path = PATH_TO_PLOTS + "{}/{}-parameters.png".format(setup.evaluation.name, setup.param_name)
        
        if end_index < 0:
            end_index = output_data.size - 1
        
        if output_data.size < 1:
            output_data = self.readDataForSetup(setup)
        
        my_palette = sns.color_palette("rocket_r", 6)

        sns.relplot(hue="coherence", kind="line",palette=my_palette, legend=False, data=output_data[output_data < threshold].iloc[start_index:])
        plt.title("{} values for {} over {} runs".format(setup.param_name, setup.evaluation.name, self.runs))
        plt.legend(["beta="+str(i) for i in setup.parameter_values])
        plt.xlabel("Evaluation")
        plt.ylabel("Best Fitness")
        
        makeDirStructure(path)
        plt.savefig(path)
        