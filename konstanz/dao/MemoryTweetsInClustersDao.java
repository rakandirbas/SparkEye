package de.uni.konstanz.dao;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uni.konstanz.models.LeaderFollowerCluster;
import de.uni.konstanz.models.Tweet;

public class MemoryTweetsInClustersDao implements TweetsInClustersDao {
	private Map<Long, Tweet> tweetsInClusters;
	
	public MemoryTweetsInClustersDao() {
		tweetsInClusters = new HashMap<Long, Tweet>();
	}

	@Override
	public Map<Long, Tweet> getTweetsInClusters() {
		return tweetsInClusters;
	}

	/**
	 * Returns a list (HashMap) of the tweets that belong to the clusters.
	 */
	@Override
	public void storeTweetsInClusters(Map<Long, LeaderFollowerCluster> clusters,
			List<Tweet> tweetsList) {
		
		Map<Long, Tweet> tweetsInClusters = getTweetsInClusters();
		//Holds the IDs of tweets that belong to clusters
		LinkedList<Long> tweetsIDs = new 
				LinkedList<Long>();
		Map<Long, Tweet> tweetsMap = new HashMap<Long, Tweet>();
		
		for ( Tweet tweet : tweetsList ) {
			tweetsMap.put(tweet.getId(), tweet);
		}
		
		for ( Long key : clusters.keySet() ) {
			LeaderFollowerCluster c = clusters.get(key);
			tweetsIDs.addAll(c.getDocumentsList());
		}
		
		for ( Long key : tweetsIDs ) {
			
			Tweet t = tweetsMap.get(key);
			
			//Because tweetsList contains only the most recently read
			//tweets so older tweets won't be available in the list
			//but their IDs will be in the clusters.
			if ( t != null ) {
				tweetsInClusters.put( key, t );
			}
		}
		
		filterTweetsInCluster(clusters);
		
	}

	/**
	 * Removes the tweets that should no longer be kept because 
	 * the clusters they belong to were filtered out.
	 */
	@Override
	public void filterTweetsInCluster(Map<Long, LeaderFollowerCluster> clusters) {
		Map<Long, Tweet> filteredTweetsInClusters = new HashMap<Long, Tweet>();
		for ( Long clusterID : clusters.keySet() ) {
			LeaderFollowerCluster c = clusters.get(clusterID);
			
			for ( Long tweetID : c.getDocumentsList() ) {
				filteredTweetsInClusters.put(tweetID, getTweetsInClusters().get(tweetID));
			}
			
		}
		setTweetsInClusters(filteredTweetsInClusters);
	}

	private void setTweetsInClusters(Map<Long, Tweet> tweetsInClusters) {
		this.tweetsInClusters = tweetsInClusters;
	}

}
