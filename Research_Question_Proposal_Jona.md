# Research Question Proposel

## from Jonathan Mitnik

The general domain of my proposal is to switch focus from fitness-focused search to novelty-based. The only objective thus will be to find novel solutions, not towards any particular final objective, but to only make sure that new children are different from anything which came before.

[One such paper](http://sinas-indonesia.org/wp-content/uploads/2013/01/ABANDONING-OBJECTIVES-EVOLUTION-THROUGH-THE-SEARCH-FOR-NOVELTY-ALONE.pdf) explains the idea in more layman terms. Following the fitness function, and landing in some local optimum, can often be deceptive: novelty does away with any such traps, and rewards the exploration as much as possible.

We could adapt a Novelty Search Algorithm, such as NEAT, to this problem to see whether the eventual score is met. This algorithm at its core is simple: whenever we have new offspring, we can evaluate how novel the offspring is. By looking at the distance from such an offspring to k of his nearest neighbours, if it exceeds a certain threshold for novelty (also variable), it has reached a new level of novelty. By keeping population size constant, we can spread our population to novel and low-density aread.

Our main research question would thus be the exploration of whether an eventual evaluation of our most novel solutions can in any way improve performance over simple fitness-based evaluations.

### Potential Research Questions
1. **Combining Novelty-search with fitness-valued functions**: Upon finding novel solutions on half-time of usual runtime, will exploitation by usage of crossover and/or mutation result in in fast different metrics of global optimum?
2. **Varying population size**: What if we explore novelty with a small population? Would using less candidates lead to quicker resolution of finding potential more interesting candidates?
3. **Combining Novelty-search with fitness-valued function by alternating**: Perhaps a bit of a simulation of how countries in real-life jump between having better armies, and then racing for other areas. Every (n+1 / 2) generation, we look for candidates with the highest fitness values. Next n generations, we set the focus back to novelty search: these can be parameters. Playing with parameters like this, we can see if exploiting both values can result in ignoring optimal values the easiest.

