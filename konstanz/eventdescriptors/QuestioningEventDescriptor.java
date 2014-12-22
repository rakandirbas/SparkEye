package de.uni.konstanz.eventdescriptors;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import de.uni.konstanz.models.Tweet;

public class QuestioningEventDescriptor extends EventDescriptor {
	
	public static Set<String> question_words = 
			new LinkedHashSet<String>();
	
	static {
		question_words.add("what");
		question_words.add("when");
		question_words.add("where");
		question_words.add("which");
		question_words.add("who");
		question_words.add("whom");
		question_words.add("whose");
		question_words.add("why");
		question_words.add("how");
	}

	public QuestioningEventDescriptor(List<Tweet> tweets,
			double classificationThreshold) {
		super(tweets, classificationThreshold);
	}

	@Override
	public double computeScore(List<Tweet> tweets) {
		double score = 0;
		int counter = 0;
		
		for ( Tweet tweet : tweets ) {
			List<String> tokens = tweet.getSimpleTokens();
			for ( String token : tokens ) {
				if ( token.matches("\\?+") | question_words.contains(token) ) {
					counter++;
					//System.out.println(tweet.getText());
					break;
				}
			}
		}
		
		score = (double) counter / tweets.size();
		
		return score;
	}
	
	public String toString() {
		return "Questioning";
	}

}









