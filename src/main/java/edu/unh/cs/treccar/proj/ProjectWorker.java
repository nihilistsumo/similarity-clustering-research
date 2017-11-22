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

import edu.unh.cs.treccar.Data;
import edu.unh.cs.treccar.read_data.DeserializeData;

public class ProjectWorker {
	private final HashMap<String, Data.Paragraph> parasMap;
	private final HashMap<String, ArrayList<String>> pageParasMap;
	private final HashMap<String, ArrayList<String>> pageSecMap;
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
	
	public ArrayList<ParaPairData> getParaPairData(ArrayList<Data.Paragraph> paraList){
		ArrayList<ParaPairData> pairData = new ArrayList<ParaPairData>();
		for(int i=0; i<paraList.size()-1; i++){
			for(int j=i+1; j<paraList.size(); j++){
				ParaPair pp = new ParaPair(paraList.get(i).getParaId(), paraList.get(j).getParaId());
				ArrayList<Double> scores = new ArrayList<Double>();
				scores.add(0.0);
				scores.add(0.1);
				scores.add(0.2);
				ParaPairData ppd = new ParaPairData(pp, scores);
				pairData.add(ppd);
			}
		}
		return pairData;
	}
	
	public void printClusterResult(ClusterResult cr){
		System.out.println("Page ID: "+cr.getPageID()+"\n");
		for(int i=0;i<cr.getOptimumWeight().length; i++){
			System.out.print(cr.getOptimumWeight()[i]+" ");
		}
		System.out.println("\nParent matrix:");
		for(int i=0; i<cr.getParentsForSim().length; i++){
			for(int j=0; j<cr.getParentsForSim()[0].length; j++){
				System.out.print(cr.getParentsForSim()[i][j]+" ");
			}
			System.out.println();
		}
	}
	
	public void processParaPairData() throws IOException{
		HashMap<String, ArrayList<ParaPairData>> allPagesData = new HashMap<String, ArrayList<ParaPairData>>();
		for(String pageID:this.pageParasMap.keySet()){
			ArrayList<String> paraIDs = this.pageParasMap.get(pageID);
			//ArrayList<String> secIDs = this.pageSecMap.get(pageID);
			ArrayList<Data.Paragraph> paras = new ArrayList<Data.Paragraph>();
			for(String paraID:paraIDs)
				paras.add(this.parasMap.get(paraID));
			ArrayList<ParaPairData> data = this.getParaPairData(paras);
			allPagesData.put(pageID, data);
			//System.out.println(data.size());
		}
		FileOutputStream fos = new FileOutputStream(this.pr.getProperty("out-dir")+"/"+this.pr.getProperty("data-file"));
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(allPagesData);
		fos.close();
		oos.close();
	}
	
	public void runClustering(){
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
				CustomClustering cl = new CustomClustering(this.pr, pageID, secIDs, paras, ppdList);
				ClusterResult r = cl.getCr();
				this.printClusterResult(r);
			}
			ois.close();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
