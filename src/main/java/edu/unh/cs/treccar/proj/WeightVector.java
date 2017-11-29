package edu.unh.cs.treccar.proj;
import java.util.*;

import edu.unh.cs.treccar.proj.util.ProjectWorker;
public class WeightVector
{
	
	private ArrayList<Double> weightVector = new ArrayList<Double>();
	private double fitness = 0.0d;
	
    	public WeightVector()
    	{
        	for (int i = 0; i < WeightManager.getDimension(); i++) 
        	{
            		weightVector.add(0.0);
        	}
    	}
    
    	public WeightVector(ArrayList<Double> weightVector)
    	{
        	this.weightVector = weightVector;
    	}

	// Creates a random individual
    	public void generateIndividual() 
    	{
        	// Loop through all our weights in the initial weight vector and add them to our vector of weights
        	for (int weightIndex = 0; weightIndex < WeightManager.getDimension(); weightIndex++) 
        	{
         	 	setWeight(weightIndex, WeightManager.getWeight(weightIndex));
        	}
        	// Randomly reorder the tour except the first and last cities
	   	Collections.shuffle(weightVector);
    	}

	// Gets a weight from the vector
    	public double getWeight(int vectorPosition) 
    	{
        	return (double)weightVector.get(vectorPosition);
    	}

    	// Sets a weight in a certain position within a vector
    	public void setWeight(int weightPosition, double wt) 
    	{
        	weightVector.set(weightPosition, wt);
        	// If the tours been altered we need to reset the fitness and distance
        	fitness = 0;
    	}
    	private  double[] convertDoubles(List<Double> doubles)
    	{
    	    double[] ret = new double[doubles.size()];
    	    Iterator<Double> iterator = doubles.iterator();
    	    int i = 0;
    	    while(iterator.hasNext())
    	    {
    	        ret[i] = iterator.next();
    	        i++;
    	    }
    	    return ret;
    	}
	// Gets the tours fitness
    	public double getFitness()
    	{
        	if (fitness == 0) 
        	{
        		double[] arr = convertDoubles(weightVector);
		    	//fitness = ProjectWorker.runClusteringOnTest(arr);
		    	System.out.println("fitness="+fitness);
        	}

        	return fitness;
    	}
	
    	// Get number of cities on our tour
    	public int getWeightVectorSize() 
    	{
       		 return weightVector.size();
    	}
    
    	// Check if the weight vector contains a weight
    	public boolean containsWeight(double weight)
    	{
        	return weightVector.contains(weight);
    	}
    
    	@Override
    	public String toString() 
    	{
        	String geneString = "|";
        	for (int i = 0; i < getWeightVectorSize(); i++) 
        	{
            		geneString += getWeight(i)+"|";
        	}
        return geneString;
    	}
}