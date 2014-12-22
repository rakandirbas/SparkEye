package de.uni.konstanz.eventdescriptors;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import de.uni.konstanz.models.Tweet;

public class PastEventDescriptor extends EventDescriptor {
	
	public static Set<String> past_words = 
			new LinkedHashSet<String>();
	
	static {
		past_words.add("was");
		past_words.add("were");
		past_words.add("had");
		//past_words.add("did");
	}

	public PastEventDescriptor(List<Tweet> tweets,
			double classificationThreshold) {
		super(tweets, classificationThreshold);
	}

	@Override
	public double computeScore(List<Tweet> tweets) {
		double score = 0;
		int counter = 0;
		
		for ( Tweet tweet : tweets ) {
			for ( String token : tweet.getSimpleTokens() ) {
				if ( past_words.contains(token) ) {
					counter++;
					break;
				}
			}
		}
		
		score = (double) counter / tweets.size();
		
		return score;
	}
	
	public String toString() {
		return "Past";
	}

}
