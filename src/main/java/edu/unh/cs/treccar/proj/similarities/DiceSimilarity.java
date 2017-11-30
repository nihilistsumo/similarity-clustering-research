package edu.unh.cs.treccar.proj.similarities;

import java.util.ArrayList;
import edu.unh.cs.treccar.proj.util.*;

import edu.unh.cs.treccar.Data;

public class DiceSimilarity implements SimilarityFunction
{
	private static ArrayList<String> paraText1;
	private static ArrayList<String> paraText2;
	private static ArrayList<Double> scores;
	double score = 0.0d;
	
	/**
	 * @param pp ParaPair A ParaPair object representing a pair of paragraphs
	 * @param list ArrayList<Data.Paragraph> Conatins a list of Data.Paragraph objects 
	 * @return dice score between two paragraphs
	 */
	
	public double simScore(ParaPair pp, ArrayList<Data.Paragraph> list)
	{
		paraText1 = ParaUtilities.getParaText1(pp, list);
		paraText2 = ParaUtilities.getParaText2(pp, list);
		int c = ParaUtilities.findIntersection(paraText1, paraText2).size();
		score = (2*c)/(paraText1.size() + paraText2.size());
		
		return score;
	}
	
	

}


