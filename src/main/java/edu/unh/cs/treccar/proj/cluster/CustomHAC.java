package edu.unh.cs.treccar.proj.cluster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;

import edu.unh.cs.treccar.Data;
import edu.unh.cs.treccar.proj.similarities.SimilarityFunction;
import edu.unh.cs.treccar.proj.util.ParaPairData;

public class CustomHAC {
	public HashMap<String, ArrayList<String>> clusterData;
	public HashMap<HashSet<String>, ArrayList<Double>> clusterPairData;
	public Properties prop;
	public String pageID;
	
	public CustomHAC(Properties p, String pID, double[] w, ArrayList<SimilarityFunction> funcs, 
			ArrayList<String> sectionIDs, ArrayList<Data.Paragraph> paras, ArrayList<ParaPairData> ppdList){
		this.prop = p;
		this.pageID = pID;
		
	}

	public HashMap<String, ArrayList<String>> cluster(ArrayList<Data.Paragraph> paras, 
			ArrayList<ParaPairData> ppdList, double[] optw, ArrayList<String> secIDs){
		
		// Initialization //
		HashMap<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();
		HashMap<HashSet<String>, ArrayList<Double>> clusterPairData = new HashMap<HashSet<String>, ArrayList<Double>>();
		Integer clusterID = 1;
		for(Data.Paragraph p:paras){
			ArrayList<String> paraList = new ArrayList<String>();
			paraList.add(p.getParaId());
			result.put(clusterID.toString(), paraList);
			clusterID++;
		}
		for(ParaPairData ppd:ppdList){
			String p1 = ppd.getParaPair().getPara1();
			String p2 = ppd.getParaPair().getPara2();
			String c1 = "", c2 = "";
			for(String c:result.keySet()){
				if(p1.equals(result.get(c))){
					c1 = c;
					break;
				}
			}
			for(String c:result.keySet()){
				if(p2.equals(result.get(c))){
					c2 = c;
					break;
				}
			}
			HashSet<String> clusterPairKey = new HashSet<String>();
			clusterPairKey.add(c1);
			clusterPairKey.add(c2);
			clusterPairData.put(clusterPairKey, ppd.getSimScoreList());
		}
		// ------------ //
		
		boolean isConverged = false;
		while(!isConverged){
			HashSet<String> clusterPairMax = null;
			double maxScore = 0.0;
			for(HashSet<String> cp:clusterPairData.keySet()){
				double score = 0.0;
				for(int i=0; i<optw.length; i++)
					score+=clusterPairData.get(cp).get(i)*optw[i];
				if(score>maxScore){
					clusterPairMax = cp;
					maxScore = score;
				}
			}
			Iterator<String> it = clusterPairMax.iterator();
			String mergeC1 = it.next();
			String mergeC2 = it.next();
			
		}
		
		return result;
	}
}
