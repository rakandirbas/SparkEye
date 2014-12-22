package de.uni.konstanz.tests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uni.konstanz.clustering.FastLeaderFollower;
import de.uni.konstanz.dao.CSVDao;
import de.uni.konstanz.models.LeaderFollowerCluster;
import de.uni.konstanz.models.TermFreq;
import de.uni.konstanz.models.Tweet;
import de.uni.konstanz.utils.MapUtils;
import de.uni.konstanz.utils.StreamUtils;

public class FastLeaderFollowerTest {

	public static void main(String[] args) throws IOException {
		int minClusterSize = 2;
		double threshold = 0.4;
		File csvFilePath = new 
				File("/Users/rockyrock/Desktop/testCSV/2013_04_19_22_filtered_sorted.csv");
		List<Tweet> tweets = CSVDao.read(csvFilePath, 1000);
		
		//System.out.println( "Started to filter ..." );
		//long oldTime = System.currentTimeMillis();
		//tweets = Preprocessor.preprocessor.filter(tweets);
		//System.out.printf( "Filtering took: %d sec.\n", (System.currentTimeMillis()-oldTime) / 1000 );
		//System.out.println( "Size after filtering: " + tweets.size() );
		Map<String, TermFreq> terms = new HashMap<String, TermFreq>();
		Map<Long, LeaderFollowerCluster> clusters = new HashMap<Long, LeaderFollowerCluster>();
		FastLeaderFollower leaderFollower = new FastLeaderFollower( threshold, 10 );
		System.out.println( "Filling the terms list ... " );
		for ( Tweet tweet : tweets ) {
			StreamUtils.updateTermsList(tweet.getTextTokens(), terms);
		}
		System.out.println( "Terms list size: " + terms.size() );
		
		System.out.println( "Started to cluster ..." );
		long oldTime = System.currentTimeMillis();
		clusters = leaderFollower.getClusters(tweets, terms, tweets.size());
		System.out.println( "Number of clusters: " + clusters.size() );
		System.out.printf( "Clustering took: %d sec.\n", (System.currentTimeMillis()-oldTime) / 1000 );
		printClusters(clusters, "fastClusteringOutput", toTweetMap(tweets), minClusterSize);
		printNumOfClusters(clusters, minClusterSize);
		
		
		System.out.println( "\nStarted to filter clusters to min size ..." );
		leaderFollower.filterClusters(clusters, minClusterSize);
		System.out.println( "Number of clusters after filtering: " + clusters.size() );
		
		System.out.println( "\nStarted to merge clusters ..." );
		oldTime = System.currentTimeMillis();
		clusters = leaderFollower.mergeClusters(clusters, threshold/2);
		System.out.println( "Number of clusters after merge: " + clusters.size() );
		System.out.printf( "Merging took: %d sec.\n", (System.currentTimeMillis()-oldTime) / 1000 );
		printClusters(clusters, "clustersAfterMerge", toTweetMap(tweets), minClusterSize);
		
		tweets.clear();
		printNumOfClusters(clusters, minClusterSize);
	}

	public static void printNumOfClusters( Map<Long, LeaderFollowerCluster> clusters, int minSize ) {
		int total = 0;
		for ( Long key : clusters.keySet() ) {
			LeaderFollowerCluster c = clusters.get( key );
			if ( c.getClusterSize() >= minSize ) {
				total++;
			}
		}

		System.out.print( "Total clusters with minimal size: " + total );
	}
	
	public static void writeTweets( List<Tweet> tweets ) {
		File file = new File( "/Users/rockyrock/Desktop/fastTweetsList.txt" );
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			
			for ( Tweet tweet : tweets ) {
				bw.write( "Tweet_ID: " + tweet.getId() + ", " +
						tweet.getTokenedText() + "\n\n");
			}
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void printTermsList( Map<String, TermFreq> terms ) throws IOException {
		
		File file = new File( "/Users/rockyrock/Desktop/fastTermsList.txt" );
		FileWriter fw = null;
		BufferedWriter bw = null;
		fw = new FileWriter(file.getAbsoluteFile());
		bw = new BufferedWriter(fw);
		Map<String, Integer> map = 
				new HashMap<String, Integer>();
		for ( String term : terms.keySet() ) {
			map.put(term, terms.get(term).getTotalFrequency());
		}
		
		MapUtils mapSorter = new MapUtils();
		Map<String,Integer> sortedMap = mapSorter.sort( map );
		for ( Map.Entry<String, Integer> entry : sortedMap.entrySet() ) {
			bw.write( "Term: " + entry.getKey() + ", Freq: " + entry.getValue() + "\n\n" );
		}
		bw.close();
	}

	public static void printClusters( Map<Long, LeaderFollowerCluster> clusters, String name,  
			Map<Long, Tweet> tweets, int minSize ) throws IOException {

		File file = new File( "/Users/rockyrock/Desktop/" + name + ".txt" );
		FileWriter fw = null;
		BufferedWriter bw = null;
		fw = new FileWriter(file.getAbsoluteFile());
		bw = new BufferedWriter(fw);


		for ( Long clusterID : clusters.keySet() ) {
			LeaderFollowerCluster c = clusters.get(clusterID);

			if ( c.getClusterSize() >= minSize ) {
				int i = 1;
				//System.out.println( c );
				bw.write(c.toString() + "\n");
				//System.out.println( "Tweets that belong to the cluster:" );
				bw.write( "\nTweets that belong to the cluster:\n" );
				for ( Long tweetID : c.getDocumentsList() ) {
					//System.out.printf( "Tweet_ID: %d: ", tweetID );
					bw.write( i + "- Tweet_ID: " + tweetID + " -> " );
					//System.out.println(tweets.get(tweetID).getTokenedText());
					bw.write( tweets.get(tweetID).getTokenedText() + "\n" );
					i++;
				}
				//System.out.println( "DONE." );
				bw.write("\n\n");
			}

		}

		bw.close();

	}

	public static Map<Long, Tweet> toTweetMap( List<Tweet> tweets ) {
		Map<Long, Tweet> map = new HashMap<Long, Tweet>();
		for ( Tweet t : tweets ) {
			map.put(t.getId(), t);
		}
		return map;
	}

}







