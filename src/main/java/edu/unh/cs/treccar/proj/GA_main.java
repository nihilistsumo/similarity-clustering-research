/**
* GA_main.java
* Create a vector and evolve a solution
*/
package edu.unh.cs.treccar.proj;
import java.io.*;
import java.util.*;
public class GA_main 
{
    public static void main(String[] args)throws IOException
    {
    		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        	// Create and add our weights 
        	Random r = new Random();
	       	WeightManager.addWeight(r.nextDouble());
	       	WeightManager.addWeight(r.nextDouble());
			WeightManager.addWeight(r.nextDouble());
			WeightManager.addWeight(r.nextDouble());
			WeightManager.addWeight(r.nextDouble());

			System.out.println("begin");
			System.out.println("Press any key-->");
			stdin.readLine();
	    	// Initialize population
        	Population pop = new Population(100, true);
        	System.out.println("Initial fittest vector: " + pop.getFittest());
        	System.out.println("Initial fitness:"+pop.getFittest().getFitness());

                // Evolve population for 100 generations
               	Population pop1 = GA.evolvePopulation(pop);
                for (int i = 0; i < 100; i++) 
                {
                    pop1 = GA.evolvePopulation(pop);
                }
				
            // Print final results
            System.out.println("Finished");
			System.out.println("Solution:");
			System.out.println(pop1.getFittest());
	                
			System.out.println("Final fitness:"+pop1.getFittest().getFitness());
			System.out.println("Press any key-->");
			stdin.readLine();
				
    }
}
