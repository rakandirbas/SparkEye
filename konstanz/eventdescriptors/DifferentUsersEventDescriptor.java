package de.uni.konstanz.eventdescriptors;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import de.uni.konstanz.models.Tweet;

public class DifferentUsersEventDescriptor extends EventDescriptor {

	public DifferentUsersEventDescriptor(List<Tweet> tweets,
			double classificationThreshold) {
		super(tweets, classificationThreshold);
	}

	@Override
	public double computeScore(List<Tweet> tweets) {
		double score = 0;
		
		Set<String> users = new LinkedHashSet<String>();
		
		for ( Tweet tweet : tweets ) {
			users.add(tweet.getUser().getScreenName());
		}
		
		score = (double) users.size() / tweets.size();
		
		return score;
	}
	
	public String toString() {
		return "DifferentUsers";
	}

}













