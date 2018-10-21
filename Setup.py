import pandas as pd
import numpy as np
import matplotlib as plot
import sklearn as sk
import importlib
from config import *

class Setup:
    """A setup for a defined parameter and evaluation, with given parameter values.

    Params:
        - param_name (str): Parameter to define
        - evaluation (str): Name of evaluation function to optimise.
        - parameter_values (list): List of values for the associated parameter
    """
    def __init__(self, evaluation, param_name = "", parameter_values = []):
        self.param_name = param_name
        self.evaluation = evaluation
        self.parameter_values = parameter_values
        self.parameter_scores = []