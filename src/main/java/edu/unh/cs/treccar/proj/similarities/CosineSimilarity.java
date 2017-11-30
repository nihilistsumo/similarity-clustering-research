package edu.unh.cs.treccar.proj.similarities;

import java.util.ArrayList;
import edu.unh.cs.treccar.proj.util.*;

import edu.unh.cs.treccar.Data;

public class CosineSimilarity extends BaseStats implements SimilarityFunction
{
	private static ArrayList<String> paraText1;
	private static ArrayList<String> paraText2;
	private static ArrayList<Double> vector1;
	private static ArrayList<Double> vector2;

	double score = 0.0d;
	
	public double simScore(ParaPair pp, ArrayList<Data.Paragraph> list)
	{
		paraText1 = ParaUtilities.getParaText1(pp, list);
		paraText2 = ParaUtilities.getParaText2(pp, list);
		
		vector1 = getNormalisedTermVector(paraText1, "lnc" , list);
		vector2 = getNormalisedTermVector(paraText2, "ltn" , list);
		
		for(double i : vector1 )
			for(double j : vector2)
				score += i*j;
		return score;
	}
}
