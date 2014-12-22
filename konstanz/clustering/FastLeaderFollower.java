package de.uni.konstanz.clustering;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import de.uni.konstanz.analysis.FastCosineSimilarityCalculator;
import de.uni.konstanz.models.LeaderFollowerCluster;
import de.uni.konstanz.models.TermFreq;
import de.uni.konstanz.models.Tweet;

public class FastLeaderFollower {
	private double threshold;
	private int termLifetime;

	private static Logger logger = Logger.getLogger(LeaderFollower.class);

	public FastLeaderFollower( double threshold, int termLifetime ) {
		this.threshold = threshold;
		this.termLifetime = termLifetime;
	}

	public  Map<Long, LeaderFollowerCluster> getClusters( List<Tweet> tweets, 
			Map<String, TermFreq> terms, int totalNumberOfDocs ) {
		 Map<Long, LeaderFollowerCluster> clusters = new LinkedHashMap<Long, LeaderFollowerCluster>();
		FastCosineSimilarityCalculator cosineCalc = 
				new FastCosineSimilarityCalculator();

		for ( Tweet tweet : tweets ) {
			Map<Long, Double> similarityArray =
					new HashMap<Long, Double>();

			Map<String, Double> tweetWeightsMap = cosineCalc.getWeightsMap(
					tweet.getTokensList(), totalNumberOfDocs, terms);

			if ( clusters.isEmpty() ) {
				LeaderFollowerCluster c = new LeaderFollowerCluster(termLifetime);
				c.getDocumentsList().add( tweet.getId() );
				c.setTfIdf_weightsMap( tweetWeightsMap );
				clusters.put( c.getId() , c );
			}
			else {

				for ( Long clusterID : clusters.keySet() ) {
					LeaderFollowerCluster cluster = clusters.get(clusterID);

					Double similarity = 
							cosineCalc.getCosineSimilarity(tweetWeightsMap, 
									cluster.getTfIdf_weightsMap());

					similarityArray.put(cluster.getId(), similarity);
				}
				
				if ( similarityArray.isEmpty() )
					logger.fatal("Similarity array is empty while clustering.");

				Long assignedClusterID = assignTweetToCluster( similarityArray );
				Double tweetClusterSimilarity = similarityArray.get(assignedClusterID); 
				if ( tweetClusterSimilarity > threshold ) {
					LeaderFollowerCluster cluster = clusters.get(assignedClusterID);
					cluster.getDocumentsList().add(tweet.getId());
					cluster.updateClusterCentroid( tweetWeightsMap );
				}
				else {
					LeaderFollowerCluster newCluster = new LeaderFollowerCluster(termLifetime);
					newCluster.getDocumentsList().add(tweet.getId());
					newCluster.setTfIdf_weightsMap(tweetWeightsMap);
					clusters.put(newCluster.getId(), newCluster);
				}
				similarityArray.clear();
			}

		}
		return clusters;

	}
	
	/**
	 * Merges the clusters in the cluster list that are similar
	 * according to the specified threshold. 
	 * @param clusters
	 * @param threshold
	 * @return
	 */
	public Map<Long, LeaderFollowerCluster> mergeClusters( Map<Long, LeaderFollowerCluster> clusters, 
			double threshold ) {
		if ( clusters.size() < 2 ) 
			return clusters;
		FastCosineSimilarityCalculator cosineCalc = 
				new FastCosineSimilarityCalculator();
		Map<Long, Double> similarityArray = null;
		Map<Long, LeaderFollowerCluster> newClusters = new LinkedHashMap<Long, LeaderFollowerCluster>();

		Iterator<Entry<Long, LeaderFollowerCluster> > outerIt = clusters.entrySet().iterator();
		while( outerIt.hasNext() ) {
			Entry<Long, LeaderFollowerCluster> outerEntry = outerIt.next();
			Long outerKey = outerEntry.getKey();
			LeaderFollowerCluster outerCluster = outerEntry.getValue();

			Map<String, Double> outerClusterWeightsMap = 
					outerCluster.getTfIdf_weightsMap();
			similarityArray =
					new HashMap<Long, Double>();
			
			
			Iterator<Entry<Long, LeaderFollowerCluster> > innerIt =
					clusters.entrySet().iterator();	
			
			while( innerIt.hasNext() ) {
				Entry<Long, LeaderFollowerCluster> innerEntry = innerIt.next();
				Long innerKey = innerEntry.getKey();
				LeaderFollowerCluster innerCluster = innerEntry.getValue();
				
				if ( (innerKey != outerKey) ) {
					Map<String, Double> innerClusterWeightsMap = 
							innerCluster.getTfIdf_weightsMap();
					Double similarity = 
							cosineCalc.getCosineSimilarity(outerClusterWeightsMap, 
									innerClusterWeightsMap);
					similarityArray.put(innerKey, similarity);
					
				}
				
			}

			if ( similarityArray.isEmpty() ) {
				//Then there's only one cluster (the last cluster)
				if ( !newClusters.containsKey(outerKey) ) {
					newClusters.put(outerKey, outerCluster);
				}
				outerIt.remove();
			}
			else {
				Long assignedClusterID = assignTweetToCluster( similarityArray );
				Double clusterClusterSimilarity = similarityArray.get(assignedClusterID);
				if ( clusterClusterSimilarity > threshold ) {
					LeaderFollowerCluster assignedCluster = clusters.get(assignedClusterID);
					//System.out.println( "Merging: " + outerKey + " with " + assignedClusterID );
					assignedCluster.getDocumentsList().addAll( outerCluster.getDocumentsList() );
					assignedCluster.updateClusterCentroid( outerClusterWeightsMap );
					
					/*
					 * A cluster that was an assigned cluster before and then
					 * merged with a new cluster should be removed,
					 * because it's now an old cluster that got merged.
					 */
					if ( newClusters.containsKey(outerKey) ) {
						newClusters.remove(outerKey);
					}
					newClusters.put( assignedClusterID, assignedCluster );
					outerIt.remove();
				}
				else {
					/* An assigned cluster will be added twice, once
					 * when it was merged, and once when there's no other one to 
					 * merge with. So I need to check if it was added so not
					 * to add it again.
					 */
					if ( !newClusters.containsKey(outerKey) ) {
						newClusters.put(outerKey, outerCluster);
					}
					outerIt.remove();
					
				}
			}
			
		}
		return newClusters;
	}
	
	/**
	 * Merges chunk clusters with existing clusters.
	 * @param chunkClusters
	 * @param clusters
	 * @param threshold
	 * @param updatedClusters a list that will be filled with the clusters that got added newly
	 * or the clusters that got modified because of a merge
	 * @return
	 */
	public Map<Long, LeaderFollowerCluster> mergeClusters( Map<Long, LeaderFollowerCluster> chunkClusters,
			Map<Long, LeaderFollowerCluster> clusters, double threshold ) {
		
		if ( clusters.size() == 0 ) {
			return chunkClusters;
		}
		FastCosineSimilarityCalculator cosineCalc = 
				new FastCosineSimilarityCalculator();
		Map<Long, Double> similarityArray = null;

		Iterator<Entry<Long, LeaderFollowerCluster> > chunkIt = chunkClusters.entrySet().iterator();
		while( chunkIt.hasNext() ) {
			Entry<Long, LeaderFollowerCluster> chunkEntry = chunkIt.next();
			Long chunkClusterID = chunkEntry.getKey();
			LeaderFollowerCluster chunkCluster = chunkEntry.getValue();
			
			Map<String, Double> chunkClusterWeightsMap = 
					chunkCluster.getTfIdf_weightsMap();
			similarityArray =
					new HashMap<Long, Double>();
			
			Iterator<Entry<Long, LeaderFollowerCluster> > clustersIt = clusters.entrySet().iterator();
			while ( clustersIt.hasNext() ) {
				Entry<Long, LeaderFollowerCluster> clusterEntry = clustersIt.next();
				Long clusterID = clusterEntry.getKey();
				LeaderFollowerCluster cluster = clusterEntry.getValue();
				
				Map<String, Double> clusterWeightsMap = 
						cluster.getTfIdf_weightsMap();
				Double similarity = 
						cosineCalc.getCosineSimilarity(chunkClusterWeightsMap, 
								clusterWeightsMap);
				similarityArray.put(clusterID, similarity);
			}
			
			Long assignedClusterID = assignTweetToCluster( similarityArray );
			Double clusterClusterSimilarity = similarityArray.get(assignedClusterID);
			if ( clusterClusterSimilarity > threshold ) {
				LeaderFollowerCluster assignedCluster = clusters.get(assignedClusterID);
				assignedCluster.getDocumentsList().addAll( chunkCluster.getDocumentsList() );
				assignedCluster.updateClusterCentroid( chunkClusterWeightsMap );
			}
			else {
				clusters.put(chunkClusterID, chunkCluster);
			}
			
		}
		return clusters;
	}
	
	public Map<Long, LeaderFollowerCluster> combineClusters(Map<Long, LeaderFollowerCluster> c1, 
			Map<Long, LeaderFollowerCluster> c2, int minClusterSize) {
		System.out.println( "C1 size BEFORE filtering: " + c1.size() );
		System.out.println( "C2 size BEFORE filtering: " + c2.size() );
		c1 = filterClusters(c1, minClusterSize);
		System.out.println( "C1 size after filtering: " + c1.size() );
		c2 = filterClusters(c2, minClusterSize);
		System.out.println( "C2 size after filtering: " + c2.size() );
		c1.putAll(c2);
		System.out.println( "C1 size after combining: " + c1.size() );
		return c1;
	}

	/**
	 * Returns the id of the cluster that has the highest similarity 
	 * to the tweet.
	 * @param similarityArray
	 * @return
	 */
	public Long assignTweetToCluster( Map<Long, Double> similarityArray ) {
		double max = 0;
		Long clusterID = 0L;

		for ( Long id : similarityArray.keySet() ) {
			max  = similarityArray.get(id);
			clusterID = id;
			break;
		}

		for ( Long id : similarityArray.keySet() ) {
			double temp  = similarityArray.get(id);

			if ( temp > max ) {
				max = temp;
				clusterID = id;
			}
		}

		return clusterID;

	}

	/**
	 * Removes clusters whose size is below the minimum size
	 * @param clusters
	 * @param minClusterSize
	 * @return
	 */
	public Map<Long, LeaderFollowerCluster> filterClusters( Map<Long, LeaderFollowerCluster> clusters,
			int minClusterSize) {

		Iterator<Entry<Long, LeaderFollowerCluster> > it = clusters.entrySet().iterator();

		while( it.hasNext() ) {
			Entry<Long, LeaderFollowerCluster> entry = it.next();
			if ( entry.getValue().getClusterSize() < minClusterSize )
				it.remove();
		}

		return clusters;
	}

	/**
	 * Returns the tweets that has the specified id.
	 * @param id
	 * @param tweets
	 * @return
	 */
	public String getTweet( Long id, List<Tweet> tweets) {
		String txt = "";

		for ( Tweet t : tweets ) {
			if ( t.getId() == id ) {
				txt += "Tweet ID-> "+ t.getId() + ": " + t.getTokenedText();
			}

		}

		return txt;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

}










