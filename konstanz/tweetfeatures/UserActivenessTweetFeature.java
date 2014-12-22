package de.uni.konstanz.tweetfeatures;

import org.joda.time.DateTime;
import org.joda.time.Days;

import de.uni.konstanz.models.VoltTweet;

public class UserActivenessTweetFeature extends TweetFeature {

	@Override
	/**
	 * Classifies the user as either active user or not
	 */
	public boolean classify(VoltTweet tweet) {
		int tweetsCount = tweet.getUserNumbTweets();
		double daysSinceReg = getScore(tweet);
		double ratio = (double) tweetsCount / daysSinceReg;
		if ( ratio > 1 )
			return true;
		return false;
	}
	
	/**
	 * Returns the days of registration since the tweet was sent.
	 * @param tweet
	 * @return
	 */
	public static double getScore(VoltTweet tweet) {
		double daysSinceReg = 0;
		DateTime currentDate = new DateTime(tweet.getCreatedAt().getTime());
		DateTime regDate = new DateTime(tweet.getUserRegistrationDate().getTime());
		daysSinceReg = Days.daysBetween(regDate, currentDate).getDays();
		return daysSinceReg;
	}
	
	public static String getName() {
		return TweetFeature.active_user;
	}

}
