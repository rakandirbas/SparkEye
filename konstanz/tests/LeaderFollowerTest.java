package de.uni.konstanz.tests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uni.konstanz.analysis.Preprocessor;
import de.uni.konstanz.clustering.LeaderFollower;
import de.uni.konstanz.dao.CSVDao;
import de.uni.konstanz.models.LeaderFollowerCluster;
import de.uni.konstanz.models.TermFreq;
import de.uni.konstanz.models.Tweet;
import de.uni.konstanz.utils.MapUtils;
import de.uni.konstanz.utils.StreamUtils;

public class LeaderFollowerTest {

	public static void main(String[] args) {
		//		String path = "/Users/rockyrock/Desktop/CSVToLuceneIndex";
		//		File indexPath = new File( path );
		//		LuceneTweetDao tweetDao = new LuceneTweetDao( indexPath );
		//		tweetDao.openReader();
		//		String query = "1370854169683";
		//		Tweet tweet = tweetDao.get(query);
		int clusterMinSize = 10;
		double threshold = 0.5;
		File csvFilePath = new 
				File("/Users/rockyrock/Desktop/testCSV/2013_04_19_22.csv");
		List<Tweet> tweets = CSVDao.read(csvFilePath, 300000);
		
		System.out.println( "Started to filter ..." );
		long oldTime = System.currentTimeMillis();
		tweets = Preprocessor.preprocessor.filter(tweets);
		System.out.printf( "Filtering took: %d sec.\n", (System.currentTimeMillis()-oldTime) / 1000 );
		System.out.println( "Size after filtering: " + tweets.size() );
		//writeTweets(tweets);
		Map<String, TermFreq> terms = new HashMap<String, TermFreq>();
		Map<Long, LeaderFollowerCluster> clusters = new HashMap<Long, LeaderFollowerCluster>();
		LeaderFollower leaderFollower = new LeaderFollower( threshold, 10 );
		System.out.println( "Filling the terms list ... " );
		for ( Tweet tweet : tweets ) {
			//System.out.println( tweet.getTokensList().size() );
			StreamUtils.updateTermsList(tweet.getTextTokens(), terms);
		}
		System.out.println( "Terms list size: " + terms.size() );
		try {
			printTermsList(terms);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		System.out.println( "Started to cluster ..." );
		oldTime = System.currentTimeMillis();
		clusters = leaderFollower.getClusters(clusters, tweets, terms, tweets.size());
		System.out.println( "Number of clusters: " + clusters.size() );
		System.out.printf( "Clustering took: %d sec.\n", (System.currentTimeMillis()-oldTime) / 1000 );
		try {
			printClusters(clusters, toTweetMap(tweets), clusterMinSize);
		} catch (IOException e) {
			e.printStackTrace();
		}
		tweets.clear();
		printNumOfClusters(clusters, clusterMinSize);
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
		File file = new File( "/Users/rockyrock/Desktop/tweetsList.txt" );
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
		
		File file = new File( "/Users/rockyrock/Desktop/termsList.txt" );
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

	public static void printClusters( Map<Long, LeaderFollowerCluster> clusters, 
			Map<Long, Tweet> tweets, int minSize ) throws IOException {

		File file = new File( "/Users/rockyrock/Desktop/clusteringOutput.txt" );
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
					//bw.write( i + "- Tweet_ID: " + tweetID + " -> " );
					bw.write( i + "- " );
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







