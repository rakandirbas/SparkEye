package de.uni.konstanz.models;

public class ClusterTerm implements Comparable<ClusterTerm> {
	
	private int termFreq;
	private String term;
	
	public ClusterTerm(String term) {
		this.term = term;
		termFreq = 0;
	}
	
	public void updateFreq(){
		termFreq++;
	}
	

	public int getTermFreq() {
		return termFreq;
	}

	public String getTerm() {
		return term;
	}
	
	@Override
	public String toString() {
		return String.format("[%s:%d]", getTerm(), getTermFreq());
	}

	@Override
	public int compareTo(ClusterTerm o) {
		if ( termFreq > o.termFreq )
			return 1;
		else if( termFreq < o.termFreq )
			return -1;
		else return 0;
	}

}
