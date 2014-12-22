package de.uni.konstanz.tests;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.uni.konstanz.dao.CSVDao;
import de.uni.konstanz.models.Tweet;

public class TweetsOrderTest {
	
	public static void main(String[] args) throws InterruptedException {
		//secondTest();
		thirdTest();
	}
	
	public static void thirdTest() {
		File csvFilePath = new 
				File("/Users/rockyrock/Desktop/testCSV/part_sorted.csv");
		System.out.println("Reading tweets ....");
		List<Tweet> tweets = CSVDao.read(csvFilePath);
		System.out.println("Done reading tweets");
		for( Tweet t : tweets ) {
			System.out.println( t.getCreatedAt().toString() );
		}
	}
	
	public static void secondTest() {
		File csvFilePath = new 
				File("/Users/rockyrock/Desktop/testCSV/part.csv");
		System.out.println("Reading tweets ....");
		List<Tweet> tweets = CSVDao.read(csvFilePath, 500);
		List<Tweet> sortedTweets = new LinkedList<Tweet>();
		sortedTweets.addAll(tweets);
		System.out.println("Done reading tweets");
		
		
		System.out.println("Sorting tweets...");
		long t0 = System.currentTimeMillis();
		Collections.sort(sortedTweets);
		long t1 = System.currentTimeMillis();
		long elapsed = t1 - t0;
		elapsed /= 1000;
		System.out.println( "Sorting time: " + elapsed );
		
		int total = 0;
		for( int i = 0; i < tweets.size(); i++ ) {
			Tweet t = tweets.get(i);
			Tweet sortedT = sortedTweets.get(i);
			
			if ( t.getId() != sortedT.getId() ) {
				System.out.println( i + ": "+ "Two tweets are different." );
				total++;
			}
		}
		System.out.println( "Total different tweets: " + total  );
		
		System.out.println( "Printing the dates of unsorted:" );
		for ( int i = 0; i < tweets.size(); i++ ) {
			Tweet t = tweets.get(i);
			System.out.println(i + "# " + t.getCreatedAt().toString() + "\t" + t.getText());
		}
		
		System.out.println("\n");
		
		System.out.println( "Printing the dates of SORTED:" );
		for ( int i = 0; i < tweets.size(); i++ ) {
			Tweet t = sortedTweets.get(i);
			System.out.println(i + "# " + t.getCreatedAt().toString() + "\t" + t.getText());
		}
	}
	
	public static void firstTest() {
		File csvFilePath = new 
				File("/Users/rockyrock/Desktop/testCSV/part.csv");
		System.out.println("Reading tweets ....");
		List<Tweet> tweets = CSVDao.read(csvFilePath, 500);
		List<Tweet> sortedTweets = new LinkedList<Tweet>();
		sortedTweets.addAll(tweets);
		System.out.println("Done reading tweets");
		
		
		System.out.println("Sorting tweets...");
		long t0 = System.currentTimeMillis();
		Collections.sort(sortedTweets);
		long t1 = System.currentTimeMillis();
		long elapsed = t1 - t0;
		elapsed /= 1000;
		System.out.println( "Sorting time: " + elapsed );
		
		int total = 0;
		for( int i = 0; i < tweets.size(); i++ ) {
			Tweet t = tweets.get(i);
			Tweet sortedT = sortedTweets.get(i);
			
			if ( t.getId() != sortedT.getId() ) {
				System.out.println( i + ": "+ "Two tweets are different." );
				total++;
			}
		}
		System.out.println( "Total different tweets: " + total  );
		
		System.out.println( "Printing the dates of unsorted:" );
		for ( int i = 470; i < tweets.size(); i++ ) {
			Tweet t = tweets.get(i);
			System.out.println(i + "# " + t.getCreatedAt().toString() + "\t" + t.getText());
		}
		
		System.out.println("\n");
		
		System.out.println( "Printing the dates of SORTED:" );
		for ( int i = 470; i < tweets.size(); i++ ) {
			Tweet t = sortedTweets.get(i);
			System.out.println(i + "# " + t.getCreatedAt().toString() + "\t" + t.getText());
		}
		
	}

}
