package edu.unh.cs.treccar.proj.train;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import edu.unh.cs.treccar.Data;
import edu.unh.cs.treccar.proj.similarities.SimilarityFunction;
import edu.unh.cs.treccar.proj.util.DataUtilities;
import edu.unh.cs.treccar.proj.util.ParaPair;

public class RankLibFileCreator {
	public Properties pr;
	public ArrayList<SimilarityFunction> simFunctions;
	
	public RankLibFileCreator(Properties prop, ArrayList<SimilarityFunction> fList){
		this.pr = prop;
		this.simFunctions = fList;
	}
	
	private boolean isRelevant(ArrayList<ArrayList<String>> gtClusters, String q, String p){
		for(ArrayList<String> cluster:gtClusters){
			if(cluster.contains(q)){
				if(cluster.contains(p))
					return true;
				else
					return false;
			}
		}
		return false;
	}
	
	public void printRankLibInputFile(){
		HashMap<String, ArrayList<String>> pageParaMap = DataUtilities.getArticleParasMapFromPath(this.pr.getProperty("art-qrels"));
		HashMap<String, Data.Paragraph> allParaIDParaMap = DataUtilities.getParaMapFromPath("parafile");
		
		for(String pageID:pageParaMap.keySet()){
			ArrayList<ArrayList<String>> paraIDClusters = DataUtilities.getGTClusters(pageID, pr.getProperty("hier-qrels"));
			ArrayList<String> paraIDList = pageParaMap.get(pageID);
			ArrayList<Data.Paragraph> paraObjList = new ArrayList<Data.Paragraph>();
			for(String p:paraIDList)
				paraObjList.add(allParaIDParaMap.get(p));
			Data.Paragraph qPara, para;
			ArrayList<Double> scores = new ArrayList<Double>();
			boolean isRel = false;
			String relLabel = "", fet = "";
			for(String qParaID:paraIDList){
				qPara = allParaIDParaMap.get(qParaID);
				for(String paraID:paraIDList){
					if(qParaID.equals(paraID))
						continue;
					para = allParaIDParaMap.get(paraID);
					for(SimilarityFunction f:this.simFunctions)
						scores.add(f.simScore(new ParaPair(qParaID, paraID), paraObjList));
					
					isRel = this.isRelevant(paraIDClusters, qParaID, paraID);
					
					//Ranklib format
					//3 qid:1 1:1 2:1 3:0 4:0.2 5:0 # 1A
					//
					if(isRel)
						relLabel = "1 ";
					else
						relLabel = "0 ";
					for(int i=0; i<scores.size(); i++)
						fet += (i+1)+":"+scores.get(i)+" ";
					System.out.println(relLabel+"qid:"+qParaID+" "+fet+"# "+paraID);
				}
			}
		}
	}

}
