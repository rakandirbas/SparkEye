package de.uni.konstanz.tests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uni.konstanz.clustering.FastLeaderFollower;
import de.uni.konstanz.dao.CSVDao;
import de.uni.konstanz.models.LeaderFollowerCluster;
import de.uni.konstanz.models.TermFreq;
import de.uni.konstanz.models.Tweet;
import de.uni.konstanz.utils.MapUtils;
import de.uni.konstanz.utils.StreamUtils;

public class ParallelLeaderFollowerTest {

	public static void main(String[] args) throws IOException {
		int minClusterSize = 10;
		double threshold = 0.4;
		File csvFilePath = new 
				File("/Users/rockyrock/Desktop/testCSV/2013_04_19_22_filtered.csv");
		LinkedList<Tweet> tweetsStack = CSVDao.read(csvFilePath, 100000);
		LinkedList<Tweet> tweetsList = new LinkedList<Tweet>();
		
		//System.out.println( "Started to filter ..." );
		long oldTime = System.currentTimeMillis();
		//tweetsStack = Preprocessor.preprocessor.filter(tweetsStack);
		tweetsList.addAll(tweetsStack);
		//System.out.printf( "Filtering took: %d sec.\n", (System.currentTimeMillis()-oldTime) / 1000 );
		//System.out.println( "Size after filtering: " + tweetsStack.size() );
		Map<String, TermFreq> terms = new HashMap<String, TermFreq>();
		Map<Long, LeaderFollowerCluster> clusters = new HashMap<Long, LeaderFollowerCluster>();
		FastLeaderFollower leaderFollower = new FastLeaderFollower( threshold, 10 );
		
		//***********************
		
		//***********************
		
		System.out.println( "Started to cluster ..." );
		oldTime = System.currentTimeMillis();
		
		while ( !tweetsStack.isEmpty() ) {
			List<Tweet> tempTweetsList  = new LinkedList<Tweet>();
			
			for ( int j = 0; j <= 1000 && j < tweetsStack.size(); j++ ) {
				tempTweetsList.add( tweetsStack.poll() );
			}
			
			for ( Tweet tweet : tempTweetsList ) {
				StreamUtils.updateTermsList(tweet.getTextTokens(), terms);
			}
			
			Map<Long, LeaderFollowerCluster> tempClusters;
			tempClusters = leaderFollower.getClusters(tempTweetsList, terms, tempTweetsList.size());
			leaderFollower.filterClusters(tempClusters, minClusterSize);
			clusters.putAll(tempClusters);
			clusters = leaderFollower.mergeClusters(clusters, threshold/2);
			tempTweetsList.clear();
			
		}
		System.out.println( "Number of clusters: " + clusters.size() );
		System.out.printf( "Clustering took: %d sec.\n", (System.currentTimeMillis()-oldTime) / 1000 );
		printClusters(clusters, "ParallelClusteringOutput", toTweetMap(tweetsList), minClusterSize);
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







