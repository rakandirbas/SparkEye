package de.uni.konstanz.tweetfeatures;

import de.uni.konstanz.models.VoltTweet;

public class PopularUserTweetFeature extends TweetFeature {

	@Override
	/**
	 * Classifies the user as either popular or not
	 */
	public boolean classify(VoltTweet tweet) {
		double ratio = getScore(tweet);
		if ( ratio > 1 )
			return true;
		return false;
	}
	
	/**
	 * Returns the popularity ratio follower/friends
	 * @param tweet
	 * @return
	 */
	public static double getScore(VoltTweet tweet) {
		double popularityRatio = 0;
		int followers = tweet.getUserFollowersCount();
		int friends = tweet.getUserFriendsCount();
		popularityRatio = (double) followers/friends;//means his followers are more than his friends.
		return popularityRatio;
	}
	
}
