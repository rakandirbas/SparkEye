package de.uni.konstanz.eventdescriptors;

import java.util.List;

import de.uni.konstanz.locationextractors.NERBasedLocationExtractor;
import de.uni.konstanz.models.Tweet;

public class TextualLocationSupportEventDescriptor extends EventDescriptor {

	public TextualLocationSupportEventDescriptor(List<Tweet> tweets,
			double classificationThreshold) {
		super(tweets, classificationThreshold);
	}

	@Override
	public double computeScore(List<Tweet> tweets) {
		double score = 0;
		int counter = 0;
		
		for ( Tweet tweet : tweets ) {
			if( NERBasedLocationExtractor.extractLocations(tweet.getText().replace("#", " ")).size() > 0 ) {
				//System.out.println(tweet.getText());
				counter++;
			}
		}
		
		score = (double) counter / tweets.size();
		
		return score;
	}
	
	public String toString() {
		return "LocationInText";
	}

}








