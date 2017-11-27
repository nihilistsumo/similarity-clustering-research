/**
* Population.java
* Manages a population of candidate weight vectors
*/
package edu.unh.cs.treccar.proj;
public class Population 
{
    // Holds population of tours
    WeightVector[] weightVectors;
    // Construct a population
    public Population(int populationSize, boolean initialise) 
    {
        weightVectors = new WeightVector[populationSize];
        // If we need to initialise a population of weightVectors do so
        if (initialise) 
        {
            // Loop and create individuals
            for (int i = 0; i < populationSize(); i++) 
            {
                WeightVector newVector = new WeightVector();
                newVector.generateIndividual();
                saveWeightVector(i, newVector);
            }
        }
        
    }
    
    // Saves a vector
    public void saveWeightVector(int index, WeightVector weight) 
    {
        weightVectors[index] = weight;
    }
    
    // Gets a weight vector from population
    public WeightVector getWeightVector(int index) 
    {
        return weightVectors[index];
    }

    // Gets the best weight vector in the population
    public WeightVector getFittest()
    {
        WeightVector fittest = weightVectors[0];
        // Loop through individuals to find fittest
        for (int i = 1; i < populationSize(); i++) 
        {
            if (getWeightVector(i).getFitness()<=fittest.getFitness()) 
            {
                fittest = getWeightVector(i);
            }
        }
        return fittest;
    }

    // Gets population size
    public int populationSize() 
    {
        return weightVectors.length;
    }
}