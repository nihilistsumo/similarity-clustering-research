package edu.unh.cs.treccar.proj;

import java.util.ArrayList;

import edu.unh.cs.treccar.Data;

public interface SimilarityFunction {
	public double simScore(ParaPair pp, ArrayList<Data.Paragraph> list);
}
