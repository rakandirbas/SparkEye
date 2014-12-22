package de.uni.konstanz.eventdescriptors;

import java.util.List;

import de.uni.konstanz.models.Tweet;

public class URLDensityEventDescriptor extends EventDescriptor {

	public URLDensityEventDescriptor(List<Tweet> tweets,
			double classificationThreshold) {
		super(tweets, classificationThreshold);
	}

	@Override
	public double computeScore(List<Tweet> tweets) {
		double score = 0;
		int counter = 0;
		
		for ( Tweet tweet : tweets ) {
			if ( tweet.getURLs() != null ) {
				if ( tweet.getURLs().length > 0 ) {
					counter++;
				}
			}
		}
		score = (double) counter / tweets.size();
		return score;
	}
	
	public String toString() {
		return "URLs";
	}

}
