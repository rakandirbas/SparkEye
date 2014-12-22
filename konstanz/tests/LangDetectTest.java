package de.uni.konstanz.tests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;

import de.uni.konstanz.analysis.Preprocessor;
import de.uni.konstanz.dao.CSVDao;
import de.uni.konstanz.models.Tweet;
import de.uni.konstanz.utils.FileUtils;

public class LangDetectTest {
	
	public static void main(String[] args) {
		try {
			System.out.println( "Getting list of Tweets ... " );
			File csvFilePath = new 
					File("/Users/rockyrock/Desktop/testCSV/2013_04_19_22.csv");
			List<Tweet> tweets = CSVDao.read(csvFilePath, 50000);
			List<Tweet> filteredList = new LinkedList<Tweet>();
			System.out.println( "Finished getting the list." );
			System.out.println( "List size: " + tweets.size() );
			Detector langDetector = null;
			System.out.println( "Testing detection time ..." );
			long testTime = System.currentTimeMillis();
			for ( Tweet t : tweets ) {
				langDetector = DetectorFactory.create();
				if (t.getTokenedText().isEmpty() )
					continue;
				langDetector.append(t.getTokenedText());
				if ( langDetector.detect().equals("en") ) {
					filteredList.add(t);
				}
			}
			System.out.println( "Filtered size: " + filteredList.size() );
			System.out.printf( "Detection took: %d sec.\n", (System.currentTimeMillis()-testTime) / 1000 );
			
		} catch (LangDetectException e) {
			e.printStackTrace();
		}
	}
	
	public static List<Tweet> testList( List<Tweet> tweets ) {
		List<Tweet> filteredList = new ArrayList<Tweet>();
		try {
			
			System.out.println( "Filtering the list ..." );
			Preprocessor.preprocessor.filter(tweets);
			System.out.println( "Done filtering the list." );
			Detector langDetector = DetectorFactory.create();
			BufferedWriter writer = FileUtils.getFileWriter(
					new File("/Users/rockyrock/Desktop/FilteredTweets.txt"));
			for ( Tweet t : tweets ) {
				langDetector = DetectorFactory.create();
				if (t.getTokenedText().isEmpty() )
					continue;
				langDetector.append(t.getTokenedText());
				if ( langDetector.detect().equals("en") ) {
//					System.out.println( "Text: " + t.getText() );
//					System.out.println( "Tokens: " + t.getTokenedText() );
//					System.out.println( "\n\n" );
					filteredList.add(t);
					writer.write( "Tweet_ID: " + t.getId() + "\n");
					writer.write( "Text: " + t.getText() + "\n");
					writer.write( "Tokens: " + t.getTokenedText() );
					writer.write( "\n\n" );
				}
			}
			
		} catch (LangDetectException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filteredList;
	}

}
