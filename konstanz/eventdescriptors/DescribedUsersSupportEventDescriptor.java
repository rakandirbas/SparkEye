package de.uni.konstanz.eventdescriptors;

import java.util.List;

import de.uni.konstanz.models.Tweet;

public class DescribedUsersSupportEventDescriptor extends EventDescriptor {

	public DescribedUsersSupportEventDescriptor(List<Tweet> tweets,
			double classificationThreshold) {
		super(tweets, classificationThreshold);
	}

	@Override
	public double computeScore(List<Tweet> tweets) {
		double score = 0;
		int counter = 0;
		
		for ( Tweet tweet : tweets ) {
			if( tweet.getUser().getDescription().length() > 0 ) {
				//System.out.println( tweet.getUser().getDescription() );
				counter++;
			}
		}
		
		score = (double) counter / tweets.size();
		
		return score;
	}
	
	public String toString() {
		return "DescribedUsers";
	}
}
