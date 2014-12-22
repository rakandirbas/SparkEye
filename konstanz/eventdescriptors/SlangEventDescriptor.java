package de.uni.konstanz.eventdescriptors;

import java.io.File;
import java.util.List;

import de.uni.konstanz.models.Tweet;
import de.uni.konstanz.utils.LookupTable;

public class SlangEventDescriptor extends EventDescriptor{
	
	public static final File dictionaryFile = 
			new File( "resources/slangs.txt" );
	
	public static final LookupTable lookupSlangsTable =
			new LookupTable(dictionaryFile);

	public SlangEventDescriptor(List<Tweet> tweets,
			double classificationThreshold) {
		super(tweets, classificationThreshold);
	}

	@Override
	public double computeScore(List<Tweet> tweets) {
		double score = 0;
		int counter = 0;
		
		for ( Tweet tweet : tweets ) {
			if ( lookupSlangsTable.getTermsFromText(tweet.getText()).size() > 0 ) {
				
//				for (String s : lookupSlangsTable.getTermsFromText(tweet.getText())) {
//					System.out.println(s);
//				}
				
				//System.out.println(tweet.getText());
				counter++;
			}
			
		}
		
		score = (double) counter / tweets.size();
		
		return score;
	}
	
	public String toString() {
		return "Slanginess";
	}

}
