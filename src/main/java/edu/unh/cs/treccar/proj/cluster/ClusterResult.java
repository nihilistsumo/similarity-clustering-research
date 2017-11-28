package edu.unh.cs.treccar.proj.cluster;

import java.util.ArrayList;

import edu.unh.cs.treccar.Data;

public class ClusterResult {
	private int[] parents;
	private String pageID;
	private ArrayList<String> sectionIDs;
	private ArrayList<Data.Paragraph> paras;
	
	public ClusterResult(int[] p, String page, ArrayList<String> secIDs,
			ArrayList<Data.Paragraph> paras){
		this.parents = p;
		this.pageID = page;
		this.sectionIDs = secIDs;
		this.paras = paras;
	}

	public int[] getParents() {
		return parents;
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
}