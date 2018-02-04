package edu.unh.cs.treccar.proj.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;

import edu.unh.cs.treccar.Data;
import edu.unh.cs.treccar.proj.similarities.CosineSimilarity;
import edu.unh.cs.treccar.proj.similarities.DiceSimilarity;
import edu.unh.cs.treccar.proj.similarities.HerstStOngeSimilarity;
import edu.unh.cs.treccar.proj.similarities.JaccardSimilarity;
import edu.unh.cs.treccar.proj.similarities.JaroWinklerDistance;
import edu.unh.cs.treccar.proj.similarities.JiangConrathSimilarity;
import edu.unh.cs.treccar.proj.similarities.LeacockChodorowSimilarity;
import edu.unh.cs.treccar.proj.similarities.LeskSimilarity;
import edu.unh.cs.treccar.proj.similarities.LinSimilarity;
import edu.unh.cs.treccar.proj.similarities.ResnikSimilarity;
import edu.unh.cs.treccar.proj.similarities.SimilarityFunction;
import edu.unh.cs.treccar.proj.similarities.WuPalmerSimilarity;
import edu.unh.cs.treccar.proj.train.RankLibFileCreator;

public class ProjectMain {
	public static final String PROPERTIES = "project.properties";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Properties prop = new Properties();
		try {
			InputStream is = new FileInputStream(ProjectMain.PROPERTIES);
			prop.load(is);
			
			ArrayList<SimilarityFunction> simFuncList = new ArrayList<SimilarityFunction>();
			//simFuncList.add(new CosineSimilarity()); // error
			simFuncList.add(new DiceSimilarity()); // ok
			simFuncList.add(new JaccardSimilarity()); // ok
			//simFuncList.add(new LinSimilarity()); // error
			simFuncList.add(new JiangConrathSimilarity()); // ok
			simFuncList.add(new LeacockChodorowSimilarity()); // ok
			//simFuncList.add(new LeskSimilarity()); // ok, but long time
			//simFuncList.add(new ResnikSimilarity()); // error
			//simFuncList.add(new WuPalmerSimilarity()); // error
			
			
			RankLibFileCreator rl = new RankLibFileCreator(prop, simFuncList);
			rl.printRankLibInputFile();
			
			// get pageid-paraids map from article qrels of train dataset and use it with process
			//ProjectWorker worker = new ProjectWorker(prop, simFuncList);
			//worker.processParaPairData();
			
			// This w is to be optimized
			double[] w = {0.1, 0.05, 0.2, 0.4, 0.15};
			//double score = worker.runClusteringOnTest(w);
			
			//System.out.println("Mean acc: "+score);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Main is working");
	}
	
}
