package de.uni.konstanz.server;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uni.konstanz.clustering.FastLeaderFollower;
import de.uni.konstanz.dao.CSVDao;
import de.uni.konstanz.models.LeaderFollowerCluster;
import de.uni.konstanz.models.TermFreq;
import de.uni.konstanz.models.Tweet;
import de.uni.konstanz.utils.StreamUtils;

public class LeaderFollowerConsumer {

	private double clusteringThreshold;
	private double mergingThreshold;
	private int minClusterSize;
	private Map<String, TermFreq> termsList;
	private Map<Long, LeaderFollowerCluster> clusters;
	private FastLeaderFollower leaderFollower;
	private int mergingPeriod;
	private int updateCounter = 0;
	
	Map<Long, Integer> clustersLiveTimes;
	private int liveTimePeriod;
	
	public LeaderFollowerConsumer( double clusteringThreshold,
			double mergingThreshold, int mergingPeriod, int liveTimePeriod) {
		this.clusteringThreshold = clusteringThreshold;
		this.mergingThreshold = mergingThreshold;
		this.mergingPeriod = mergingPeriod;
		this.liveTimePeriod = liveTimePeriod;
		clustersLiveTimes = new LinkedHashMap<Long, Integer>();
		minClusterSize = 1;
		termsList = new LinkedHashMap<String, TermFreq>();
		clusters = new LinkedHashMap<Long, LeaderFollowerCluster>();
		leaderFollower = new FastLeaderFollower( clusteringThreshold, liveTimePeriod/2 );
		
	}

	public Map<Long, LeaderFollowerCluster> cluster( List<Tweet> tweets ) {
		
		/*
		 * This was calculating the df from the entire stream (1)
		 */
//		for ( Tweet tweet : tweets ) {
//			StreamUtils.updateTermsList(tweet.getTextTokens(), termsList);
//		}
		
		//This is the new one to (1) i.e to calculate the df from the chunk only
		termsList = StreamUtils.getTermsList(tweets);
		//
		
		Map<Long, LeaderFollowerCluster> chunkClusters =
				leaderFollower.getClusters(tweets, termsList, tweets.size());
		int chunkMinClusterSize = updateMinClusterSize(chunkClusters);
		chunkClusters = leaderFollower.filterClusters(chunkClusters, chunkMinClusterSize);
		/**
		 * This is the old version. If you want just remove the new merge statement
		 * and uncomment the following lines.
//		clusters.putAll(chunkClusters);
//		clusters = leaderFollower.mergeClusters(clusters, mergingThreshold);
		 */
		Map<Long, Integer> oldClustersMergeSizes = getClustersSizesAsMap(clusters);
		
		clusters = leaderFollower.mergeClusters(chunkClusters, clusters, mergingThreshold);
		Map<Long, Integer> newClustersMergeSizes = getClustersSizesAsMap(clusters);
		
		List<Long> modifiedClusters = getModifiedClustersFromChunkMerge(
				oldClustersMergeSizes, newClustersMergeSizes);
		
		updateClustersLiveTimes(clustersLiveTimes, modifiedClusters, liveTimePeriod);
		filterStaticClusters(clusters, clustersLiveTimes);
		decreaseClustersLiveTimes(clustersLiveTimes);
		modifiedClusters.clear();
		
		updateCounter++;
		if ( updateCounter == mergingPeriod ) {
			//System.err.println("I AM DOING PERIODIC MERGING!");
			//System.out.println("Cluster size before merge: " + clusters.size());
			oldClustersMergeSizes = getClustersSizesAsMap(clusters);
			clusters = leaderFollower.mergeClusters(clusters, mergingThreshold);
			//System.out.println("Cluster size after merge: " + clusters.size());
			newClustersMergeSizes = getClustersSizesAsMap(clusters);
			modifiedClusters = getModifiedClustersFromFullMerge(
					oldClustersMergeSizes, newClustersMergeSizes);
			updateClustersLiveTimes(clustersLiveTimes, modifiedClusters, liveTimePeriod);
			modifiedClusters.clear();
			updateCounter = 0;
		}
		
		return clusters;
	}
	
	public List<Long> getModifiedClustersFromChunkMerge( Map<Long, Integer> oldClustersSizes,
			Map<Long, Integer> newClustersSizes) {
		List<Long> modifiedClusters = new LinkedList<Long>();
		
		/*
		 * The reason why the number of modified clusters is lower than
		 * the number of chunk clusters is that more than one tweet could
		 * be merged with one cluster. So here we having only one cluster affected
		 * but more than one tweet difference in the number of chunk clusters.
		 */
		
		for ( Long clusterID : newClustersSizes.keySet() ) {
			Integer oldClusterSize = oldClustersSizes.get(clusterID);
			
			if ( oldClusterSize == null ) {
				//So this is a new cluster from chunkClusters
				modifiedClusters.add(clusterID);
			}
			else {
				int newClusterSize = newClustersSizes.get(clusterID);
				if ( newClusterSize != oldClusterSize ) {
					modifiedClusters.add(clusterID);
				}
			}
		}
		
		return modifiedClusters;
	}
	
	public List<Long> getModifiedClustersFromFullMerge( Map<Long, Integer> oldClustersSizes,
			Map<Long, Integer> newClustersSizes) {
		List<Long> modifiedClusters = new LinkedList<Long>();
		
//		for ( Long oldClusterID : oldClustersSizes.keySet() ) {
//			Integer oldClusterSize = oldClustersSizes.get(oldClusterID);
//			Integer newClusterSize = newClustersSizes.get( oldClusterID );
//			if ( newClusterSize != null ) {
//				if ( newClusterSize != oldClusterSize ) {
//					modifiedClusters.add(oldClusterID);
//				}
//			}
//		}
		
		for ( Long newClusterID : newClustersSizes.keySet() ) {
			int newClusterSize = newClustersSizes.get(newClusterID);
			int oldClusterSize = oldClustersSizes.get(newClusterID);
			if ( newClusterSize != oldClusterSize ) {
				modifiedClusters.add(newClusterID);
			}
		}
		
		return modifiedClusters;
	}
	
	public Map<Long, Integer> getClustersSizesAsMap( Map<Long, LeaderFollowerCluster> clusters ) {
		Map<Long, Integer> clustersSizes = 
				new LinkedHashMap<Long, Integer>();
		
		for ( Long clusterID : clusters.keySet() ) {
			LeaderFollowerCluster c = clusters.get(clusterID);
			clustersSizes.put( clusterID, c.getClusterSize() );
		}
		
		return clustersSizes;
	}
	
	public void filterStaticClusters( Map<Long, LeaderFollowerCluster> clusters, 
			Map<Long, Integer> clustersLiveTimes ) {
		
		Iterator<Entry<Long,Integer>> it = clustersLiveTimes.entrySet().iterator();
		
		while( it.hasNext() ) {
			Entry<Long,Integer> entry = it.next();
			Long clusterID = entry.getKey();
			int liveTime = entry.getValue();
			//System.out.printf( "ClusterID: %d, livetime: %d\n", clusterID, liveTime );
			if ( liveTime == 0 ) {
				it.remove();
				clusters.remove(clusterID);
			}
		}
		
		//System.out.println("\n\n");
		
	}
	
	public void decreaseClustersLiveTimes( Map<Long, Integer> clustersLiveTimes ) {
		for ( Long clusterID : clustersLiveTimes.keySet() ) {
			int liveTime = clustersLiveTimes.get(clusterID);
			liveTime--;
			clustersLiveTimes.put(clusterID, liveTime);
		}
	}
	
	public void updateClustersLiveTimes( Map<Long, Integer> clustersLiveTimes,
			List<Long> updatedClusters, int liveTimePeriod ) {
		
		for ( Long clusterID : updatedClusters ) {
			//System.out.printf("Putting liveTimePeriod: %d\n", liveTimePeriod);
			clustersLiveTimes.put(clusterID, liveTimePeriod);
		}
		
	}

	public int updateMinClusterSize( Map<Long, LeaderFollowerCluster> clusters ) {
		int size = clusters.size();
		int total = 0;
		for ( Long key : clusters.keySet() ) {
			LeaderFollowerCluster c = clusters.get(key);
			total += c.getClusterSize();
		}
		//System.out.println("Number of clusters: " + size);
		//System.out.println("total items in clusters: " + total);
		double average = (double) total / size;
		return (int) Math.ceil( average );
	}



	public int getMinClusterSize() {
		return minClusterSize;
	}

	public static void main(String[] args) {
		System.out.println( "Reading tweets..." );
		long startTime = System.currentTimeMillis();
		File csvFilePath = new 
				File("/Users/rockyrock/Desktop/testCSV/2013_04_19_22_filtered.csv");
		LinkedList<Tweet> tweetsStack = CSVDao.read(csvFilePath, 300000);
		System.out.println( "Done reading tweets." );
		LeaderFollowerConsumer lfConsumer = new 
				LeaderFollowerConsumer(0.4, 0.2, 10, 10);
		Map<Long, LeaderFollowerCluster> lfClusters = new LinkedHashMap<Long, LeaderFollowerCluster>();
		List<Tweet> tempTweetsList  = new LinkedList<Tweet>();

		System.out.println( "Started to cluster tweets..." );

		while( !tweetsStack.isEmpty() ) {
			for ( int j = 0; j <= 1000 && j < tweetsStack.size(); j++ ) {
				tempTweetsList.add( tweetsStack.poll() );
			}
			lfClusters = lfConsumer.cluster(tempTweetsList);
			tempTweetsList.clear();
		}

		System.out.println( "Final clusters size: " + lfClusters.size() );
		System.out.println( "Done clustering." );
		long elapsedTime = System.currentTimeMillis() - startTime;
		System.out.println( "Clustering took: " + elapsedTime/1000 + " seconds." );

	}

	public double getClusteringThreshold() {
		return clusteringThreshold;
	}

	public void setClusteringThreshold(double clusteringThreshold) {
		this.clusteringThreshold = clusteringThreshold;
	}

	public double getMergingThreshold() {
		return mergingThreshold;
	}

	public void setMergingThreshold(double mergingThreshold) {
		this.mergingThreshold = mergingThreshold;
	}

	public int getLiveTimePeriod() {
		return liveTimePeriod;
	}

	public void setLiveTimePeriod(int liveTimePeriod) {
		this.liveTimePeriod = liveTimePeriod;
	}

	public FastLeaderFollower getLeaderFollower() {
		return leaderFollower;
	}


}
















