import pandas as pd
import numpy as np
import matplotlib as plot
import sklearn as sk
import importlib
from config import *

class Evaluation:
    def __init__(self, eval_name, parameters):
        self.name = eval_name
        self.parameters = parameters