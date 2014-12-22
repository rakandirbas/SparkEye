package de.uni.konstanz.tweetfeatures;

import java.util.LinkedHashSet;
import java.util.Set;

import de.uni.konstanz.models.VoltTweet;

public class ObjectivityTweetFeature extends TweetFeature {

	public static Set<String> first_person_pronouns = 
			new LinkedHashSet<String>();
	static {
		first_person_pronouns.add("i");
		first_person_pronouns.add("me");
		first_person_pronouns.add("my");
		first_person_pronouns.add("mine");
		first_person_pronouns.add("myself");
		first_person_pronouns.add("we");
		first_person_pronouns.add("us");
		first_person_pronouns.add("our");
		first_person_pronouns.add("ours");
		first_person_pronouns.add("ourselfves");
	}

	@Override
	/**
	 * Classifies the tweet as either objective or not. (i.e.
	 * if it has NO personal pronouns then it's objective).
	 */
	public boolean classify(VoltTweet tweet) {
		
		for ( String token : tweet.getTokens() ) {
			if ( first_person_pronouns.contains(token) ) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Returns the number of personal pronouns in the tweet.
	 * @param tweet
	 * @return
	 */
	public static double getScore(VoltTweet tweet) {
		double counter = 0;
		for ( String token : tweet.getTokens() ) {
			if ( first_person_pronouns.contains(token) ) {
				counter++;
			}
		}
		return counter;
	}


}
