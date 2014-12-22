package de.uni.konstanz.gui.server;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

import de.uni.konstanz.analysis.ClustersAnalyzer;
import de.uni.konstanz.dao.MemoryTweetsInClustersDao;
import de.uni.konstanz.dao.TweetsInClustersDao;
import de.uni.konstanz.models.LeaderFollowerCluster;
import de.uni.konstanz.models.Tweet;

public class GTweetsConsumer implements Runnable {

	private volatile ThreadState state;
	private volatile ThreadState innerState;
	private BlockingQueue<Tweet> buffer;
	private GLeaderFollowerConsumer leaderFollowerConsumer;
	private Map<Long, LeaderFollowerCluster> leaderFollowerClusters;
	private TweetsInClustersDao tweetsInClustersDao;
	private LinkedList<Tweet> chunkTweetsList;
	private volatile int chuncksCounter = 0;
	private int timeUnits = 0;
	private volatile int numberOfClusters = 0;
	private ClustersAnalyzer clustersAnalyzer;

	//Parameters
	private volatile int chunkSize;
	private volatile double clustering_threshold;
	private volatile int timeSlice;
	private volatile int clusterLiveTime;

	private static Logger logger = Logger.getLogger(GTweetsConsumer.class);

	public GTweetsConsumer(BlockingQueue<Tweet> buffer, int chunkSize,
			double clustering_threshold, int timeSlice, int clusterLiveTime) {
		this.buffer = buffer;
		this.chunkSize = chunkSize;
		this.clustering_threshold = clustering_threshold;
		this.timeSlice = timeSlice;
		this.clusterLiveTime = clusterLiveTime;
		leaderFollowerConsumer = new 
				GLeaderFollowerConsumer(clustering_threshold, clusterLiveTime);
		leaderFollowerClusters = new 
				LinkedHashMap<Long, LeaderFollowerCluster>();
		tweetsInClustersDao = new
				MemoryTweetsInClustersDao();
		chunkTweetsList = new LinkedList<Tweet>();
		clustersAnalyzer = new ClustersAnalyzer(1, 1.0);
		state = ThreadState.RUNNING;
	}

	@Override
	public void run() {
		//		synchronized (this) {
		//			try {
		//				wait(10000);
		//			} catch (InterruptedException e) {
		//				e.printStackTrace();
		//			}
		//		}
		/*
		 * The first while loop is to keep getting items from the buffer
		 */
		while( state != ThreadState.DYING ) {
			if ( state == ThreadState.RUNNING ) {
				//if the buffer has items then process them
				boolean emptyBuffer = true;
				synchronized (buffer) {
					emptyBuffer = buffer.isEmpty();
				}

				if ( emptyBuffer == false) {
					//System.out.println("I'm consuming man");
					//Start doing your stuff
					innerState = ThreadState.RUNNING;
					Tweet item;
					synchronized (buffer) {
						item = buffer.poll();
					}

					work(item);

				}
				//otherwise the thread waits for the buffer to get items
				else {
					//System.out.println("I'm NOT consuming man");
					synchronized (this) {
						try {
							//							System.out.println(new Date());
							//							System.err.println("Consumer is waiting...");
							innerState = ThreadState.WAITING;
							wait(1);
						} catch (InterruptedException e) {
							//e.printStackTrace();
						}
					}
				}

			}
			else if ( state == ThreadState.WAITING ) {
				synchronized (this) {
					try {
						innerState = ThreadState.WAITING;
						wait();
					} catch (InterruptedException e) {
						//e.printStackTrace();
					}
				}
			}
		}

		synchronized (this) {
			innerState = ThreadState.DYING;
		}

	}

	public void work(Tweet tweet) {
		chunkTweetsList.add(tweet);
		if ( chunkTweetsList.size() == chunkSize ) {
			chuncksCounter++;
			leaderFollowerClusters = leaderFollowerConsumer.cluster(chunkTweetsList);
			int totalTweetsCount = chunkTweetsList.size() * chuncksCounter;
			clustersAnalyzer.updateClustersStats(leaderFollowerClusters, totalTweetsCount, chuncksCounter);
			numberOfClusters = leaderFollowerClusters.size();
			tweetsInClustersDao.storeTweetsInClusters(leaderFollowerClusters, chunkTweetsList);
			chunkTweetsList.clear();
//			if ( (chuncksCounter % timeSlice) == 0 ) {
//				timeUnits++;
//			}
		}
	}

	public void pause() {
		state = ThreadState.WAITING;
	}

	public void die() {
		state = ThreadState.DYING;
	}

	public void wakeup() {
		state = ThreadState.RUNNING;
	}

	public ThreadState getState() {
		return state;
	}

	public ThreadState getInnerState() {
		return innerState;
	}

	public boolean isSynched() {
		if ( state == innerState )
			return true;
		else 
			return false;
	}

	public Map<Long, LeaderFollowerCluster> getLeaderFollowerClusters() {
		return leaderFollowerClusters;
	}

	public int getChuncksCounter() {
		return chuncksCounter;
	}

	public TweetsInClustersDao getTweetsInClustersDao() {
		return tweetsInClustersDao;
	}

	public int getNumberOfWindows() {
		return chuncksCounter;
	}
	
	public int getNumberOfClusters() {
		return numberOfClusters;
	}

	public ClustersAnalyzer getClustersAnalyzer() {
		return clustersAnalyzer;
	}

}








