package edu.unh.cs.treccar.proj;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Random;

import edu.unh.cs.treccar.Data;
import edu.unh.cs.treccar.read_data.DeserializeData;

public class ProjectWorker {
	private final HashMap<String, Data.Paragraph> parasMap;
	private final HashMap<String, ArrayList<String>> pageParasMap;
	private final HashMap<String, ArrayList<String>> pageSecMap;
	private final HashMap<String, ArrayList<ArrayList<String>>> gtClusterMap;
	public Properties pr;
	
	public HashMap<String, Data.Paragraph> getParasMap() {
		return parasMap;
	}

	public HashMap<String, ArrayList<String>> getPageParasMap() {
		return pageParasMap;
	}

	public ProjectWorker(Properties prop){
		this.pr = prop;
		this.parasMap = DataUtilities.getParaMapFromPath
				(prop.getProperty("data-dir")+"/"+prop.getProperty("parafile"));
		this.pageParasMap = DataUtilities.getArticleParasMapFromPath
				(prop.getProperty("data-dir")+"/"+prop.getProperty("art-qrels"));
		this.pageSecMap = DataUtilities.getArticleSecMapFromPath
				(prop.getProperty("data-dir")+"/"+prop.getProperty("hier-qrels"));
		this.gtClusterMap = new HashMap<String, ArrayList<ArrayList<String>>>();
		for(String pageID:this.pageParasMap.keySet()){
			this.gtClusterMap.put(pageID, DataUtilities.getGTClusters(
			pageID, prop.getProperty("data-dir")+"/"+prop.getProperty("hier-qrels")));
		}
	}
	
	public ArrayList<String> getVocabList(String parafilePath) throws FileNotFoundException{
		FileInputStream fis = new FileInputStream(new File(parafilePath));
		String[] paraTokens;
		HashSet<String> vocabSet = new HashSet<String>();
		for(Data.Paragraph para:DeserializeData.iterableParagraphs(fis)){
			paraTokens = para.getTextOnly().replaceAll("[^a-zA-Z ]", " ").toLowerCase().split("\\s+");
			for(String token:paraTokens){
				vocabSet.add(token);
			}
		}
		ArrayList<String> vocabList = new ArrayList<String>(vocabSet);
		return vocabList;
	}
	
	private ArrayList<Double> computeScores(int size){
		ArrayList<Double> randScores = new ArrayList<Double>(size);
		Random r = new Random();
		for(int i=0; i<size; i++)
			randScores.add(r.nextDouble());
		return randScores;
	}
	
	private ArrayList<Double> computeScores(ParaPair pp, ArrayList<Data.Paragraph> paraList){
		int size = Integer.parseInt(this.pr.getProperty("sim-fet-count"));
		ArrayList<Double> scores = new ArrayList<Double>(size);
		
		scores.add(new HerstStOngeSimilarity().simScore(pp, paraList));
		scores.add(new JaroWinklerDistance().simScore(pp, paraList));
		scores.add(new JiangConrathSimilarity().simScore(pp, paraList));
		scores.add(new LeacockChodorowSimilarity().simScore(pp, paraList));
		scores.add(new LeskSimilarity().simScore(pp, paraList));
		
		return scores;
	}
	
	public ArrayList<ParaPairData> getParaPairData(ArrayList<Data.Paragraph> paraList){
		ArrayList<ParaPairData> pairData = new ArrayList<ParaPairData>();
		for(int i=0; i<paraList.size()-1; i++){
			for(int j=i+1; j<paraList.size(); j++){
				ParaPair pp = new ParaPair(paraList.get(i).getParaId(), paraList.get(j).getParaId());
				//ArrayList<Double> scores = this.computeScores(3);
				ArrayList<Double> scores = this.computeScores(pp, paraList);
				System.out.println(scores);
				ParaPairData ppd = new ParaPairData(pp, scores);
				pairData.add(ppd);
			}
		}
		return pairData;
	}
	
	public void printClusterResult(ClusterResult cr){
		String pageID = cr.getPageID();
		ArrayList<String> paraids = DataUtilities.getOrderedParaIDArray(cr.getParas());
		int[] parentLabels = cr.getParents();
		System.out.println("Page ID: "+pageID+"\n");
		for(int i=0;i<parentLabels.length; i++){
			System.out.print(parentLabels[i]+" ");
		}
		System.out.println();
	}
	
	public void processParaPairData() throws IOException{
		HashMap<String, ArrayList<ParaPairData>> allPagesData = new HashMap<String, ArrayList<ParaPairData>>();
		for(String pageID:this.pageParasMap.keySet()){
			ArrayList<String> paraIDs = this.pageParasMap.get(pageID);
			//ArrayList<String> secIDs = this.pageSecMap.get(pageID);
			ArrayList<Data.Paragraph> paras = new ArrayList<Data.Paragraph>();
			for(String paraID:paraIDs)
				paras.add(this.parasMap.get(paraID));
			
			//Expensive op
			ArrayList<ParaPairData> data = this.getParaPairData(paras);
			//
			
			allPagesData.put(pageID, data);
			System.out.println(pageID+" is done");
			//System.out.println(data.size());
		}
		FileOutputStream fos = new FileOutputStream(this.pr.getProperty("out-dir")+"/"+this.pr.getProperty("data-file"));
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(allPagesData);
		fos.close();
		oos.close();
	}
	
	public double runClusteringOnTrain(double[] w){
		HashMap<String, Double> pageScoreMap = new HashMap<String, Double>();
		double meanScore = 0;
		try {
			ObjectInputStream ois = new ObjectInputStream(
					new BufferedInputStream(new FileInputStream(
							this.pr.getProperty("out-dir")+"/"+this.pr.getProperty("data-file"))));
			HashMap<String, ArrayList<ParaPairData>> pageDataMap = 
					(HashMap<String, ArrayList<ParaPairData>>)ois.readObject();
			for(String pageID:pageDataMap.keySet()){
				ArrayList<String> paraIDs = this.pageParasMap.get(pageID);
				ArrayList<String> secIDs = this.pageSecMap.get(pageID);
				ArrayList<Data.Paragraph> paras = new ArrayList<Data.Paragraph>();
				for(String paraID:paraIDs)
					paras.add(this.parasMap.get(paraID));
				ArrayList<ParaPairData> ppdList = pageDataMap.get(pageID);
				
				CustomClustering cl = new CustomClustering(this.pr, pageID, w, secIDs, paras, ppdList);
				ClusterResult r = cl.getCr();
				this.printClusterResult(r);
				PerformanceMetrics pm = new PerformanceMetrics();
				double s = pm.getAccuracy(this.gtClusterMap.get(pageID), paraIDs, r.getParents());
				System.out.println("Accuracy: "+s);
				pageScoreMap.put(pageID, s);
			}
			ois.close();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(String p:pageScoreMap.keySet())
			meanScore+=pageScoreMap.get(p);
		meanScore/=pageScoreMap.size();
		return meanScore;
	}
}
