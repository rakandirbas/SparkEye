package de.uni.konstanz.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import de.uni.konstanz.models.Cluster;
import de.uni.konstanz.models.ClusterStats;
import de.uni.konstanz.models.ClusterTerm;
import de.uni.konstanz.models.TermFreq;
import de.uni.konstanz.models.Tweet;

public class StreamUtils {

	private static Logger logger = Logger.getLogger(StreamUtils.class);
	
	public static Map<String, TermFreq> getTermsList(List<Tweet> tweets) {
		
		Map<String, TermFreq> terms = new LinkedHashMap<String, TermFreq>();
		
		for ( Tweet tweet : tweets ) {
			Set<String> termsSet = new HashSet<String>();
			for ( String token : tweet.getTextTokens() ) {
				if ( terms.containsKey(token) ) {
					TermFreq tF = terms.get(token);
					int termFreq = tF.getTotalFrequency();
					termFreq++;
					tF.setTotalFrequency(termFreq);
				}
				else {
					TermFreq tF = new TermFreq();
					tF.setTotalFrequency(1);
					terms.put(token, tF);
				}
				termsSet.add(token);
			}
			
			//Second update the document frequnecy.
			//Same term must be updated only once, that's why it's a set!
			for ( String token : termsSet ) {
				TermFreq tF = terms.get( token );
				int docFreq = tF.getDocFrequency();
				docFreq++;
				tF.setDocFrequency(docFreq);
				terms.put(token, tF);
			}
		}
		
		return terms;
	}

	/**
	 * updates the terms list with the terms/tokens of a document/tweet
	 * @param tokens
	 * @param terms
	 */
	public static void updateTermsList( String[] tokens, 
			Map<String, TermFreq> terms  ) {
		Set<String> termsSet = new HashSet<String>();
		//First update the total term frequency.
		//Same term can be updated twice cuz that's the idea!
		for ( String token : tokens ) {
			if ( terms.containsKey(token) ) {
				TermFreq tF = terms.get(token);
				int termFreq = tF.getTotalFrequency();
				termFreq++;
				tF.setTotalFrequency(termFreq);
				terms.put(token, tF);
				//System.out.println( "Added: " + token );
			}
			else {
				TermFreq tF = new TermFreq();
				tF.setTotalFrequency(1);
				terms.put(token, tF);
				//System.out.println( "Added: " + token );
			}
			termsSet.add(token);
		}

		//Second update the document frequnecy.
		//Same term must be updated only once, that's why it's a set!
		for ( String token : termsSet ) {
			TermFreq tF = terms.get( token );
			int docFreq = tF.getDocFrequency();
			docFreq++;
			tF.setDocFrequency(docFreq);
			terms.put(token, tF);
		}

	}
	
	public static void printTrendsToFile( File outputFile, Map<Long, ? extends Cluster> clusters,   
			Map<Long, Tweet> tweets, List<Long> trendsList,int unit ) {
		File file = new File( outputFile + ".txt" );
		BufferedWriter bw = FileUtils.getFileWriter( file, true );
		try {
			bw.append(String.format( "\n\n####\n\nTrends at unit: %d\n", unit ));
			for ( Long trendID : trendsList ) {
				Cluster c = clusters.get(trendID);
				int i = 1;

				bw.append(c.toString() + "\n");
				bw.append( "\nTweets that belong to the cluster:\n" );
				for ( Long tweetID : c.getDocumentsList() ) {
					bw.append( i + "- Tweet_ID: " + tweetID + " -> " );
					bw.append( tweets.get(tweetID).getText() + "\n" );
					i++;
				}
				bw.append("\n\n");

			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Error while printing the trends into file", e);
		}

		try {
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Error while closing the writer " +
					"after printing the trends into file", e);
		}
	}

	/**
	 * Prints clusters to a file where clusters are in sorted order
	 * @param outputFile
	 * @param clusters
	 * @param tweets a map of all tweets that belong to all clusters.
	 * @param unit
	 */
	public static void printClusters(File outputFile, Map<Long, ? extends Cluster> clusters,   
			Map<Long, Tweet> tweets, int unit ) {

		printClusters(outputFile, getSortedClusters(clusters), tweets, unit);
	}

	/**
	 * Prints clusters to a file where the order of clusters are subject to the 
	 * order defined in the list of clusters. So the they are not necessarly sorted.
	 * @param outputFile
	 * @param clusters
	 * @param tweets tweets a map of all tweets that belong to all clusters.
	 * @param unit
	 */
	public static void printClusters(File outputFile, List<? extends Cluster> clusters,   
			Map<Long, Tweet> tweets, int unit ) {

		File file = new File( outputFile + ".txt" );
		BufferedWriter bw = FileUtils.getFileWriter( file, true );

		try {
			bw.append(String.format( "\n\n####\n\nClusters at unit: %d\n", unit ));
			for ( Cluster c : clusters ) {
				int i = 1;

				bw.append(c.toString() + "\n");
				bw.append( "\nTweets that belong to the cluster:\n" );
				for ( Long tweetID : c.getDocumentsList() ) {
					bw.append( i + "- Tweet_ID: " + tweetID + " -> " );
					bw.append( tweets.get(tweetID).getText() + "\n" );
					i++;
				}
				bw.append("\n\n");

			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Error while printing the clusters into file", e);
		}

		try {
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Error while closing the writer " +
					"after printing the clusters into file", e);
		}

	}

	/**
	 * Returns a list of the clusters sorted in descending order in terms of 
	 * clusters size. 
	 * @param clusters
	 * @return
	 */
	public static List<? extends Cluster> getSortedClusters( Map<Long, ? extends Cluster> clusters ) {
		List<Cluster> sortedClusters = new LinkedList<Cluster>();

		for ( Long clusterID : clusters.keySet() ) {
			Cluster c = clusters.get(clusterID);
			sortedClusters.add(c);
		}

		Collections.sort(sortedClusters, Collections.reverseOrder());

		return sortedClusters;
	}

	/**
	 * Returns a map that contains the list of terms [with their frequency]
	 * of each cluster.
	 * @param clusters
	 * @param tweets a map of all tweets that belong to all clusters.
	 * @return
	 */
	public static Map<Long, List<ClusterTerm>> getAllClustersTerms(
			Map<Long, ? extends Cluster> clusters, Map<Long, Tweet> tweets) {

		Map<Long, List<ClusterTerm>> clustersTermsMap = 
				new HashMap<Long, List<ClusterTerm>>();

		for ( Long clusterID : clusters.keySet() ) {
			Cluster c = clusters.get(clusterID);
			List<Tweet> clusterTweetsList =
					getClusterTweetsList( c, tweets );
			List<ClusterTerm> clusterTermsList =
					getClusterTerms( clusterTweetsList );
			clustersTermsMap.put( clusterID, clusterTermsList );
		}

		return clustersTermsMap;

	}

	/**
	 * Returns a list containing all terms in a the cluster with their frequency.
	 * @param clusterTweetsList the list of tweets that belong to the cluster.
	 * @return
	 */
	public static List<ClusterTerm> getClusterTerms(List<Tweet> clusterTweetsList) {
		Set<String> termsSet = new HashSet<String>();
		List<ClusterTerm> clusterTermsList = new ArrayList<ClusterTerm>();

		Map<String, ClusterTerm> termsMap = 
				new HashMap<String, ClusterTerm>();

		for ( Tweet tweet : clusterTweetsList ) {
			for( String term : tweet.getTextTokens() ) {
				if ( termsSet.contains(term) ) {
					ClusterTerm clusterTerm = 
							termsMap.get(term);
					clusterTerm.updateFreq();
				}
				else {
					termsSet.add(term);
					ClusterTerm clusterTerm = new ClusterTerm(term);
					clusterTerm.updateFreq();
					termsMap.put(term, clusterTerm);
				}
			}
		}

		for ( String term : termsMap.keySet() ) {
			clusterTermsList.add( termsMap.get(term) );
		}

		return clusterTermsList;
	}

	/**
	 * Returns a list of all tweets that belong to a cluster.
	 * @param cluster
	 * @param tweets a map of all tweets that belong to all clusters.
	 * @return
	 */
	public static List<Tweet> getClusterTweetsList( Cluster cluster,
			Map<Long, Tweet> tweets ) {
		List<Tweet> tweetsList = new ArrayList<Tweet>();

		for ( Long tweetID : cluster.getDocumentsList() ) {
			Tweet t = tweets.get(tweetID);
			if ( t == null ) {
				throw new NullPointerException("A tweet that belong to" +
						" a cluster is not in the tweets list!");
			}
			else {
				tweetsList.add(t);
			}
		}

		return tweetsList;
	}

	/**
	 * Print the terms that belong to each cluster into output file. Each 
	 * terms is printed with its frequency in the cluster.
	 * @param outputFile the name of the output file
	 * @param clusters 
	 * @param tweets a map of all tweets that belong to all clusters
	 */
	public static void printClustersTerms( File outputFile, 
			Map<Long, ? extends Cluster> clusters, Map<Long, Tweet> tweets, int unit ) {
		File file = new File( outputFile + ".txt" );
		BufferedWriter writer = FileUtils.getFileWriter(file, true);

		Map<Long, List<ClusterTerm>> clustersTerms = 
				getAllClustersTerms(clusters, tweets);

		List<? extends Cluster> sortedClusters = getSortedClusters(clusters);
		try {
			writer.append(String.format( "\n\n####\n\nClusters terms at unit: %d\n", unit ));
			
			for ( Cluster c : sortedClusters ) {

				writer.append( "ClusterID: " + c.getId() + "\n");
				writer.append( "Cluster size: " + c.getClusterSize() + "\n");
				writer.append( "Cluster top terms: " );
				List<ClusterTerm> clusterTerms = clustersTerms.get(c.getId());
				Collections.sort( clusterTerms, Collections.reverseOrder() );

				for ( ClusterTerm term : clusterTerms ) {
					writer.append( term.toString() + " " );
				}

				writer.append( "\n***********************************\n" );


			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Error while writing the clusters terms", e);
		}

		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Error while closing the writer after" +
					" writing the clusters terms", e);
		}

	}

	/**
	 * Prints the sizes of clusters over time, but where the clusters are in
	 * sorted order. 
	 * @param outputFile
	 * @param clusters
	 * @param analyzer
	 * @param lastUnit
	 */
	public static void printClustersAnalyzer(File outputFile,
			Map<Long, ? extends Cluster> clusters,
			Map<Long, ClusterStats> analyzer, int lastUnit) {
		List<? extends Cluster> sortedClusters = 
				getSortedClusters(clusters);

		File file = new File( outputFile + ".txt" );
		BufferedWriter writer = FileUtils.getFileWriter(file);

		try {
			writer.write( "ClusterID,");
			for ( int i = 0; i <= lastUnit; i++ ) {
				writer.write(i + ",");
			}
			writer.write("\n");
			for ( Cluster c : sortedClusters ) {
				Long clusterID = c.getId();
				writer.write( clusterID + ",");
				ClusterStats clusterStats = 
						analyzer.get(clusterID);
				for ( int i = 0; i <= lastUnit; i++ ) {
					Integer clusterSize = clusterStats.getSizesPerUnit().get(i);
					if ( clusterSize == null ) {
						writer.write(0 + ",");
					}
					else {
						writer.write(clusterSize + ",");
					}

				}
				writer.write("\n");
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			logger.error("Error while writing the clusters analyzer", e);
		}

		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Error while closing the writer after" +
					" writing the clusters analyzer", e);
		}

	}

	/**
	 * Prints the sizes of clusters over time, but where the clusters are NOT in
	 * sorted order. 
	 * @param outputFile
	 * @param clusters
	 * @param analyzer
	 * @param lastUnit
	 */
	public static void printClustersAnalyzer(File outputFile,
			Map<Long, ClusterStats> analyzer, int lastUnit) {
		File file = new File( outputFile + ".txt" );
		BufferedWriter writer = FileUtils.getFileWriter(file);
		try {
			writer.write( "ClusterID\t");
			for ( int i = 0; i <= lastUnit; i++ ) {
				writer.write(i + "\t");
			}
			writer.write("\n");
			for ( Long clusterID : analyzer.keySet() ) {

				writer.write( clusterID + "\t");
				ClusterStats clusterStats = 
						analyzer.get(clusterID);
				for ( int i = 0; i <= lastUnit; i++ ) {
					Integer clusterSize = clusterStats.getSizesPerUnit().get(i);
					if ( clusterSize == null ) {
						writer.write(0 + "\t");
					}
					else {
						writer.write(clusterSize + "\t");
					}

				}
				writer.write("\n");
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			logger.error("Error while writing the clusters analyzer", e);
		}

		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Error while closing the writer after" +
					" writing the clusters analyzer", e);
		}

	}

	/**
	 * Prints the sizes, assignemnts and loglikelihood ratios of clusters over time,
	 * where the clusters are in sorted order. 
	 * 
	 * @param outputFile
	 * @param clusters
	 * @param analyzer
	 * @param lastUnit
	 */
	public static void printAllStatsInClustersAnalyzer(File outputFile,
			Map<Long, ? extends Cluster> clusters,
			Map<Long, ClusterStats> analyzer, int lastUnit) {
		List<? extends Cluster> sortedClusters = 
				getSortedClusters(clusters);

		File file = new File( outputFile + ".txt" );
		BufferedWriter writer = FileUtils.getFileWriter(file, true);

		try {
			writer.append(String.format( "\n\n####\n\nClusters analysis until unit: %d\n", lastUnit ));
			writer.append( "ClusterID\t\t\t\t\t\t");
			for ( int i = 0; i <= lastUnit; i++ ) {
				writer.append(i + "\t");
			}
			writer.append("\n\n\n");
			for ( Cluster c : sortedClusters ) {
				Long clusterID = c.getId();
				writer.append( clusterID + "\t\t\t");
				ClusterStats clusterStats = 
						analyzer.get(clusterID);
				for ( int i = 0; i <= lastUnit; i++ ) {
					Integer clusterSize = clusterStats.getSizesPerUnit().get(i);
					if ( clusterSize == null ) {
						writer.append(0 + "\t");
					}
					else {
						writer.append(clusterSize + "\t");
					}

				}
				writer.append("\n");

				writer.append( "Assignments" + "\t\t\t\t\t\t");
				for ( int i = 0; i <= lastUnit; i++ ) {
					Integer clusterAssignments = clusterStats.getAssignmentsPerUnit().get(i);
					if ( clusterAssignments == null ) {
						writer.append(0 + "\t");
					}
					else {
						writer.append(clusterAssignments + "\t");
					}

				}
				writer.append("\n");

				writer.append( "Ratios" + "\t\t\t\t\t\t");
				for ( int i = 0; i <= lastUnit; i++ ) {
					Double clusterRatio = clusterStats.getRatiosPerUnit().get(i);
					if ( clusterRatio == null ) {
						writer.append(0 + "\t");
					}
					else {
						writer.append(clusterRatio + "\t");
					}

				}

				writer.append("\n\n\n\n");

			}
		}
		catch (IOException e) {
			e.printStackTrace();
			logger.error("Error while writing the clusters analyzer", e);
		}

		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Error while closing the writer after" +
					" writing the clusters analyzer", e);
		}

	}
	
	public static void printTrendsStatsToFile( File outputFile,
			Map<Long, ? extends Cluster> clusters,
			Map<Long, ClusterStats> analyzer, List<Long> trendsList, int lastUnit ) { 

		File file = new File( outputFile + ".txt" );
		BufferedWriter writer = FileUtils.getFileWriter(file, true);
		try {
			writer.append(String.format( "\n\n####\n\nTrends analysis until unit: %d\n", lastUnit ));
			writer.append( "ClusterID\t\t\t\t\t\t");
			for ( int i = 0; i <= lastUnit; i++ ) {
				writer.append(i + "\t");
			}
			writer.append("\n\n\n");
			for ( Long trendID : trendsList ) {
				Cluster c = clusters.get(trendID);
				Long clusterID = c.getId();
				writer.append( clusterID + "\t\t\t");
				ClusterStats clusterStats = 
						analyzer.get(clusterID);
				for ( int i = 0; i <= lastUnit; i++ ) {
					Integer clusterSize = clusterStats.getSizesPerUnit().get(i);
					if ( clusterSize == null ) {
						writer.append(0 + "\t");
					}
					else {
						writer.append(clusterSize + "\t");
					}

				}
				writer.append("\n");

				writer.append( "Assignments" + "\t\t\t\t\t\t");
				for ( int i = 0; i <= lastUnit; i++ ) {
					Integer clusterAssignments = clusterStats.getAssignmentsPerUnit().get(i);
					if ( clusterAssignments == null ) {
						writer.append(0 + "\t");
					}
					else {
						writer.append(clusterAssignments + "\t");
					}

				}
				writer.append("\n");

				writer.append( "Ratios" + "\t\t\t\t\t\t");
				for ( int i = 0; i <= lastUnit; i++ ) {
					Double clusterRatio = clusterStats.getRatiosPerUnit().get(i);
					if ( clusterRatio == null ) {
						writer.append(0 + "\t");
					}
					else {
						writer.append(clusterRatio + "\t");
					}

				}

				writer.append("\n\n\n\n");

			}
		}
		catch (IOException e) {
			e.printStackTrace();
			logger.error("Error while writing the clusters analyzer", e);
		}

		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Error while closing the writer after" +
					" writing the clusters analyzer", e);
		}
	}

}







