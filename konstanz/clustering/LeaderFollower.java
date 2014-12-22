package de.uni.konstanz.clustering;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import de.uni.konstanz.analysis.FastCosineSimilarityCalculator;
import de.uni.konstanz.models.LeaderFollowerCluster;
import de.uni.konstanz.models.TermFreq;
import de.uni.konstanz.models.Tweet;

public class LeaderFollower {
	private double threshold;
	int termLifetime;

	private static Logger logger = Logger.getLogger(LeaderFollower.class);

	public LeaderFollower( double threshold, int termLifetime ) {
		this.threshold = threshold;
		this.termLifetime = termLifetime;
	}

	public  Map<Long, LeaderFollowerCluster> getClusters( Map<Long, LeaderFollowerCluster> clusters, 
			List<Tweet> tweets, Map<String, TermFreq> terms, 
			int totalNumberOfDocs ) {
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

				Long assignedClusterID = assignTweetToCluster( similarityArray );
				Double tweetClusterSimilarity = similarityArray.get(assignedClusterID); 
				if ( tweetClusterSimilarity > threshold ) {
					LeaderFollowerCluster cluster = clusters.get(assignedClusterID);
					//System.out.println( "Assigned the following tweet: " + tweet.getId());
					//System.out.println( tweet.getTokenedText() );
					//System.out.println( getTweet(tweet.getId(), tweets) );
					//System.out.println( "TO the following cluster:" );
					//System.out.println( cluster );
					//System.out.println("Assigned Tweet-Cluster similarity: "
					//		+ tweetClusterSimilarity + "\n");
					cluster.getDocumentsList().add(tweet.getId());
					cluster.updateClusterCentroid( tweetWeightsMap );
					//System.out.println( cluster +"\n" );
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
	
	public String getTweet( Long id, List<Tweet> tweets) {
		String txt = "";
		
		for ( Tweet t : tweets ) {
			if ( t.getId() == id ) {
				txt += "Tweet ID-> "+ t.getId() + ": " + t.getTokenedText();
			}
				
		}
		
		return txt;
	}

}










