package de.uni.konstanz.eventdescriptors;

import java.util.List;

import de.uni.konstanz.models.Tweet;

public class RetweetDensityEventDescriptor extends EventDescriptor {

	public RetweetDensityEventDescriptor(List<Tweet> tweets,
			double classificationThreshold) {
		super(tweets, classificationThreshold);
	}

	@Override
	public double computeScore(List<Tweet> tweets) {
		double score = 0;
		int counter = 0;
		
		for ( Tweet tweet : tweets ) {
			if( tweet.getRetweetCount() > 0 ) {
				counter++;
			}
		}
		
		score = (double) counter / tweets.size();
		
		return score;
	}
	
	public String toString() {
		return "Retweet";
	}

}
