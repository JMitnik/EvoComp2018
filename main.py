import numpy as np

from Simulator import Simulator
from Setup import Setup

EVALUATIONS = ["SphereEvaluation", "BentCigarFunction",
               "SchaffersEvaluation", "KatsuuraEvaluation"]

populationSetup = Setup("Mutation", np.linspace(100, 1000, 100))

momentumSetup = Setup("Momentum", np.linspace(0, 1, 0.1))

sigmaSetup = Setup("Sigma", np.linspace(0, 100))

setups = [populationSetup, momentumSetup]

sim = Simulator(EVALUATIONS, setups, 10)
sim.run()
