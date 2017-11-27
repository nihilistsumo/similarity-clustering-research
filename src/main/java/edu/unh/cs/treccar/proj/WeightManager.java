package edu.unh.cs.treccar.proj;
import java.util.ArrayList;

public class WeightManager 
{

    // Holds our weights
    public static ArrayList<Double> initialWeight = new ArrayList<Double>();

    // Adds a weight
    public static void addWeight(double weight) 
    {
        initialWeight.add(weight);
    }
    
    // Get a weight
    public static double getWeight(int index)
    {
        return (double)initialWeight.get(index);
    }
    
    // Get the dimension of the vector
    public static int getDimension()
    {
        return initialWeight.size();
    }
}
