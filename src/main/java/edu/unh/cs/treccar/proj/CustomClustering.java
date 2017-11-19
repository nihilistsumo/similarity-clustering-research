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
	
	public CustomClustering(Properties prop, String pageID, 
			ArrayList<String> sectionIDs, ArrayList<Data.Paragraph> paras){
		this.pr = prop;
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
	
	private void cluster(String pageID, ArrayList<String> sectionIDs, ArrayList<Data.Paragraph> paras) throws FileNotFoundException, IOException, ClassNotFoundException{
		int simCount = Integer.parseInt(this.pr.getProperty("sim-fet-count"));
		double[] w = new double[simCount];
		double[] optW = new double[simCount];
		double[] best = new double[simCount];
		ArrayList<String> paraids = DataUtilities.getOrderedParaIDArray(paras);
		ArrayList<ArrayList<String>> gtClusters = DataUtilities.getGTClusters(
				pageID, this.pr.getProperty("data-dir")+"/"+this.pr.getProperty("hier-qrels"));
		double score = 0, mergeScore = 0;
		double threshold = Double.parseDouble(this.pr.getProperty("threshold"));
		ObjectInputStream ois = new ObjectInputStream(
				new FileInputStream(
						this.pr.getProperty("out-dir")+"/"+pageID+"data"));
		ArrayList<ParaPairData> ppd = (ArrayList<ParaPairData>)ois.readObject();
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
				for(ParaPairData ppData:ppd){
					score = ppData.getSimScoreList().get(i)*w[i];
					if(score>threshold){
						//merge them
						parents[paraids.indexOf(ppData.getParaPair().getPara2())]
								= parents[paraids.indexOf(ppData.getParaPair().getPara1())];
					}
				}
				//compare merged clusters with gt
				mergeScore = this.mergeScore(gtClusters, paraids, parents);
				//update best and optW if necessary
				if(mergeScore>best[i]){
					best[i] = mergeScore;
					optW[i] = w[i];
				}
			}
		}
	}
}
