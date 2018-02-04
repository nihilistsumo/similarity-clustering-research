package edu.unh.cs.treccar.proj.cluster;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import edu.unh.cs.treccar.Data;
import edu.unh.cs.treccar.proj.similarities.SimilarityFunction;
import edu.unh.cs.treccar.proj.util.DataUtilities;
import edu.unh.cs.treccar.proj.util.ParaPairData;

public class CustomClustering {
	
	public Properties pr;
	private String pageID;
	private ClusterResult cr;
	
	public CustomClustering(Properties prop, String pageID, double[] w, ArrayList<SimilarityFunction> funcs, 
			ArrayList<String> sectionIDs, ArrayList<Data.Paragraph> paras, ArrayList<ParaPairData> ppdList){
		this.pr = prop;
		this.pageID = pageID;
		assert(w.length == funcs.size());
		try {
			this.cr = this.cluster(pageID, w, funcs, sectionIDs, paras, ppdList);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getPageID() {
		return pageID;
	}

	public ClusterResult getCr() {
		return cr;
	}

	private int getParent(int i, int[] parents){
		while(parents[i]!=i)
			i = parents[i];
		return i;
	}
	/*
	private double mergeScore(ArrayList<ArrayList<String>> clusters, ArrayList<String> paraids, int[] parents){
		double score = 0;
		String p1,p2;
		int pos = 0, count = 0;
		for(int i=0; i<paraids.size()-1; i++){
			p1 = paraids.get(i);
			for(int j=i+1; j<paraids.size(); j++){
				p2 = paraids.get(j);
				if(this.getParent(i, parents)==this.getParent(j, parents)){
					for(ArrayList<String> cluster:clusters){
						if(cluster.contains(p1)){
							if(cluster.contains(p2))
								pos++;
							break;
						}
					}
				}
				else{
					for(ArrayList<String> cluster:clusters){
						if(cluster.contains(p1)){
							if(!cluster.contains(p2))
								pos++;
							break;
						}
					}
				}
				count++;
			}
		}
		score = (double)pos/(double)count;
		return score;
	}
	*/
	private ClusterResult cluster(String pageID, double[] wVec, ArrayList<SimilarityFunction> f, ArrayList<String> sectionIDs, ArrayList<Data.Paragraph> paras, ArrayList<ParaPairData> ppdList) throws FileNotFoundException, IOException, ClassNotFoundException{
		int simCount = f.size();
		ArrayList<String> paraids = DataUtilities.getOrderedParaIDArray(paras);
		int pNo = paraids.size();
		int parents[] = new int[pNo];
		int pPairNo = pNo*(pNo-1)/2;
		String para1, para2;
		int para1index, para2index;
		double[] mergeScores = new double[pPairNo];
		for(int m=0; m<parents.length; m++)
			parents[m] = m;
		double score = 0, mergeScore = 0;
		double threshold = Double.parseDouble(this.pr.getProperty("threshold"));
		for(int i=0; i<pPairNo; i++){
			ArrayList<Double> simScores = ppdList.get(i).getSimScoreList();
			mergeScores[i] = 0;
			for(int j=0; j<simCount; j++){
				mergeScores[i]+=simScores.get(j)*wVec[j];
			}
			mergeScores[i]/=simCount;
			if(mergeScores[i]>threshold){
				//merge them
				para1 = ppdList.get(i).getParaPair().getPara1();
				para2 = ppdList.get(i).getParaPair().getPara2();
				para1index = paraids.indexOf(para1);
				para2index = paraids.indexOf(para2);
				parents[para2index] = parents[para1index];
			}
		}
		ClusterResult r = new ClusterResult(parents, pageID, sectionIDs, paras);
		return r;
	}
	
	private HashMap<String, ArrayList<String>> getCandSecParaMap(
			String pageID, ArrayList<String> sectionIDs, ArrayList<ArrayList<String>> paraClusters){
		HashMap<String, ArrayList<String>> secParaMap = new HashMap<String, ArrayList<String>>();
		
		return secParaMap;
	}
}
