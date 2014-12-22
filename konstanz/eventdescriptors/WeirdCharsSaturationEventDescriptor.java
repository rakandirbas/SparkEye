package de.uni.konstanz.eventdescriptors;

import java.util.List;

import de.uni.konstanz.models.Tweet;

public class WeirdCharsSaturationEventDescriptor extends EventDescriptor {

	public WeirdCharsSaturationEventDescriptor(List<Tweet> tweets,
			double classificationThreshold) {
		super(tweets, classificationThreshold);
	}

	@Override
	public double computeScore(List<Tweet> tweets) {
		double score = 0;
		int counter = 0;
		
		for ( Tweet tweet : tweets ) {
			int innerCounter = 0;
			List<String> tokens = tweet.getSimpleTokens();
			for ( String token : tokens ) {
				if ( token.matches("\\P{L}+") ) {
					//System.out.println(token);
					innerCounter++;
				}
			}
			//System.out.println("#Tokens: " + tokens.size());
			//System.out.println("InnerScore: " + (double) innerCounter / tokens.size() );
			score += (double) innerCounter / tokens.size();
		}
		
		score = (double) score / tweets.size();
		
		return score;
	}
	
	public String toString() {
		return "WeirdChars";
	}

}
