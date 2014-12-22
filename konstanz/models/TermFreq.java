package de.uni.konstanz.models;

public class TermFreq {
	private int totalFrequency;
	private int docFrequency;
	private double movingAverage;
	
	public int getTotalFrequency() {
		return totalFrequency;
	}
	public void setTotalFrequency(int totalFrequency) {
		this.totalFrequency = totalFrequency;
	}
	public int getDocFrequency() {
		return docFrequency;
	}
	public void setDocFrequency(int docFrequency) {
		this.docFrequency = docFrequency;
	}
	public double getMovingAverage() {
		return movingAverage;
	}
	public void setMovingAverage(double movingAverage) {
		this.movingAverage = movingAverage;
	}
	
	
}
