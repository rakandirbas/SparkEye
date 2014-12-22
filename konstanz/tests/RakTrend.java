package de.uni.konstanz.tests;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import de.uni.konstanz.models.VoltTweet;
import de.uni.konstanz.stream.StreamListener;
import de.uni.konstanz.stream.VoltCSVStream;


public class RakTrend implements StreamListener {
	
	VoltCSVStream stream;
	public Map<String, Integer> keywordsWindow1;
	public Map<String, Integer> keywordsWindow2;
	int tweetsCounter = 0;
	int windowSize = 100000;
	int threshold = 20;
	
	public RakTrend() throws Exception {
		String f1 = "/Users/rockyrock/Desktop/testCSV/2013_04_15_22_filtered_sorted.csv";
		String f2 = "/Volumes/Passport/Twitter_Data/VizShotsTests/arora/2012_07_20_08.csv";
		stream = new VoltCSVStream(new File(f1), this);
		keywordsWindow1 = new
				LinkedHashMap<String, Integer>();
		keywordsWindow2 = new
				LinkedHashMap<String, Integer>();
	}

	@Override
	public void onReceiving(VoltTweet tweet) {
		
		if ( tweetsCounter <= windowSize ) {
			for( String tag : tweet.getHashtags() ) {
				tag = tag.toLowerCase();
				if ( keywordsWindow2.containsKey(tag) ) {
					Integer freq = keywordsWindow2.get(tag);
					freq++;
					keywordsWindow2.put(tag, freq);
				}
				else {
					Integer freq = 1;
					keywordsWindow2.put(tag, freq);
				}
			}
			tweetsCounter++;
		}
		else {
			System.err.println("*******New window*****");
			tweetsCounter = 0;
			for ( String term : keywordsWindow2.keySet() ) {
				if ( keywordsWindow1.containsKey(term) ) {
					int currentFreq = keywordsWindow2.get(term);
					int oldFreq = keywordsWindow1.get(term);
					int freqDiff = currentFreq - oldFreq;
					//System.out.printf("Term = %s, Freqs: %d + %d\n", term, currentFreq, oldFreq);
					if ( freqDiff > threshold ) {
						System.err.printf("Term = %s, Freqs: %d + %d\n", term, currentFreq, oldFreq);
					}
				}
			}
			
			keywordsWindow1 = keywordsWindow2;
			keywordsWindow2 = new
					LinkedHashMap<String, Integer>();
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		RakTrend stream = new RakTrend();
		stream.stream.start();
	}

}
