package de.uni.konstanz.server;

import java.io.File;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.prefs.Preferences;

import org.apache.log4j.Logger;

import de.uni.konstanz.analysis.ClustersAnalyzer;
import de.uni.konstanz.dao.CSVDao;
import de.uni.konstanz.dao.MemoryTweetsInClustersDao;
import de.uni.konstanz.dao.TweetsInClustersDao;
import de.uni.konstanz.eventdetection.DetectionPipeline;
import de.uni.konstanz.models.LeaderFollowerCluster;
import de.uni.konstanz.models.Tweet;

/*
 * Every chunkSize (1000) I cluster, and every 
 * monitoringThreshold * chunkSize I detect for trends.
 */

public class TweetsConsumer implements Runnable {
	
	private int numberOfTweets = -1;
	private final BlockingQueue<Tweet> buffer;
	private final LeaderFollowerConsumer leaderFollowerConsumer;
	private Map<Long, LeaderFollowerCluster> leaderFollowerClusters;
	private List<Long> leaderFollowerTrends;
	private DetectionPipeline detectionPipeline = new DetectionPipeline(0.5);
	
	//Parameters:
	private final int leader_follower_chunkSize;
	private final double leader_follower_clustering_threshold;
	private final double leader_follower_merging_threshold;
	private final int leader_follower_monitoring_threshold;
	private final String outputpath;
	private final int cluster_stats_std_alpha;
	private final double leader_follower_likelihoodThreshold;
	private final int leader_follower_merging_period;
	private final int leader_follower_clusters_livetime_period;
	
	
	private static Logger logger = Logger.getLogger(TweetsConsumer.class);
	
	public TweetsConsumer( BlockingQueue<Tweet> sharedLocation, Preferences prefs ) {
		//Initialize paramters:
		leader_follower_chunkSize =
				prefs.getInt("leader_follower_chunkSize", 1000);
		leader_follower_clustering_threshold = 
				prefs.getDouble("leader_follower_clustering_threshold", 0.4);
		leader_follower_merging_threshold = 
				prefs.getDouble("leader_follower_merging_threshold", 0.4);
		leader_follower_monitoring_threshold = 
				prefs.getInt("leader_follower_monitoring_threshold", 10);
		outputpath = 
				prefs.get("outputpath", new File("output").getAbsolutePath());
		cluster_stats_std_alpha = prefs.getInt("cluster_stats_std_alpha", 1);
		leader_follower_likelihoodThreshold = prefs.getDouble("leader_follower_likelihoodThreshold", 0.5);
		leader_follower_merging_period = prefs.getInt("leader_follower_merging_period", 10);
		leader_follower_clusters_livetime_period = 
				prefs.getInt("leader_follower_clusters_livetime_period", 10);
		
		buffer = sharedLocation;
		leaderFollowerConsumer = new LeaderFollowerConsumer(
				leader_follower_clustering_threshold,
				leader_follower_merging_threshold, 
				leader_follower_merging_period, 
				leader_follower_clusters_livetime_period);
		leaderFollowerClusters = new LinkedHashMap<Long, LeaderFollowerCluster>();
	}
	
	public TweetsConsumer( BlockingQueue<Tweet> sharedLocation, File CSVFile, Preferences prefs ) {
		this( sharedLocation, prefs );
		numberOfTweets = CSVDao.getNumberOfTweets(CSVFile);
	}
	
	
	@Override
	public void run() {
		//TODO Remove this
		numberOfTweets = 100000;
		long startTime = System.currentTimeMillis();
		System.out.println( "Started to cluster tweets..." );
		LinkedList<Tweet> chunkTweetsList = new LinkedList<Tweet>();
		//Holds the id of the Tweet and the Tweet itself.
		TweetsInClustersDao tweetsInClustersDao = new
				MemoryTweetsInClustersDao();
		ClustersAnalyzer clustersAnalyzer = 
				new ClustersAnalyzer( cluster_stats_std_alpha, 
						leader_follower_likelihoodThreshold );
		int unit = -1;
		int j = -1;
		for ( int i = 0; (numberOfTweets == -1? true : ( i < numberOfTweets ? true : false ));
				i++ ) {
			try {
				
				chunkTweetsList.add(buffer.take());
				if ( chunkTweetsList.size() == leader_follower_chunkSize ) {
					System.out.println(new Date());
					System.out.println(j);
					//unit++;
					leaderFollowerClusters = leaderFollowerConsumer.cluster(chunkTweetsList);
					tweetsInClustersDao.storeTweetsInClusters(leaderFollowerClusters, chunkTweetsList);
					j++;
					if ( j == leader_follower_monitoring_threshold ) {
						unit++;
						clustersAnalyzer.updateClustersStats(leaderFollowerClusters, 
								chunkTweetsList.size() * leader_follower_monitoring_threshold, unit);
						//leaderFollowerTrends = ClustersAnalyzer.getTrends(clustersAnalyzer);
						leaderFollowerTrends = ClustersAnalyzer.getHotClusters(
								clustersAnalyzer, leaderFollowerClusters, unit, 2);
						//printTrends(leaderFollowerTrends, unit); 
						
						
//						detectionPipeline.updateDescriptorsMap(
//								leaderFollowerClusters, tweetsInClustersDao.getTweetsInClusters());
						
						
						//System.out.println( leaderFollowerClusters.size() );
						//System.out.println(detectionPipeline.getGlobalEventsMap().size());
						j = 0;
						
						
						//***********
//						if ( !leaderFollowerTrends.isEmpty() ) {
//							System.out.println( "Printing trends into file..." );
//							StreamUtils.printTrendsToFile(new File(outputpath + "Trends"),
//									leaderFollowerClusters, 
//									tweetsInClustersDao.getTweetsInClusters(), 
//									leaderFollowerTrends, unit);
//							
//							
//							System.out.println( "Printing trends analysis into file..." );
//							StreamUtils.printTrendsStatsToFile(
//									new File(outputpath + "TrendsAnalyzer"), leaderFollowerClusters,
//									clustersAnalyzer.getClustersStats(), leaderFollowerTrends, unit);
//							System.out.println( "Done." );
//						}
						
					}
					
					chunkTweetsList.clear();
				}
				
				
			} catch (InterruptedException e) {
				e.printStackTrace();
				logger.error("Interrupted while consuming tweets", e);
			}
		}
		
		//**************************
		/**
		 * If at the end of the for-loop we don't reach a monitoring 
		 * threshold then we have clusters that didn't pass through
		 * the analyzer to be added to the analyzer because they are new
		 * and didn't get the change to be analyzed because the monitoring
		 * threshold didn't reach its value.
		 */
		unit++;
		clustersAnalyzer.updateClustersStats(leaderFollowerClusters, 
				chunkTweetsList.size() * leader_follower_monitoring_threshold, unit);
		leaderFollowerTrends = ClustersAnalyzer.getTrends(clustersAnalyzer);
		printTrends(leaderFollowerTrends, unit);
		
		//**************************
		
		System.out.println( "#Clusters: " + leaderFollowerClusters.size() );
		long elapsedTime = System.currentTimeMillis() - startTime;
		System.out.println( "Clustering took: " + elapsedTime/1000 + " seconds." );
		
//		System.out.println( "Printing clusters into file..." );
//		StreamUtils.printClusters(new File(outputpath + "TweetsConsumerClusters"),
//				leaderFollowerClusters, 
//				tweetsInClustersDao.getTweetsInClusters(), -1000);
//		System.out.println( "Printing clusters terms into file..." );
//		StreamUtils.printClustersTerms(new File(outputpath + "ClustersTerms"),
//				leaderFollowerClusters, tweetsInClustersDao.getTweetsInClusters(), -1000);
//		System.out.println( "Printing clusters analysis into file..." );
//		StreamUtils.printAllStatsInClustersAnalyzer(
//				new File(outputpath + "ClustersAnalyzer"), leaderFollowerClusters,
//				clustersAnalyzer.getClustersStats(), unit);
		System.out.println( "Done." );
	}
	
	public void printTrends( List<Long> trends, int unit ) {
		System.out.println("########################");
		System.out.printf( "- %d Trends at unit: %d -\n", trends.size(), unit );
		for ( Long clusterID : trends ) {
			System.out.println( clusterID );
		}
		
	}
	
}








