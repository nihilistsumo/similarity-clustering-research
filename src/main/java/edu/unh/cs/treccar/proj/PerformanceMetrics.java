package edu.unh.cs.treccar.proj;

import java.util.ArrayList;

public class PerformanceMetrics {

	private int getParentLabel(int i, int[] parents){
		while(parents[i]!=i)
			i = parents[i];
		return i;
	}
	
	public double getAccuracy(ArrayList<ArrayList<String>> gtClusters, ArrayList<String> paraids, int[] labels){
		double score = 0;
		String p1,p2;
		int pos = 0, count = 0;
		for(int i=0; i<paraids.size()-1; i++){
			p1 = paraids.get(i);
			for(int j=i+1; j<paraids.size(); j++){
				p2 = paraids.get(j);
				if(this.getParentLabel(i, labels)==this.getParentLabel(j, labels)){
					for(ArrayList<String> cluster:gtClusters){
						if(cluster.contains(p1)){
							if(cluster.contains(p2))
								pos++;
							break;
						}
					}
				}
				else{
					for(ArrayList<String> cluster:gtClusters){
						if(cluster.contains(p1)){
							if(!cluster.contains(p2))
								pos++;
							break;
						}
					}
				}
				count++;
			}
		}
		score = (double)pos/(double)count;
		return score;
	}
}
