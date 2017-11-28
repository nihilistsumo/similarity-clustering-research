package edu.unh.cs.treccar.proj.util;

import java.io.Serializable;

public class ParaPair implements Serializable {
	private String para1;
	private String para2;
	
	public ParaPair(String w1, String w2){
		this.para1 = w1;
		this.para2 = w2;
	}
	
	public String getPara1() {
		return para1;
	}
	public void setPara1(String word1) {
		this.para1 = word1;
	}
	public String getPara2() {
		return para2;
	}
	public void setPara2(String word2) {
		this.para2 = word2;
	}
	@Override
	public boolean equals(Object o){
		if(((ParaPair)o).getPara1()==this.para1 
				&& ((ParaPair)o).getPara2()==this.para2)
				return true;
		else if(((ParaPair)o).getPara2()==this.para1 
				&& ((ParaPair)o).getPara1()==this.para2)
				return true;
		return false;
	}
	@Override
	public int hashCode(){
		return this.para1.hashCode()+this.para2.hashCode();
	}
}
