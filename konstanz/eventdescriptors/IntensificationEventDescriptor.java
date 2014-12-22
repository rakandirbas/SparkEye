package de.uni.konstanz.eventdescriptors;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import de.uni.konstanz.analysis.Preprocessor;
import de.uni.konstanz.models.Token;
import de.uni.konstanz.models.Tweet;
import de.uni.konstanz.utils.AnalyzerUtils;
import de.uni.konstanz.utils.FileUtils;

public class IntensificationEventDescriptor extends EventDescriptor {
	
	private static Logger logger = Logger.getLogger(IntensificationEventDescriptor.class);
	
	public static final File dictionaryFile = 
			new File( "resources/intensifiers.txt" );
	public static final Set<String> intensifiers = 
			new LinkedHashSet<String>();
	
	static {
		populateSetWithIntensifiers();
	}
	
	public IntensificationEventDescriptor(List<Tweet> tweets,
			double classificationThreshold) {
		super(tweets, classificationThreshold);
	}

	@Override
	public double computeScore(List<Tweet> tweets) {
		double score = 0;
		int counter = 0;
		
		for ( Tweet tweet : tweets ) {
			List<Token> tokensList = Preprocessor.getClassicTokensList(tweet.getText());
			
			for ( Token token : tokensList ) {
				if ( intensifiers.contains(token.getToken()) ) {
					//System.out.println(tweet.getText());
					counter++;
					break;
				}
			}
			
		}
		
		score = (double) counter / tweets.size();
		
		return score;
	}
	
	public static void populateSetWithIntensifiers() {
		BufferedReader reader = FileUtils.getFileReader(dictionaryFile);
		String line = "";
		try {
			while((line = reader.readLine()) != null) {
				String word  = line;
				word = word.toLowerCase();
				word = AnalyzerUtils.filterKeyword(Preprocessor.stemmingAnlyzer, word);
				//System.out.println(word);
				intensifiers.add(word);
			}
		}
		catch (IOException e) {
			logger.error("Error while populating intensifers table from dic.", e);
			e.printStackTrace();
		}
		
		try {
			reader.close();
		} catch (IOException e) {
			logger.error("Error while closing intensifers table dic file.", e);
			e.printStackTrace();
		}
	}
	
	public String toString() {
		return "Intensification";
	}

}
