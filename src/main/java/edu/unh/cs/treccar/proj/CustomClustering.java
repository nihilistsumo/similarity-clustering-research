package edu.unh.cs.treccar.proj;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Properties;

import edu.unh.cs.treccar.Data;

public class CustomClustering {
	
	public Properties pr;
	private String pageID;
	private ClusterResult cr;
	
	public CustomClustering(Properties prop, String pageID, 
			ArrayList<String> sectionIDs, ArrayList<Data.Paragraph> paras, ArrayList<ParaPairData> ppdList){
		this.pr = prop;
		this.pageID = pageID;
		try {
			this.cr = this.cluster(pageID, sectionIDs, paras, ppdList);
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
	
	private ClusterResult cluster(String pageID, ArrayList<String> sectionIDs, ArrayList<Data.Paragraph> paras, ArrayList<ParaPairData> ppdList) throws FileNotFoundException, IOException, ClassNotFoundException{
		int simCount = Integer.parseInt(this.pr.getProperty("sim-fet-count"));
		ArrayList<String> paraids = DataUtilities.getOrderedParaIDArray(paras);
		double[] w = new double[simCount];
		double[] optW = new double[simCount];
		double[] best = new double[simCount];
		String para1, para2;
		int para1index, para2index;
		int[][] parentsForSim = new int[simCount][paraids.size()];
		ArrayList<ArrayList<String>> gtClusters = DataUtilities.getGTClusters(
				pageID, this.pr.getProperty("data-dir")+"/"+this.pr.getProperty("hier-qrels"));
		double score = 0, mergeScore = 0;
		double threshold = Double.parseDouble(this.pr.getProperty("threshold"));
		for(int i=0; i<simCount; i++){
			w[i] = 0.0;
			optW[i] = 0.0;
			best[i] = 0.0;
		}
		for(int i=0; i<simCount; i++){
			for(int j=0; j<=10; j++){
				w[i] = (double)j/10;
				int[] parents = new int[paraids.size()];
				for(int m=0; m<parents.length; m++)
					parents[m] = m;
				for(ParaPairData ppData:ppdList){
					score = ppData.getSimScoreList().get(i)*w[i];
					if(score>threshold){
						//merge them
						para1 = ppData.getParaPair().getPara1();
						para2 = ppData.getParaPair().getPara2();
						para1index = paraids.indexOf(para1);
						para2index = paraids.indexOf(para2);
						parents[para2index]
								= parents[para1index];
					}
				}
				//compare merged clusters with gt
				mergeScore = this.mergeScore(gtClusters, paraids, parents);
				//update best and optW if necessary
				if(mergeScore>best[i]){
					best[i] = mergeScore;
					optW[i] = w[i];
					parentsForSim[i] = parents;
				}
			}
		}
		ClusterResult r = new ClusterResult(optW, pageID, sectionIDs, paras, parentsForSim);
		return r;
	}
}
