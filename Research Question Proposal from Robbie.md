## **Research Question Proposal**
<h3 style="color:#6666FF;text-align:right;">From Robbie</h3>

### **1. Changing the strategy according to the distribution of the fitness in every generation.**

For each operator of the Evolutionary Algorithm(EA), we mostly choose a single one
strategy that has always been used during the whole evolution. But we can actually change it during different period of the evolution.
It doesn't seem like a strategy can always have the best performance during the whole progress.
We could choose new strategies in a new generation.
But we don't know when we should choose a new one and how to find the best one.

Perhaps we could focus on finding the connection between the performance of strategies and the distributions of the fitness.
For instance, by calculating the variance of the fitness, we can apply different pressure for selection.
We can always apply the best strategy by analyzing the distribution of the fitness in every generation of the population.

### **2. Considering using a variable possibility for the crossover and mutation.**

Optimizing the possibility for the mutation and crossover can give a great improvement to the performance of the Evolutionary Algorithm.
Typically we use fixed possibilities for crossover and mutation.
But actually we can come up with some strategies using variable possibilities.
It means that the possibilities can be changed as time goes by.
In this case, we could focus on finding a formula describing how the possibility could be changed according to the number of generations.

### **3. Implement another EA for optimizing the parameters.**

If we determined the strategies for the EA, the first thing we like to do is finding the best parameters. However, the parameters are randomly chosen and combined to each other, which means that we can't determine the best parameters in a simple way.
Then we could implement another EA for optimizing the parameters. We take the parameters as the inputs and the performance of the original EA as the output.
A fantastic group of parameters may be found in this way.
