package de.uni.konstanz.tests;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uni.konstanz.clustering.FastLeaderFollower;
import de.uni.konstanz.dao.CSVDao;
import de.uni.konstanz.models.LeaderFollowerCluster;
import de.uni.konstanz.models.TermFreq;
import de.uni.konstanz.models.Tweet;
import de.uni.konstanz.utils.StreamUtils;

public class ClusterSizeTest {
	public static void main(String[] args) {
		int minClusterSize = 5;
		double threshold = 0.7;
		File csvFilePath = new 
				File("/Users/rockyrock/Desktop/testCSV/2013_04_19_22_filtered.csv");
		List<Tweet> tweets = CSVDao.read(csvFilePath, 5000);
		Map<String, TermFreq> terms = new HashMap<String, TermFreq>();
		Map<Long, LeaderFollowerCluster> clusters = new HashMap<Long, LeaderFollowerCluster>();
		FastLeaderFollower leaderFollower = new FastLeaderFollower( threshold, 10 );
		for ( Tweet tweet : tweets ) {
			StreamUtils.updateTermsList(tweet.getTextTokens(), terms);
		}
		System.out.println( "Terms list size: " + terms.size() );
		System.out.println( "Started to cluster ..." );
		long t0 = System.currentTimeMillis();
		clusters = leaderFollower.getClusters(tweets, terms, tweets.size());
		long t1 = System.currentTimeMillis() - t0;
		t1 /= 1000;
		System.out.println( "Number of clusters before merge: " + clusters.size() );
		System.out.println("Clustering time: " + t1);

		int total = 0;

		for ( Long key : clusters.keySet() ) {
			LeaderFollowerCluster c = clusters.get(key);
			total += c.getClusterSize();
		}
		
		System.out.println( "Total number of items in clusters before merge: " + total );
		t0 = System.currentTimeMillis();
		clusters = leaderFollower.mergeClusters(clusters, threshold*0.5);
		t1 = System.currentTimeMillis() - t0;
		t1 /= 1000;
		System.out.println( "Number of clusters after merge: " + clusters.size() );
		System.out.println("Merging time: " + t1);
		total = 0;

		for ( Long key : clusters.keySet() ) {
			LeaderFollowerCluster c = clusters.get(key);
			total += c.getClusterSize();
		}

		System.out.println( "Total number of items in clusters after merge: " + total );
	}
}










