package edu.unh.cs.treccar.proj.similarities;

import java.util.ArrayList;

import edu.unh.cs.treccar.Data;
import edu.unh.cs.treccar.proj.util.ParaPair;
import edu.unh.cs.treccar.proj.util.ParaUtilities;

/**
 * This class implements the LeacockChodorow Semantic Similarity measure 
 * and returns the measure between two paragraphs.
 * @author Shubham Chatterjee
 *
 */
public class LeacockChodorowSimilarity implements SimilarityFunction
{
	private static ArrayList<String> paraText1;
	private static ArrayList<String> paraText2;
	private static ArrayList<Double> scores;
	double score = 0.0d;
	
	/**
	 * @param pp ParaPair A ParaPair object representing a pair of paragraphs
	 * @param list ArrayList<Data.Paragraph> Conatins a list of Data.Paragraph objects 
	 * @return LeacockChodorow score between two paragraphs
	 */
	
	public double simScore(ParaPair pp, ArrayList<Data.Paragraph> list)
	{
		paraText1 = ParaUtilities.getParaText1(pp, list);
		paraText2 = ParaUtilities.getParaText2(pp, list);
		
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
		scores = new ArrayList<Double>();
		for(String w1 : list1)
			for(String w2 : list2)
				scores.add(WordSemanticSimilarity.findSimilarity(w1, w2, "lc"));
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
		for(double i : list ){
			if(i<1000)
				total += i;
		}
		mean = total/list.size();
		return mean;
	}
}
