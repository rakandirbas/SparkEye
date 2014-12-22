package de.uni.konstanz.tests;

import java.util.List;

import de.uni.konstanz.eventdetection.DetectionPipeline;
import de.uni.konstanz.models.Cluster;
import de.uni.konstanz.models.Tweet;

public class ResultsToCSV {

	public static void main(String[] args) {
		DetectionPipeline detectionPipeline = new
				DetectionPipeline(0.5);
		
		String path = "/Users/rockyrock/Desktop/testCSV/2013_04_15_22_filtered_sorted.csv";
		int similarityThreshold = 50;
		int clusterLifeTime = 30;
		int windowSize = 8000;
		int numberOfWindowsToCluster = 2;
		Clusterer clusterer = new Clusterer(path, similarityThreshold, clusterLifeTime, 
				windowSize, numberOfWindowsToCluster);
		System.out.println("Starting clustering ...");
		clusterer.startClustering();
		System.out.println("Clustering ended.");
		System.out.println("Number of topics: " + clusterer.getClusters().size());
		for ( Cluster cluster : clusterer.getClusters().keySet() ) {
			List<Tweet> tweetsList = clusterer.getClusters().get(cluster);
			
			System.out.println("***Printing the contents of each cluster****");
			for ( Tweet tweet : tweetsList ) {
				System.out.println(tweet.getText());
			}
		}
		
	}

}
