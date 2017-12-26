package edu.unh.cs.treccar.proj.ui;

import java.util.ArrayList;

import edu.unh.cs.treccar.proj.similarities.SimilarityFunction;

public class UIDataBinder {
	private String outdir;
	private String trainParafile;
	private String testParafile;
	private String trainArtqrels;
	private String trainHierqrels;
	private String testArtqrels;
	private String testHierqrels;
	private String trainScoreData;
	private ArrayList<SimilarityFunction> funcs;
	private double threshold;
	
	public ArrayList<SimilarityFunction> getFuncs() {
		return funcs;
	}

	public void setFuncs(ArrayList<SimilarityFunction> funcs) {
		this.funcs = funcs;
	}

	public String getOutdir() {
		return outdir;
	}

	public void setOutdir(String outdir) {
		this.outdir = outdir;
	}

	public String getTrainParafile() {
		return trainParafile;
	}

	public void setTrainParafile(String trainParafile) {
		this.trainParafile = trainParafile;
	}

	public String getTestParafile() {
		return testParafile;
	}

	public void setTestParafile(String testParafile) {
		this.testParafile = testParafile;
	}

	public String getTrainArtqrels() {
		return trainArtqrels;
	}

	public void setTrainArtqrels(String trainArtqrels) {
		this.trainArtqrels = trainArtqrels;
	}

	public String getTrainHierqrels() {
		return trainHierqrels;
	}

	public void setTrainHierqrels(String trainHierqrels) {
		this.trainHierqrels = trainHierqrels;
	}

	public String getTestArtqrels() {
		return testArtqrels;
	}

	public void setTestArtqrels(String testArtqrels) {
		this.testArtqrels = testArtqrels;
	}

	public String getTestHierqrels() {
		return testHierqrels;
	}

	public void setTestHierqrels(String testHierqrels) {
		this.testHierqrels = testHierqrels;
	}

	public String getTrainScoreData() {
		return trainScoreData;
	}

	public void setTrainScoreData(String trainScoreData) {
		this.trainScoreData = trainScoreData;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public UIDataBinder(String outdir, String trainParafile,
			String testParafile, String trainArtqrels, String trainHierqrels,
			String testArtqrels, String testHierqrels, String trainScoreData,
			ArrayList<SimilarityFunction> funcs, double threshold) {
		super();
		this.outdir = outdir;
		this.trainParafile = trainParafile;
		this.testParafile = testParafile;
		this.trainArtqrels = trainArtqrels;
		this.trainHierqrels = trainHierqrels;
		this.testArtqrels = testArtqrels;
		this.testHierqrels = testHierqrels;
		this.trainScoreData = trainScoreData;
		this.funcs = funcs;
		this.threshold = threshold;
	}
}
