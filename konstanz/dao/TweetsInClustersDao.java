package de.uni.konstanz.dao;

import java.util.List;
import java.util.Map;

import de.uni.konstanz.models.LeaderFollowerCluster;
import de.uni.konstanz.models.Tweet;

/**
 * This interface is used to store the tweets that belong
 * to clusters so it's then possible to print the contents 
 * of each cluster. It also has methods to remove tweets 
 * that shouldn't be kept because their clusters were filtered out
 * @author rockyrock
 *
 */
public interface TweetsInClustersDao {
	
	public Map<Long, Tweet> getTweetsInClusters();
	public void storeTweetsInClusters(Map<Long, LeaderFollowerCluster> clusters, 
			List<Tweet> tweetsList);
	public void filterTweetsInCluster(Map<Long, LeaderFollowerCluster> clusters);
	

}
