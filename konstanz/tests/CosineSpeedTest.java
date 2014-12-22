package de.uni.konstanz.tests;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uni.konstanz.analysis.FastCosineSimilarityCalculator;
import de.uni.konstanz.dao.CSVDao;
import de.uni.konstanz.models.TermFreq;
import de.uni.konstanz.models.Tweet;
import de.uni.konstanz.utils.StreamUtils;

public class CosineSpeedTest {
	public static void main(String[] args) {
		int clusterMinSize = 5;
		File csvFilePath = new 
				File("/Users/rockyrock/Desktop/testCSV/2013_04_19_22.csv");
		
		System.out.println( "Filling the list with tweets ..." );
		List<Tweet> tweets = CSVDao.read(csvFilePath, 50000);
		System.out.println( "Done filling the list." );
		
		FastCosineSimilarityCalculator cosineCalc = 
				new FastCosineSimilarityCalculator();
		
		Map<String, TermFreq> terms = new HashMap<String, TermFreq>();
		
		System.out.println( "Filling the terms list ... " );
		for ( Tweet tweet : tweets ) {
			StreamUtils.updateTermsList(tweet.getTextTokens(), terms);
		}
		System.out.println( "Terms list size: " + terms.size() );
		
		long oldTime = System.currentTimeMillis();
		
		for ( Tweet tweet : tweets ) {
			Map<String, Double> tweetWeightsMap = 
			cosineCalc.getWeightsMap(tweet.getTokensList(), tweets.size(), terms);
			double similarity = 
					cosineCalc.getCosineSimilarity(tweetWeightsMap, tweetWeightsMap);
		}
		
		System.out.printf( "Looping took: %d sec.\n", (System.currentTimeMillis()-oldTime) / 1000 );
		
	}
}
