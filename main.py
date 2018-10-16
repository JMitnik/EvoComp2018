from Simulator import Simulator
from Setup import Setup

EVALUATIONS = ["SphereEvaluation", "BentCigarFunction",
               "SchaffersEvaluation", "KatsuuraEvaluation"]

populationSetup = Setup("Mutation", [
    i for i in range(100, 1000, 100)
])

momentumSetup = Setup("Momentum", [
    i for i in range(0, 1, 0.1)
])

sigmaSetup = Setup("Sigma", [
    # Sigma values
])

setups = [populationSetup, momentumSetup, sigmaSetup]

sim = Simulator(setups, 10)
sim.run()
