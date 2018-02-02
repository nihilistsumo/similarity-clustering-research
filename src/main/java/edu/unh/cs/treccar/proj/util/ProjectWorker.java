package edu.unh.cs.treccar.proj.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Random;

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.unh.cs.treccar.Data;
import edu.unh.cs.treccar.proj.cli.DataBinder;
import edu.unh.cs.treccar.proj.cluster.ClusterResult;
import edu.unh.cs.treccar.proj.cluster.CustomClustering;
import edu.unh.cs.treccar.proj.cluster.CustomHAC;
import edu.unh.cs.treccar.proj.similarities.HerstStOngeSimilarity;
import edu.unh.cs.treccar.proj.similarities.JaroWinklerDistance;
import edu.unh.cs.treccar.proj.similarities.JiangConrathSimilarity;
import edu.unh.cs.treccar.proj.similarities.LeacockChodorowSimilarity;
import edu.unh.cs.treccar.proj.similarities.LeskSimilarity;
import edu.unh.cs.treccar.proj.similarities.SimilarityFunction;
import edu.unh.cs.treccar.read_data.DeserializeData;

public class ProjectWorker {
	private final HashMap<String, Data.Paragraph> parasMap;
	private final HashMap<String, ArrayList<String>> preprocessedParasMap;
	private final HashMap<String, ArrayList<String>> truePageParasMap;
	private final HashMap<String, ArrayList<String>> pageSecMap;
	private final HashMap<String, ArrayList<ArrayList<String>>> gtClusterMap;
	private final ArrayList<SimilarityFunction> funcList;
	
	private DataBinder uidata;
	public Properties pr;
	
	public HashMap<String, Data.Paragraph> getParasMap() {
		return parasMap;
	}

	public HashMap<String, ArrayList<String>> getTruePageParasMap() {
		return truePageParasMap;
	}
	
	public ArrayList<SimilarityFunction> getFuncList(){
		return this.funcList;
	}

	// new
	public ProjectWorker(DataBinder data, Properties p){
		this.uidata = data;
		this.pr = p;
		this.parasMap = DataUtilities.getParaMapFromPath(data.getTrainParafile());
		this.preprocessedParasMap = DataUtilities.getPreprocessedParaMap(parasMap);
		this.truePageParasMap = DataUtilities.getTrueArticleParasMapFromPath(data.getTrainArtqrels());
		
		this.pageSecMap = DataUtilities.getArticleSecMap(data.getTrainOutfile());
		this.gtClusterMap = new HashMap<String, ArrayList<ArrayList<String>>>();
		for(String pageID:this.truePageParasMap.keySet()){
			this.gtClusterMap.put(pageID, DataUtilities.getGTClusters(pageID, data.getTrainHierqrels()));
		}
		this.funcList = data.getFuncs();
	}
	
	public ArrayList<String> getVocabList(){
		HashSet<String> vocabSet = new HashSet<String>();
		ArrayList<String> currTokens;
		for(String pid:this.preprocessedParasMap.keySet()){
			currTokens = this.preprocessedParasMap.get(pid);
			for(String token:currTokens)
				vocabSet.add(token);
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
	
	// new
	private ArrayList<Double> computeScores(ParaPair pp, ArrayList<Data.Paragraph> paraList, ILexicalDatabase db){
		int size = uidata.getFuncs().size();
		ArrayList<Double> scores = new ArrayList<Double>(size);
		/*
		scores.add(new HerstStOngeSimilarity().simScore(pp, paraList));
		scores.add(new JaroWinklerDistance().simScore(pp, paraList));
		scores.add(new JiangConrathSimilarity().simScore(pp, paraList));
		scores.add(new LeacockChodorowSimilarity().simScore(pp, paraList));
		scores.add(new LeskSimilarity().simScore(pp, paraList));
		*/
		
		for(SimilarityFunction f:this.funcList){
			scores.add(f.simScore(pp, db));
		}
		
		return scores;
	}
	
	// new
	public ArrayList<ParaPairData> getParaPairData(ArrayList<Data.Paragraph> paraList, ILexicalDatabase db){
		ArrayList<ParaPairData> pairData = new ArrayList<ParaPairData>();
		for(int i=0; i<paraList.size()-1; i++){
			for(int j=i+1; j<paraList.size(); j++){
				String pid1 = paraList.get(i).getParaId();
				String pid2 = paraList.get(j).getParaId();
				ParaPair pp = new ParaPair(pid1, pid2, this.preprocessedParasMap.get(pid1), this.preprocessedParasMap.get(pid2));
				//ArrayList<Double> scores = this.computeScores(3);
				ArrayList<Double> scores = this.computeScores(pp, paraList, db);
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
	
	// new
	public HashMap<String, ArrayList<ParaPairData>> processParaPairData(
			HashMap<String, ArrayList<String>> pageParasMap) throws IOException{
		HashMap<String, ArrayList<ParaPairData>> allPagesData = new HashMap<String, ArrayList<ParaPairData>>();
		ILexicalDatabase db = new NictWordNet();
		int i=0;
		int n=pageParasMap.keySet().size();
		File logOut = new File(this.pr.getProperty("out-dir")+"/"+this.pr.getProperty("log-file"));
		for(String pageID:pageParasMap.keySet()){
			ArrayList<String> paraIDs = pageParasMap.get(pageID);
			//ArrayList<String> secIDs = this.pageSecMap.get(pageID);
			ArrayList<Data.Paragraph> paras = new ArrayList<Data.Paragraph>();
			System.out.println("Page ID: "+pageID+", "+paraIDs.size()+" paras");
			BufferedWriter bw = new BufferedWriter(new FileWriter(logOut));
			bw.append(pageID+" has started with "+paraIDs.size()+" paras, "+(n-i-1)+" to go after this\n");
			bw.close();
			for(String paraID:paraIDs)
				paras.add(this.parasMap.get(paraID));
			
			//Expensive op
			ArrayList<ParaPairData> data = this.getParaPairData(paras, db);
			//
			
			allPagesData.put(pageID, data);
			i++;
			System.out.println(pageID+" is done, "+(n-i)+" to go");
			//bw.append(" is done, "+(n-i)+" to go");
			//bw.close();
			//System.out.println(data.size());
		}
		return allPagesData;
	}
	
	// new
	public void saveParaSimilarityData(HashMap<String, ArrayList<ParaPairData>> allPagesData, String filePath){
		try {
			FileOutputStream fos = new FileOutputStream(filePath);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(allPagesData);
			fos.close();
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// new
	public void runClustering(){
		try {
			FileInputStream fis = new FileInputStream(new File(pr.getProperty("out-dir")+"/"+pr.getProperty("data-file")));
			ObjectInputStream ois = new ObjectInputStream(fis);
			HashMap<String, ArrayList<ParaPairData>> ppdData = (HashMap<String, ArrayList<ParaPairData>>) ois.readObject();
			double[] w = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6};
			for(String pageid:this.pageSecMap.keySet()){
				ArrayList<Data.Paragraph> paraList = new ArrayList<Data.Paragraph>();
				for(String paraid:this.truePageParasMap.get(pageid))
					paraList.add(this.parasMap.get(paraid));
				CustomHAC hac = new CustomHAC(pr, pageid, w, this.funcList, this.pageSecMap.get(pageid), paraList, ppdData.get(pageid));
				HashMap<String, ArrayList<String>> clusterResult = hac.cluster();
				System.out.println("Page ID: "+pageid+"\n");
				for(String cid:clusterResult.keySet()){
					System.out.println("Cluster ID: "+cid);
					System.out.println("-----------------------------");
					for(String pid:clusterResult.get(cid))
						System.out.println(pid);
					System.out.println("-----------------------------");
					System.out.println();
				}
			}
			ois.close();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public double runClusteringOnTest(double[] w){
		HashMap<String, Double> pageScoreMap = new HashMap<String, Double>();
		double meanScore = 0;
		try {
			ObjectInputStream ois = new ObjectInputStream(
					new BufferedInputStream(new FileInputStream(
							this.pr.getProperty("out-dir")+"/"+this.pr.getProperty("data-file"))));
			HashMap<String, ArrayList<ParaPairData>> pageDataMap = 
					(HashMap<String, ArrayList<ParaPairData>>)ois.readObject();
			for(String pageID:pageDataMap.keySet()){
				ArrayList<String> paraIDs = this.truePageParasMap.get(pageID);
				ArrayList<String> secIDs = this.pageSecMap.get(pageID);
				ArrayList<Data.Paragraph> paras = new ArrayList<Data.Paragraph>();
				for(String paraID:paraIDs)
					paras.add(this.parasMap.get(paraID));
				ArrayList<ParaPairData> ppdList = pageDataMap.get(pageID);
				
				CustomClustering cl = new CustomClustering(this.pr, pageID, w, this.funcList, secIDs, paras, ppdList);
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
	
	/*
	//new
	public class WordPair{
		String word1, word2;
		
		public WordPair(String w1, String w2){
			this.word1 = w1;
			this.word2 = w2;
		}

		@Override
		public boolean equals(Object obj) {
			// TODO Auto-generated method stub
			if(obj.getClass() != WordPair.class)
				return false;
			return (((WordPair)obj).word1==this.word1 && ((WordPair)obj).word2==this.word2) ||
					(((WordPair)obj).word1==this.word2 && ((WordPair)obj).word2==this.word1);
		}
	}
	*/
}
