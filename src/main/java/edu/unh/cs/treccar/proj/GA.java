/**
* GA.java
* Manages algorithms for evolving population
*/
package edu.unh.cs.treccar.proj;
public class GA 
{

    /* GA parameters */
    private static final double mutationRate = 0.01;
    private static final int tournamentSize = 2;
    private static final boolean elitism = false;

    // Evolves a population over one generation
    public static Population evolvePopulation(Population pop)
    {
        Population newPopulation = new Population(pop.populationSize(), false);

        // Keep our best individual if elitism is enabled
        int elitismOffset = 0;
        if (elitism) 
        {
            newPopulation.saveWeightVector(0, pop.getFittest());
            elitismOffset = 1;
        }

        // Crossover population
        // Loop over the new population's size and create individuals from
        // Current population
        for (int i = elitismOffset; i < newPopulation.populationSize(); i++) 
        {
            // Select parents
            WeightVector parent1 = tournamentSelection(pop);
            WeightVector parent2 = tournamentSelection(pop);
            // Crossover parents
            WeightVector child = crossover(parent1, parent2);
            // Add child to new population
            newPopulation.saveWeightVector(i, child);
        }

        // Mutate the new population a bit to add some new genetic material
        for (int i = elitismOffset; i < newPopulation.populationSize(); i++) 
        {
            mutate(newPopulation.getWeightVector(i));
        }

        return newPopulation;
    }

    // Applies crossover to a set of parents and creates offspring
    public static WeightVector crossover(WeightVector parent1, WeightVector parent2) 
    {
        // Create new child tour
        WeightVector child = new WeightVector();

        // Get start and end sub tour positions for parent1's tour
        int startPos = (int) (Math.random() * parent1.getWeightVectorSize());
        int endPos = (int) (Math.random() * parent1.getWeightVectorSize());

        // Loop and add the sub vector from parent1 to our child
        for (int i = 0; i < child.getWeightVectorSize(); i++) 
        {
            // If our start position is less than the end position
            if (startPos < endPos && i > startPos && i < endPos) 
            {
                child.setWeight(i, parent1.getWeight(i));
            } 
	    // If our start position is larger
            else if (startPos > endPos) 
            {
                if (!(i < startPos && i > endPos)) 
                {
                    child.setWeight(i, parent1.getWeight(i));
                }
            }
        }

        // Loop through parent2's city tour
        for (int i = 0; i < parent2.getWeightVectorSize(); i++) 
        {
            // If child doesn't have the city add it
            if (!child.containsWeight(parent2.getWeight(i))) 
            {
                // Loop to find a spare position in the child's vector
                for (int ii = 0; ii < child.getWeightVectorSize(); ii++) 
                {
                    // Spare position found, add weight
                    if (child.getWeight(ii) == 0.0) 
                    {
                        child.setWeight(ii, parent2.getWeight(i));
                        break;
                    }
                }
            }
        }
        return child;
    }

    // Mutate a tour using swap mutation
    private static void mutate(WeightVector weightVector) 
    {
        // Loop through vector 
        for(int weightPos1=0; weightPos1 < weightVector.getWeightVectorSize(); weightPos1++)
        {
            // Apply mutation rate
            if(Math.random() < mutationRate)
            {
                // Get a second random position in the tour
                int weightPos2 = (int) (weightVector.getWeightVectorSize() * Math.random());

                // Get the cities at target position in tour
                double weight1 = weightVector.getWeight(weightPos1);
                double weight2 = weightVector.getWeight(weightPos2);

                // Swap them around
                weightVector.setWeight(weightPos2, weight1);
                weightVector.setWeight(weightPos1, weight2);
            }
        }
    }

    // Selects candidate tour for crossover
    private static WeightVector tournamentSelection(Population pop)
    {
        // Create a tournament population
        Population tournament = new Population(tournamentSize, false);
        // For each place in the tournament get a random candidate weight vector and
        // add it
        for (int i = 0; i < tournamentSize; i++) 
        {
            int randomId = (int) (Math.random() * pop.populationSize());
            tournament.saveWeightVector(i, pop.getWeightVector(randomId));
        }
        // Get the fittest vector
        WeightVector fittest = tournament.getFittest();
        return fittest;
    }
}
