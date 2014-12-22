package de.uni.konstanz.tests;

import java.util.List;

import de.uni.konstanz.Parameters;
import de.uni.konstanz.dao.CSVDao;
import de.uni.konstanz.eventdescriptors.EventDescriptor;
import de.uni.konstanz.eventdescriptors.NeutralSentimentEventDescriptor;
import de.uni.konstanz.eventdescriptors.SentimentComputer;
import de.uni.konstanz.models.Tweet;

public class EventDescriptorsTests {
	
	public static void main(String[] args) {
		List<Tweet> tweets = CSVDao.read(Parameters.testFile, 15);
//		for (Tweet tweet : tweets ) {
//			System.out.println(tweet.getText());
//		}
		
		SentimentComputer sentComp = new SentimentComputer();
		
		
		System.out.println("Started...");
		long t0 = System.currentTimeMillis();
		sentComp.computeAndUpdateScores(tweets);
		System.out.println("SentComp: " + sentComp.getNeutralSentimentScore());
		EventDescriptor eventDescriptor =
				new NeutralSentimentEventDescriptor(tweets, 0.5);
		eventDescriptor.setScore(sentComp.getNeutralSentimentScore());
		long t1 = System.currentTimeMillis();
		System.out.println("Done.");
		
		System.out.println("Score: " + eventDescriptor.getScore());
		
		System.out.println((double) (t1-t0) / 1000 );
		
	}
	
	public static void testCompplexity() {
		int array[] = new int[100];
		int counter = 0;
		
		for ( int i = 0; i < array.length; i++ ) {
			for ( int j = i + 1; j < array.length; j++ ) {
				if ( array[i] == array[j] ) {
					counter++;
				}
			}
		}
		
	}

}





