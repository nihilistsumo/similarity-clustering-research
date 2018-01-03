package edu.unh.cs.treccar.proj.similarities;

import java.util.ArrayList;

import edu.unh.cs.treccar.Data;
import edu.unh.cs.treccar.proj.util.ParaPair;
import edu.unh.cs.treccar.proj.util.ParaUtilities;

/**
 * This class implements the Path Semantic Similarity measure 
 * and returns the measure between two paragraphs.
 * @author Shubham Chatterjee
 *
 */

public class PathSimilarity implements SimilarityFunction
{
	private static ArrayList<String> paraText1;
	private static ArrayList<String> paraText2;
	private static ArrayList<Double> scores;
	double score = 0.0d;
	
	/**
	 * @param pp ParaPair A ParaPair object representing a pair of paragraphs
	 * @param list ArrayList<Data.Paragraph> Conatins a list of Data.Paragraph objects 
	 * @return Path score between two paragraphs
	 */
	
	public double simScore(ParaPair pp, ArrayList<Data.Paragraph> list)
	{
		paraText1 = pp.getPara1tokens();
		paraText2 = pp.getPara2tokens();
		
		score = getParaScore(paraText1, paraText2);
		
		return score;
	}
	
	/**
	 * 
	 * @param list1 ArrayList<String> List of words from first paragraph
	 * @param list2 ArrayList<String> List of words from second paragraph
	 * @return similarity score between two lists
	 */
	
	private static double getParaScore(ArrayList<String> list1, ArrayList<String> list2)
	{
		double s = 0.0d;
		for(String w1 : list1)
			for(String w2 : list2)
				scores.add(WordSemanticSimilarity.findSimilarity(w1, w2, "path"));
		s = findMeanScore(scores);
		return s;
	}
	
	/**
	 * 
	 * @param list ArrayList<Double> List of scores between words of two paragraphs
	 * @return mean of the score
	 */
	
	private static double findMeanScore(ArrayList<Double> list)
	{
		double total = 0.0d;
		double mean = 0.0d;
		for(double i : list )
			total += i;
		mean = total/list.size();
		return mean;
	}
}

