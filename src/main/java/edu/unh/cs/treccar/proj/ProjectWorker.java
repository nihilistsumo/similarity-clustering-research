package edu.unh.cs.treccar.proj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
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
		this.pageParasMap = DataUtilities.getArticleMapFromPath
				(prop.getProperty("data-dir")+"/"+prop.getProperty("art-qrels"));
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
	
	public void processParaPairData() throws IOException{
		for(String pageID:this.pageParasMap.keySet()){
			ArrayList<String> paraIDs = this.pageParasMap.get(pageID);
			ArrayList<Data.Paragraph> paras = new ArrayList<Data.Paragraph>();
			for(String paraID:paraIDs)
				paras.add(this.parasMap.get(paraID));
			ArrayList<ParaPairData> data = this.getParaPairData(paras);
			System.out.println(data.size());
			FileOutputStream fos = new FileOutputStream(this.pr.getProperty("out-dir")+"/"+pageID+"data");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(data);
			fos.close();
			oos.close();
		}
	}

}
