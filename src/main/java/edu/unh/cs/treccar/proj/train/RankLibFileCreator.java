package edu.unh.cs.treccar.proj.train;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import edu.unh.cs.treccar.Data;
import edu.unh.cs.treccar.proj.similarities.SimilarityFunction;
import edu.unh.cs.treccar.proj.util.DataUtilities;
import edu.unh.cs.treccar.proj.util.ParaPair;
import edu.unh.cs.treccar.proj.util.ParaPairData;

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
	
	public void printRankLibInputFile() throws IOException{
		HashMap<String, ArrayList<String>> pageParaMap = DataUtilities.getTrueArticleParasMapFromPath(
				this.pr.getProperty("data-dir")+"/"+this.pr.getProperty("train-art-qrels"));
		HashMap<String, Data.Paragraph> allParaIDParaMap = DataUtilities.getParaMapFromPath(
				this.pr.getProperty("data-dir")+"/"+this.pr.getProperty("train-parafile"));
		File ranklibOut = new File(this.pr.getProperty("out-dir")+"/"+this.pr.getProperty("rlib-file"));
		BufferedWriter bw = new BufferedWriter(new FileWriter(ranklibOut));
		FileInputStream fis = new FileInputStream(new File(this.pr.getProperty("out-dir")+"/"+this.pr.getProperty("data-file")));
		ObjectInputStream ois = new ObjectInputStream(fis);
		try {
			HashMap<String, ArrayList<ParaPairData>> allScores = (HashMap<String, ArrayList<ParaPairData>>)ois.readObject();
		
			for(String pageID:pageParaMap.keySet()){
				ArrayList<ArrayList<String>> paraIDClusters = DataUtilities.getGTClusters(pageID, 
						this.pr.getProperty("data-dir")+"/"+pr.getProperty("train-qrels"));
				ArrayList<String> paraIDList = pageParaMap.get(pageID);
				ArrayList<Data.Paragraph> paraObjList = new ArrayList<Data.Paragraph>();
				ArrayList<ParaPairData> scoresInPage = allScores.get(pageID);
				for(String p:paraIDList)
					paraObjList.add(allParaIDParaMap.get(p));
				Data.Paragraph qPara, para;
				for(String qParaID:paraIDList){
					qPara = allParaIDParaMap.get(qParaID);
					for(String paraID:paraIDList){
						if(qParaID.equals(paraID))
							continue;
						
						boolean isRel = false;
						String relLabel = "", fet = "";
						ArrayList<Double> scores = this.getSavedScore(scoresInPage, qParaID, paraID);
						
						para = allParaIDParaMap.get(paraID);
						/*
						for(SimilarityFunction f:this.simFunctions)
							scores.add(f.simScore(new ParaPair(qParaID, paraID), paraObjList));
						*/
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
						//System.out.println(relLabel+"qid:"+qParaID+" "+fet+"# "+paraID);
						bw.append(relLabel+"qid:"+qParaID+" "+fet+"# "+paraID+"\n");
					}
				}
				System.out.println(pageID+" finished");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bw.close();
	}
	
	public ArrayList<Double> getSavedScore(ArrayList<ParaPairData> scoresData, String paraid1, String paraid2){
		for(ParaPairData ppd:scoresData){
			if((ppd.getParaPair().getPara1().equals(paraid1)&&ppd.getParaPair().getPara2().equals(paraid2)) ||
					(ppd.getParaPair().getPara2().equals(paraid1)&&ppd.getParaPair().getPara1().equals(paraid2))){
				return ppd.getSimScoreList();
			}
		}
		return null;
	}

}
