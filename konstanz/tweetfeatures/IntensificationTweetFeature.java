package de.uni.konstanz.tweetfeatures;

import java.io.File;

import de.uni.konstanz.models.VoltTweet;
import de.uni.konstanz.utils.VoltLookUpTable;

public class IntensificationTweetFeature extends TweetFeature {

	public static VoltLookUpTable lookUpTable = 
			new VoltLookUpTable(new File("resources/intensifiers.txt" ));

	@Override
	/**
	 * Checks if the tweet has an intensification word
	 */
	public boolean classify(VoltTweet tweet) {
		for ( String token : tweet.getTokens() ) {
			if ( lookUpTable.contains(token) ) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the number of intensified tokens in the tweet
	 * @param tweet
	 * @return
	 */
	public static double getScore(VoltTweet tweet) {
		double counter = 0;
		for ( String token : tweet.getTokens() ) {
			if ( lookUpTable.contains(token) ) {
				counter++;
			}
		}
		return counter;
	}

}
