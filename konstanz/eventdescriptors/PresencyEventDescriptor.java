package de.uni.konstanz.eventdescriptors;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import de.uni.konstanz.models.Tweet;

public class PresencyEventDescriptor extends EventDescriptor {
	
	public static Set<String> presence_words = 
			new LinkedHashSet<String>();

	static {
		presence_words.add( "am" );
		presence_words.add( "is" );
		presence_words.add( "are" );
		presence_words.add( "has" );
		presence_words.add( "have" );
		presence_words.add( "i'm" );
		presence_words.add( "he's" );
		presence_words.add( "she's" );
		presence_words.add( "it's" );
	}
	
	public PresencyEventDescriptor(List<Tweet> tweets,
			double classificationThreshold) {
		super(tweets, classificationThreshold);
	}

	@Override
	public double computeScore(List<Tweet> tweets) {
		double score = 0;
		int counter = 0;
		
		for ( Tweet tweet : tweets ) {
			for ( String token : tweet.getSimpleTokens() ) {
				if ( presence_words.contains(token) ) {
					counter++;
					break;
				}
			}
		}
		
		score = (double) counter / tweets.size();
		
		return score;
	}
	
	public String toString() {
		return "Presence";
	}

}















