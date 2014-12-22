package de.uni.konstanz.server;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.prefs.Preferences;

import de.uni.konstanz.Parameters;
import de.uni.konstanz.models.Tweet;

public class Starter {
	public static void main(String[] args) {
		final BlockingQueue<Tweet> queue = new LinkedBlockingQueue<>(100000);
		Preferences prefs = Parameters.getParameters();
		
		File csvFileLocation = new 
				File("/Users/rockyrock/Desktop/testCSV/2013_04_19_22_filtered_sorted.csv");
		
		ExecutorService executor = Executors.newCachedThreadPool();

		executor.execute( new TweetsProducer(queue, csvFileLocation) );
		executor.execute( new TweetsConsumer( queue, csvFileLocation, prefs ) );
		executor.shutdown();
				
	}
}
