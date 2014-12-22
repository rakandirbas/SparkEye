package de.uni.konstanz.tests;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import de.uni.konstanz.analysis.FastLangDetector;
import de.uni.konstanz.analysis.Twokenize;
import de.uni.konstanz.dao.CSVDao;
import de.uni.konstanz.models.Tweet;


public class FastLangDetecTest {

	public static void main(String[] args) {
		FastLangDetector detector = new FastLangDetector();
		System.out.println( "Getting list of Tweets ... " );
		File csvFilePath = new 
				File("/Users/rockyrock/Desktop/testCSV/2013_04_19_22.csv");
		List<Tweet> tweets = CSVDao.read(csvFilePath, 50000);
		List<Tweet> filteredList = new LinkedList<Tweet>();
		System.out.println( "Finished getting the list." );
		System.out.println( "List size: " + tweets.size() );
		System.out.println( "Testing detection time ..." );
		long testTime = System.currentTimeMillis();
		for ( Tweet t : tweets ) {
			boolean isEnglish = 
					detector.isEnglishSentence(Twokenize.tokenize(
							t.getText()).toArray(new String[0])
								, t.getText());
			if ( isEnglish ) {
				filteredList.add(t);
			}
			
		}
		System.out.println( "Filtered size: " + filteredList.size() );
		System.out.printf( "Detection took: %d sec.\n", (System.currentTimeMillis()-testTime) / 1000 );

	}

}
