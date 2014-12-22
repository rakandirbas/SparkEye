package de.uni.konstanz.eventdescriptors;

import java.util.List;

import de.uni.konstanz.models.Tweet;

public class PositiveSentimentEventDescriptor extends EventDescriptor {
	

	public PositiveSentimentEventDescriptor(List<Tweet> tweets,
			double classificationThreshold) {
		super(tweets, classificationThreshold);
	}

	@Override
	public double computeScore(List<Tweet> tweets) {
		return -100000;
	}
	
	public String toString() {
		return "PositiveSentiment";
	}

}
