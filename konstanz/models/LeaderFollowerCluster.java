package de.uni.konstanz.models;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.uni.konstanz.analysis.FastCosineSimilarityCalculator;

public class LeaderFollowerCluster extends Cluster {
	//Represents the TF/IDF vector for cluster centroid
	private Map<String, Double> tfIdf_weightsMap;
	private Map<String, Integer> termsLifeTimes;
	private int termLifetime;

	public LeaderFollowerCluster(int termLifetime) {
		tfIdf_weightsMap = new HashMap<String, Double>();
		termsLifeTimes = new LinkedHashMap<String, Integer>();
		this.termLifetime = termLifetime-1;
	}

	/**
	 * Update the cluster centroid with the new assigned vector/tweet to the cluster
	 * @param weightsMap the weight map of the assigned tweet
	 */
	public void updateClusterCentroid( Map<String, Double> weightsMap ) {
		Map<String, Double> clusterMap = getTfIdf_weightsMap();
		double normalization = 0;

		for ( String term : weightsMap.keySet() ) {
			if ( term.equals( FastCosineSimilarityCalculator.NORMALIZATION_ID ) )
				continue;
			double tweetWeight = weightsMap.get(term);
			double clusterWeight = 0;
			if ( clusterMap.containsKey(term) ) {
				clusterWeight = clusterMap.get(term);
			}
			double newCentroid = 
					( (clusterWeight * getClusterSize() - 1 ) + tweetWeight ) / getClusterSize();
			clusterMap.put(term, newCentroid);
			normalization += newCentroid * newCentroid;
		}

		
		for ( String term : clusterMap.keySet() ) {
			if ( term.equals( FastCosineSimilarityCalculator.NORMALIZATION_ID ) )
				continue;
			
			if ( !weightsMap.keySet().contains(term) ) {
				double tweetWeight = 0;
				double clusterWeight = clusterMap.get(term);
				double newCentroid = 
						( (clusterWeight * getClusterSize() - 1 ) + tweetWeight ) / getClusterSize();
				clusterMap.put(term, newCentroid);
				normalization += newCentroid * newCentroid;
			}
			
		}
		clusterMap.put( FastCosineSimilarityCalculator.NORMALIZATION_ID, normalization );
		termsLifeTimes = updateTermsLifeTimes(clusterMap, weightsMap, termsLifeTimes);
	}
	
	public Map<String, Integer> updateTermsLifeTimes( Map<String, Double> clusterWeightsMap,
			Map<String, Double> tweetWeightsMap, 
			Map<String, Integer> termsLifeTimes) {
		
		Map<String, Integer> newTermsLifeTimes = new LinkedHashMap<String, Integer>();
		
		for ( String term : tweetWeightsMap.keySet() ) {
			termsLifeTimes.put(term, termLifetime);
		}
			
		for ( String term : termsLifeTimes.keySet() ) {
			int lifetime = termsLifeTimes.get(term);
			lifetime--;
			newTermsLifeTimes.put(term, lifetime);
		}
		
		Iterator<Entry<String,Integer>> it = newTermsLifeTimes.entrySet().iterator();
		
		while(it.hasNext()) {
			Entry<String,Integer> entry = it.next();
			String term = entry.getKey();
			int lifetime = entry.getValue();
			
			if ( lifetime <= 0 ) {
				it.remove();
				clusterWeightsMap.remove(term);
			}
		}
		
		return newTermsLifeTimes;
		
	}

	public Map<String, Double> getTfIdf_weightsMap() {
		return tfIdf_weightsMap;
	}

	public void setTfIdf_weightsMap(Map<String, Double> tfIdf_weightsMap) {
		this.tfIdf_weightsMap = tfIdf_weightsMap;
	}

	@Override
	public String toString() {
		String dump = "############### Printing Cluster Dump ###############\n";
		dump += "Cluster ID: " + getId() + "\n";
		dump += "Cluster size: " + getClusterSize() + "\n";
		
		dump += "Cluster centroid vector: \n";
		
		for ( String term : getTfIdf_weightsMap().keySet() ) {
			if ( term.equals( FastCosineSimilarityCalculator.NORMALIZATION_ID ) )
				continue;
			dump += "[" + term + "] ";
		}
		return dump;
	}

}










