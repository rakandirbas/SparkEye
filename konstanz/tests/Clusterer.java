package de.uni.konstanz.tests;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import de.uni.konstanz.gui.server.GTweetsConsumer;
import de.uni.konstanz.gui.server.GTweetsProducer;
import de.uni.konstanz.models.Cluster;
import de.uni.konstanz.models.Tweet;
import de.uni.konstanz.utils.ClustersUtils;

public class Clusterer {
	
	private ExecutorService executor;
	private BlockingQueue<Tweet> buffer;
	private File csvFileLocation;
	private GTweetsProducer producer;
	private GTweetsConsumer consumer;
	private int numberOfWindowsToCluster;
	private Map<Cluster, List<Tweet>> clusters; 
	
	public Clusterer( String pathToCSVFile, int similarityThreshold, 
			int clusterLifeTime, int windowSize, int numberOfWindowsToCluster) {
		//make the threshold between 0.0 & 1.0
		similarityThreshold = similarityThreshold / 100;
		this.numberOfWindowsToCluster = numberOfWindowsToCluster;
		executor = Executors.newCachedThreadPool();
		buffer = new LinkedBlockingQueue<Tweet>(100000);
		csvFileLocation = new File(pathToCSVFile);
		producer = new GTweetsProducer(buffer, csvFileLocation);
		//The timeslice (10) is just to cope with deprecated/old code ... has *no* effect ... any value won't change anything.
		consumer = new GTweetsConsumer(buffer, windowSize, similarityThreshold, 10, clusterLifeTime);
		clusters = new LinkedHashMap<Cluster, List<Tweet>>();
	}
	
	public Map<Cluster, List<Tweet>> getClusters() {
		return clusters;
	}
	
	public void startClustering() {
		executor.execute( producer );
		executor.execute( consumer );
		executor.shutdown();
		while( consumer.getChuncksCounter() != numberOfWindowsToCluster ) {
			
		}
		
		stopClustering();
		
		for ( Long clusterID : consumer.getLeaderFollowerClusters().keySet() ) {
			Cluster cluster  = consumer.getLeaderFollowerClusters().get(clusterID);
			Map<Long, Tweet> tweets = consumer.getTweetsInClustersDao().getTweetsInClusters();
			List<Tweet> clusterTweets = ClustersUtils.getClusterTweets(cluster, tweets);
			clusters.put(cluster, clusterTweets);
		}
		
	}
	
	public void stopClustering() {
		producer.pause();
		consumer.pause();
		while( !producer.isSynched() ) {
			//System.out.println("Producer Not synched in paused yet!");
		}
		while( !consumer.isSynched() ) {
			//System.out.println("Consumer Not synched in paused yet!");
		}
		
		System.out.println("Clustering is paused.");
	}
	
	public static void main(String[] args) throws InterruptedException {
		String path = "/Users/rockyrock/Desktop/testCSV/2013_04_19_22.csv";
		Clusterer clusterer = new Clusterer(path, 50, 30, 1000, 2);
		System.out.println("Starting clustering ...");
		clusterer.startClustering();
		System.out.println("Clustering ended.");
		System.out.println("Number of topics: " + clusterer.getClusters().size());
		Thread.sleep(4000);
		for ( Cluster cluster : clusterer.getClusters().keySet() ) {
			List<Tweet> tweetsList = clusterer.getClusters().get(cluster);
			System.out.println("***Printing a cluster****");
			System.out.println(tweetsList.size());
//			for ( Tweet tweet : tweetsList ) {
//				System.out.println(tweet.getText());
//			}
		}
	}
	
	
}
