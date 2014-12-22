package de.uni.konstanz.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uni.konstanz.models.Cluster;
import de.uni.konstanz.models.Tweet;

public class ClustersUtils {
	public static List<Tweet> getClusterTweets( 
			Cluster cluster, Map<Long, Tweet> tweets ) {
		List<Tweet> clusterTweets = new LinkedList<Tweet>();
		
		for ( Long tweetID : cluster.getDocumentsList() ) {
			clusterTweets.add( tweets.get(tweetID) );
		}
		
		return clusterTweets;
	}
}
