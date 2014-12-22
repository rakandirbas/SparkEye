package de.uni.konstanz.eventdescriptors;

import java.util.List;

import de.uni.konstanz.models.Tweet;

public class PopularUsersSupportEventDescriptor extends EventDescriptor {

	public PopularUsersSupportEventDescriptor(List<Tweet> tweets,
			double classificationThreshold) {
		super(tweets, classificationThreshold);
	}

	@Override
	public double computeScore(List<Tweet> tweets) {
		double score = 0;
		int counter = 0;
		
		for ( Tweet tweet : tweets ) {
			int followers = tweet.getUser().getFollowersCount();
			int friends = tweet.getUser().getFriendsCount();
			double ratio = (double) followers/friends;//means his followers are more than his friends.
			if ( ratio > 1 ) {
				//System.out.println( tweet.getUser().getScreenName()+ ", followers: " + followers + ", Friends: " + friends);
				counter++;
			}
		}
		
		score = (double) counter / tweets.size();
		
		return score;
	}
	
	public String toString() {
		return "PopularUsers";
	}

}
