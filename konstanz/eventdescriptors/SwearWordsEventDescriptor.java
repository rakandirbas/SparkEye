package de.uni.konstanz.eventdescriptors;

import java.io.File;
import java.util.List;

import de.uni.konstanz.models.Tweet;
import de.uni.konstanz.utils.LookupTable;

public class SwearWordsEventDescriptor extends EventDescriptor {
	
	public static final File dictionaryFile = 
			new File( "resources/badwords.txt" );
	
	public static final LookupTable lookupSwearingTable =
			new LookupTable(SwearWordsEventDescriptor.dictionaryFile);
	
	public SwearWordsEventDescriptor(List<Tweet> tweets,
			double classificationThreshold) {
		super(tweets, classificationThreshold);
	}

	@Override
	public double computeScore(List<Tweet> tweets) {
		double score = 0;
		int counter = 0;
		
		for ( Tweet tweet : tweets ) {
			if ( lookupSwearingTable.getTermsFromText(tweet.getText()).size() > 0 ) {
				//System.out.println(tweet.getText());
				counter++;
			}
			
		}
		
		score = (double) counter / tweets.size();
		
		return score;
	}
	
	public String toString() {
		return "Swearing";
	}
	
}
