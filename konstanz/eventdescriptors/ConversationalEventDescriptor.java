package de.uni.konstanz.eventdescriptors;

import java.util.List;

import de.uni.konstanz.models.Tweet;

/**
 * Check if over x% of the tweets have user mentions.
 * @author rockyrock
 *
 */

public class ConversationalEventDescriptor extends EventDescriptor {

	public ConversationalEventDescriptor(List<Tweet> tweets,
			double classificationThreshold) {
		super(tweets, classificationThreshold);
	}

	@Override
	public double computeScore(List<Tweet> tweets) {
		int counter = 0;
		for ( Tweet tweet : tweets ) {
			if ( tweet.getUserMentionEntites() != null ) {
				if ( tweet.getUserMentionEntites().length > 0 ) {
					counter++;
				}
			}
		}
		double score = (double) counter / tweets.size();
		
		return score;
	}
	
	public String toString() {
		return "Conversational";
	}

}
