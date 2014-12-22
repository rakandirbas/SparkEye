package de.uni.konstanz.tweetfeatures;

import de.uni.konstanz.models.VoltTweet;

public class WeirdCharsSaturationTweetFeature extends TweetFeature {

	/**
	 * Returns true if the number of weird characters is higher than
	 * half of the tweet's text size.
	 */
	public boolean classify(VoltTweet tweet) {
		double numbOfWeirdChars = getScore(tweet);
		
		if ( numbOfWeirdChars > (tweet.getText().length()/2) )
			return true;
		
		return false;
	}
	
	/**
	 * Returns the number of non-letter characters in the tweet
	 * @param tweet
	 * @return
	 */
	public static double getScore( VoltTweet tweet ) {
		double numbOfWeirdChars = 0;
		for ( Character c : tweet.getText().toCharArray() ) {
			if ( !Character.isLetter(c) ) {
				numbOfWeirdChars++;
			}
		}
		return numbOfWeirdChars;
	}
	
}
