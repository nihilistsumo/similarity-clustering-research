package edu.unh.cs.treccar.proj.similarities;
import java.util.Locale;
import edu.unh.cs.treccar.Data;
import edu.unh.cs.treccar.proj.util.ParaPair;
import edu.unh.cs.treccar.proj.util.ParaUtilities;
import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;

public class FuzzyScore
{
	private final Locale locale;
	private static ArrayList<String> paraText1;
	private static ArrayList<String> paraText2;
	private static ArrayList<Integer> scores;
	private static int score;
	
	public FuzzyScore(final Locale locale) {
        if (locale == null) {
            throw new IllegalArgumentException("Locale shouldn't be null");
        }
        this.locale = locale;
	}
	/** FuzzyScore.java
     *  @author ajesh
     */
	
	/**
	 * @param1 pp ParaPair: ParaPair object representing a pair of paragraphs
	 * @param2 list ArrayList<Data.Paragraph> Contains a list of Data.Paragraph objects
	 * 
	 * @return Mean distance between two paragraphs
	 */
	
	public double simScore(ParaPair pp, ArrayList<Data.Paragraph> list)
	{
		paraText1 = ParaUtilities.getParaText1(pp, list);
		paraText2 = ParaUtilities.getParaText2(pp, list);
		
		score = (int) getParaScore(paraText1, paraText2);
		
		return score;
	}
	
	/**
	 * 
	 * @param list1 ArrayList<String> Words from first paragraph
	 * @param list2 ArrayList<String> Words from second paragraph
	 * 
	 * @return Mean Fuzzy score between two paragraphs
	 */
	private static double getParaScore(ArrayList<String> list1, ArrayList<String> list2)
	{
		double s = 0.0d;
		
		for(String w1 : list1)
		{
			for(String w2 : list2)
			{
				scores.add(score.fuzzyScore(w1, w2, Locale.ENGLISH));
			}
		}
		s = findMeanScore(scores);
		return s;
	}
	
	/**
	 * Returns the mean of the total Fuzzy Score
	 * 
	 * @param list ArrayList<Integer> list of Fuzzy score between all pairs of words
	 * 
	 * @return mean Fuzzy score
	 */
	private static double findMeanScore(ArrayList<Integer> list)
	{
		double total = 0.0d;
		double mean = 0.0d;
		for(double i : list)
			total += i;
		mean = total/list.size();
		return mean;
	}	
}