package de.uni.konstanz.eventdescriptors;

import java.util.List;

import de.uni.konstanz.models.Tweet;

public class NegativeSentimentEventDescriptor extends EventDescriptor {
	
	public NegativeSentimentEventDescriptor(List<Tweet> tweets,
			double classificationThreshold) {
		super(tweets, classificationThreshold);
	}

	@Override
	public double computeScore(List<Tweet> tweets) {
		return -1000000000;
	}
	
	public String toString() {
		return "NegativeSentiment";
	}
	

}
