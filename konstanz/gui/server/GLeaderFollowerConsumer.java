package de.uni.konstanz.gui.server;

import java.util.List;
import java.util.Map;

import de.uni.konstanz.models.LeaderFollowerCluster;
import de.uni.konstanz.models.Tweet;
import de.uni.konstanz.server.LeaderFollowerConsumer;


public class GLeaderFollowerConsumer  {
	private LeaderFollowerConsumer lfConsumer;
	
	public GLeaderFollowerConsumer(double clusteringThreshold, int clusterLiveTime) {
		/*
		 * I need periodic merging to avoid fragmentation
		 */
		lfConsumer = new LeaderFollowerConsumer(clusteringThreshold,
				clusteringThreshold, clusterLiveTime/2, clusterLiveTime);
	}
	
	public Map<Long, LeaderFollowerCluster> cluster( List<Tweet> tweets ) {
		return lfConsumer.cluster(tweets);
	}
	
	public void setClusteringThreshold(double clusteringThreshold) {
		lfConsumer.setClusteringThreshold(clusteringThreshold);
		lfConsumer.setMergingThreshold(clusteringThreshold);
		lfConsumer.getLeaderFollower().setThreshold(clusteringThreshold);
	}
	
	public void setClusterLiveTime(int clusterLiveTime) {
		lfConsumer.setLiveTimePeriod(clusterLiveTime);
	}
	
}
