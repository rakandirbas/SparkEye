package de.uni.konstanz.tests;

import java.util.List;
import java.util.Map;

import de.uni.konstanz.models.Cluster;
import de.uni.konstanz.models.LeaderFollowerCluster;
import de.uni.konstanz.models.Tweet;
import de.uni.konstanz.utils.MapUtils;

public class ClustererTest {
	public static void main(String[] args) {
		String path = "/Users/rockyrock/Desktop/testCSV/2013_04_19_22.csv";
		int similarityThreshold = 50;
		int clusterLifeTime = 30;
		int windowSize = 1000;
		int numberOfWindowsToCluster = 2;
		Clusterer clusterer = new Clusterer(path, similarityThreshold, clusterLifeTime, 
				windowSize, numberOfWindowsToCluster);
		System.out.println("Starting clustering ...");
		clusterer.startClustering();
		System.out.println("Clustering ended.");
		System.out.println("Number of topics: " + clusterer.getClusters().size());
		for ( Cluster cluster : clusterer.getClusters().keySet() ) {
			List<Tweet> tweetsList = clusterer.getClusters().get(cluster);
			LeaderFollowerCluster lfCluster = (LeaderFollowerCluster) cluster;
			Map<String, Double> wieghtsMap = lfCluster.getTfIdf_weightsMap();
			wieghtsMap = MapUtils.sortByValue(wieghtsMap, true);
			
			for ( String term : wieghtsMap.keySet() ) {
				System.out.println(term);
			}
			
			System.out.println("***Printing the contents of each cluster****");
			for ( Tweet tweet : tweetsList ) {
				System.out.println(tweet.getText());
			}
		}
	}
}
