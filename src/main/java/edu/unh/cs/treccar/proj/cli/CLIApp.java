package edu.unh.cs.treccar.proj.cli;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import edu.unh.cs.treccar.proj.similarities.DiceSimilarity;
import edu.unh.cs.treccar.proj.similarities.HerstStOngeSimilarity;
import edu.unh.cs.treccar.proj.similarities.JaccardSimilarity;
import edu.unh.cs.treccar.proj.similarities.JaroWinklerDistance;
import edu.unh.cs.treccar.proj.similarities.JiangConrathSimilarity;
import edu.unh.cs.treccar.proj.similarities.LeacockChodorowSimilarity;
import edu.unh.cs.treccar.proj.similarities.LeskSimilarity;
import edu.unh.cs.treccar.proj.similarities.LinSimilarity;
import edu.unh.cs.treccar.proj.similarities.PathSimilarity;
import edu.unh.cs.treccar.proj.similarities.ResnikSimilarity;
import edu.unh.cs.treccar.proj.similarities.SimilarityFunction;
import edu.unh.cs.treccar.proj.similarities.WuPalmerSimilarity;
import edu.unh.cs.treccar.proj.train.RankLibFileCreator;
import edu.unh.cs.treccar.proj.util.ParaPairData;
import edu.unh.cs.treccar.proj.util.ProjectMain;
import edu.unh.cs.treccar.proj.util.ProjectWorker;

public class CLIApp {
	public static final String PROPERTIES = "project.properties";
    
    public static void main(String[] args) {
    	ArrayList<SimilarityFunction> funcList = new ArrayList<SimilarityFunction>();
    	for(String f:args)
    		CLIApp.addFunc(f, funcList);
    	Properties prop = new Properties();
		try {
			InputStream is = new FileInputStream(CLIApp.PROPERTIES);
			prop.load(is);
			DataBinder data = new DataBinder(prop.getProperty("out-dir"), prop.getProperty("data-dir")+"/"+prop.getProperty("train-parafile"),
			prop.getProperty("data-dir")+"/"+prop.getProperty("test-parafile"), prop.getProperty("data-dir")+"/"+prop.getProperty("train-art-qrels"),
			prop.getProperty("data-dir")+"/"+prop.getProperty("train-hier-qrels"), prop.getProperty("data-dir")+"/"+prop.getProperty("test-art-qrels"),
			prop.getProperty("data-dir")+"/"+prop.getProperty("test-hier-qrels"), prop.getProperty("out-dir")+"/"+prop.getProperty("data-file"),
			funcList, Double.parseDouble(prop.getProperty("threshold")));
			ProjectWorker pw = new ProjectWorker(data);
			//have to normalize all the scores
			//HashMap<String, ArrayList<ParaPairData>> scoresMap = pw.processParaPairData(pw.getPageParasMap());
			//pw.saveParaSimilarityData(scoresMap, data.getTrainScoreData());
			RankLibFileCreator rlb = new RankLibFileCreator(prop, funcList);
			rlb.printRankLibInputFile();
		} catch(IOException e){
			e.printStackTrace();
		}
    }
    
    public static void addFunc(String func, ArrayList<SimilarityFunction> list){
    	if(func.equalsIgnoreCase("hso"))
    		list.add(new HerstStOngeSimilarity());
    	else if(func.equalsIgnoreCase("ji"))
    		list.add(new JiangConrathSimilarity());
    	else if(func.equalsIgnoreCase("lea"))
    		list.add(new LeacockChodorowSimilarity());
    	else if(func.equalsIgnoreCase("les"))
    		list.add(new LeskSimilarity());
    	else if(func.equalsIgnoreCase("lin"))
    		list.add(new LinSimilarity());
    	else if(func.equalsIgnoreCase("pat"))
    		list.add(new PathSimilarity());
    	else if(func.equalsIgnoreCase("res"))
    		list.add(new ResnikSimilarity());
    	else if(func.equalsIgnoreCase("wu"))
    		list.add(new WuPalmerSimilarity());
    }
}
