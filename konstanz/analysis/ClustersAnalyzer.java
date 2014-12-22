package de.uni.konstanz.analysis;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uni.konstanz.models.ClusterStats;
import de.uni.konstanz.models.LeaderFollowerCluster;
import de.uni.konstanz.utils.MapUtils;
import de.uni.konstanz.utils.MathUtils;

/**
 * This class serves to record the sizes of the clusters at each time unit.
 * @author rockyrock
 *
 */

public class ClustersAnalyzer {
	
	/**
	 * Long is the clusterID. First Integer is the unit and ClusterStats 
	 * is the statistics of the cluster at that unit.
	 */
	private Map<Long, ClusterStats> clustersStats;
	private int alpha;
	private double likelihoodThreshold;
	
	public ClustersAnalyzer(int alpha, double likelihoodThreshold) {
		clustersStats = new LinkedHashMap<Long, ClusterStats>();
		this.alpha = alpha;
		this.likelihoodThreshold = likelihoodThreshold;
	}
	
	/**
	 * Adds the current clusters sizes in the specified unit.
	 * @param clusters
	 * @param unit (1st [second] [minute], 2nd ...etc )
	 */
	public void updateClustersStats( Map<Long, LeaderFollowerCluster> clusters, int totalTweetsCount, int unit ) {
		
		Map<Long, ClusterStats> updatedClustersStats = 
				new LinkedHashMap<Long, ClusterStats>();
		
		for ( Long clusterID : clusters.keySet() ) {
			LeaderFollowerCluster c = clusters.get(clusterID);
			int clusterSize = c.getClusterSize();
			
			//This condition is to remove clusters that got filtered 
			//because of a merge.
			if ( clustersStats.containsKey(clusterID) ) {
				ClusterStats singleClusterStats = 
						clustersStats.get(clusterID);
				singleClusterStats.update(clusterSize, totalTweetsCount, unit);
				updatedClustersStats.put(clusterID, singleClusterStats);
				clustersStats.remove(clusterID);
			}
			else {
				ClusterStats singleClusterStats = new ClusterStats(alpha, likelihoodThreshold);
				singleClusterStats.update(clusterSize, totalTweetsCount, unit);
				updatedClustersStats.put(clusterID, singleClusterStats);
			}
			
		}
		clustersStats = updatedClustersStats;
	}
	
	/**
	 * Returns the popularity threshold of a cluster given its ID
	 * @param clusterID
	 * @return
	 */
	public double getPopularityThreshold(long clusterID) {
		ClusterStats singleClusterStats = 
				clustersStats.get(clusterID);
		return singleClusterStats.getPopularityThreshold();
	}
	
	public Map<Long, ClusterStats> getClustersStats() {
		return clustersStats;
	}
	
	public static List<Long> getTrends( ClustersAnalyzer analyzer ) {
		List<Long> trends = new LinkedList<Long>();
		
		for ( Long clusterID : analyzer.getClustersStats().keySet() ) {
			ClusterStats clusterStats = analyzer.getClustersStats().get(clusterID);
			if ( clusterStats.isTrending() )
				trends.add(clusterID);
		}
		
		return trends;
	}
	
	/**
	 * This method returns the clusters at the specified unit that got assigned a 
	 * number of documents that is with in stdCount of standard dev.
	 * @param analyzer
	 * @param unit time unit.
	 * @param stdCount how many standard deviations.
	 * @return
	 */
	public static List<Long> getHotClusters( 
			ClustersAnalyzer analyzer,  Map<Long, LeaderFollowerCluster> clusters, int unit, int stdCount ) {
		List<Long> hotClusters = new LinkedList<Long>();
		List<Integer> assignments = new LinkedList<Integer>();
		
		for ( Long clusterID : analyzer.getClustersStats().keySet() ) {
			ClusterStats clusterStats = analyzer.getClustersStats().get(clusterID);
			assignments.add( clusterStats.getAssignmentsPerUnit().get(unit) );
		}
		double average = MathUtils.getAverage(assignments);
		double std = MathUtils.getStd(assignments, average);
		double threshold = average + ( stdCount * std );
		
		Map<Long, LeaderFollowerCluster> sortedClusters = MapUtils.sortByValue(clusters, true);
		
		for ( Long clusterID : sortedClusters.keySet() ) {
			ClusterStats clusterStats = analyzer.getClustersStats().get(clusterID);
			int clusterAssignments = clusterStats.getAssignmentsPerUnit().get(unit);
			if ( clusterAssignments > threshold ) {
				hotClusters.add(clusterID);
			}
		}
		
		return hotClusters;
	}

}





