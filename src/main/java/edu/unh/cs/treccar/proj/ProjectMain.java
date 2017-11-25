package edu.unh.cs.treccar.proj;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;

import edu.unh.cs.treccar.Data;

public class ProjectMain {
	public static final String PROPERTIES = "project.properties";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Properties prop = new Properties();
		try {
			InputStream is = new FileInputStream(ProjectMain.PROPERTIES);
			prop.load(is);
			ProjectWorker worker = new ProjectWorker(prop);
			worker.processParaPairData();
			
			// This w is to be optimized
			double[] w = {0.1, 0.05, 0.2, 0.4, 0.15};
			double score = worker.runClusteringOnTrain(w);
			
			System.out.println("Mean acc: "+score);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Main is working");
	}

}
