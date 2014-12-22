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

public class ClustersOrderTest {
	
	public static void main(String[] args) {
		double threshold = 0.6;
		File csvFilePath = new 
				File("/Users/rockyrock/Desktop/testCSV/2013_04_19_22_filtered_sorted.csv");
		List<Tweet> tweets = CSVDao.read(csvFilePath, 10000);
		Map<String, TermFreq> terms = new HashMap<String, TermFreq>();
		Map<Long, LeaderFollowerCluster> clusters;
		FastLeaderFollower leaderFollower = new FastLeaderFollower( threshold, 10 );
		System.out.println( "Filling the terms list ... " );
		for ( Tweet tweet : tweets ) {
			StreamUtils.updateTermsList(tweet.getTextTokens(), terms);
		}
		System.out.println( "Terms list size: " + terms.size() );
		
		System.out.println( "Started to cluster ..." );
		long oldTime = System.currentTimeMillis();
		clusters = leaderFollower.getClusters(tweets, terms, tweets.size());
		System.out.println( "Number of clusters: " + clusters.size() );
		System.out.printf( "Clustering took: %d sec.\n", (System.currentTimeMillis()-oldTime) / 1000 );
		
		clusters = leaderFollower.mergeClusters(clusters, 0.4);
		
		StreamUtils.printClusters( new File("/Users/rockyrock/Desktop/clusters.txt"),
				clusters, toTweetMap(tweets), 0);
	}
	
	public static Map<Long, Tweet> toTweetMap( List<Tweet> tweets ) {
		Map<Long, Tweet> map = new HashMap<Long, Tweet>();
		for ( Tweet t : tweets ) {
			map.put(t.getId(), t);
		}
		return map;
	}
}
