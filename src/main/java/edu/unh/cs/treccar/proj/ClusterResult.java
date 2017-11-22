package edu.unh.cs.treccar.proj;

import java.util.ArrayList;

import edu.unh.cs.treccar.Data;

public class ClusterResult {
	private double[] optimumWeight;
	private String pageID;
	private ArrayList<String> sectionIDs;
	private ArrayList<Data.Paragraph> paras;
	private int[][] parentsForSim;
	
	public ClusterResult(double[] weight, String page, ArrayList<String> secIDs,
			ArrayList<Data.Paragraph> paras, int[][] parents){
		this.optimumWeight = weight;
		this.pageID = page;
		this.sectionIDs = secIDs;
		this.paras = paras;
		this.parentsForSim = parents;
	}

	public double[] getOptimumWeight() {
		return optimumWeight;
	}

	public String getPageID() {
		return pageID;
	}

	public ArrayList<String> getSectionIDs() {
		return sectionIDs;
	}

	public ArrayList<Data.Paragraph> getParas() {
		return paras;
	}

	public int[][] getParentsForSim() {
		return parentsForSim;
	}

}
