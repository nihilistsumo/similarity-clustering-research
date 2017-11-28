package edu.unh.cs.treccar.proj.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import co.nstant.in.cbor.CborException;
import edu.unh.cs.treccar.Data;
import edu.unh.cs.treccar.read_data.DeserializeData;

public class DataUtilities {
	
	public static HashMap<String, Data.Paragraph> getParaMapFromPath(String path){
		HashMap<String, Data.Paragraph> paras = new HashMap<String, Data.Paragraph>();
		try {
			FileInputStream fis = new FileInputStream(new File(path));
			for(Data.Paragraph para:DeserializeData.iterableParagraphs(fis))
				paras.put(para.getParaId(), para);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return paras;
	}
	
	public static HashMap<String, ArrayList<String>> getArticleParasMapFromPath(String path){
		HashMap<String, ArrayList<String>> articleMap = new HashMap<String, ArrayList<String>>();
		BufferedReader br;
		try{
			br = new BufferedReader(new FileReader(path));
			String line;
			String[] lineData = new String[4];
			while((line = br.readLine()) != null){
				lineData = line.split(" ");
				if(articleMap.containsKey(lineData[0])){
					articleMap.get(lineData[0]).add(lineData[2]);
				} else{
					ArrayList<String> paraList = new ArrayList<String>();
					paraList.add(lineData[2]);
					articleMap.put(lineData[0], paraList);
				}	
			}
			br.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		return articleMap;
	}
	
	public static HashMap<String, ArrayList<String>> getArticleSecMapFromPath(String path){
		HashMap<String, ArrayList<String>> articleSecMap = new HashMap<String, ArrayList<String>>();
		BufferedReader br;
		try{
			br = new BufferedReader(new FileReader(path));
			String line, pageid;
			String[] lineData = new String[4];
			while((line = br.readLine()) != null){
				lineData = line.split(" ");
				pageid = lineData[0].split("/")[0];
				if(articleSecMap.containsKey(pageid)){
					articleSecMap.get(pageid).add(lineData[0]);
				} else{
					ArrayList<String> secIDList = new ArrayList<String>();
					secIDList.add(lineData[2]);
					articleSecMap.put(pageid, secIDList);
				}	
			}
			br.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		return articleSecMap;
	}
	// Converts arraylist of para objects into their corresponding para id array
	public static ArrayList<String> getOrderedParaIDArray(ArrayList<Data.Paragraph> paras){
		ArrayList<String> ids = new ArrayList<String>(paras.size());
		for(Data.Paragraph p:paras)
			ids.add(p.getParaId());
		return ids;
	}
	
	public static HashMap<String,ArrayList<String>> getGTMapQrels(String qrelsPath){
		HashMap<String, ArrayList<String>> gtMap = new HashMap<String, ArrayList<String>>();
		BufferedReader br;
		try{
			br = new BufferedReader(new FileReader(qrelsPath));
			String line;
			String[] lineData = new String[4];
			while((line = br.readLine()) != null){
				lineData = line.split(" ");
				if(gtMap.containsKey(lineData[0])){
					gtMap.get(lineData[0]).add(lineData[2]);
				} else{
					ArrayList<String> paraList = new ArrayList<String>();
					paraList.add(lineData[2]);
					gtMap.put(lineData[0], paraList);
				}	
			}
			br.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		return gtMap;
	}
	
	public static ArrayList<ArrayList<String>> getGTClusters(String pageid, String qrelsPath){
		HashMap<String, ArrayList<String>> gtMap = DataUtilities.getGTMapQrels(qrelsPath);
		ArrayList<ArrayList<String>> clusters = new ArrayList<ArrayList<String>>();
		for(String secID:gtMap.keySet()){
			if(secID.startsWith(pageid)){
				ArrayList<String> clust = gtMap.get(secID);
				clusters.add(clust);
			}
		}
		return clusters;
	}

}
