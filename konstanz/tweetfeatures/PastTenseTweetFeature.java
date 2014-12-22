package de.uni.konstanz.tweetfeatures;

import java.util.LinkedHashSet;
import java.util.Set;

import de.uni.konstanz.models.VoltTweet;

public class PastTenseTweetFeature extends TweetFeature {
	
	public static Set<String> past_words = 
			new LinkedHashSet<String>();
	
	static {
		past_words.add("was");
		past_words.add("were");
		past_words.add("had");
		//past_words.add("did");
	}

	@Override
	/**
	 * Classifies the tweet to have a past tense or not
	 */
	public boolean classify(VoltTweet tweet) {
		for ( String token : tweet.getTokens() ) {
			if ( past_words.contains(token) ) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns the number of past tenses words in the tweet
	 * @param tweet
	 * @return
	 */
	public static double getScore(VoltTweet tweet) {
		double counter = 0;
		for ( String token : tweet.getTokens() ) {
			if ( past_words.contains(token) ) {
				counter++;
			}
		}
		return counter;
	}
	
}
