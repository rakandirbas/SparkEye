package de.uni.konstanz.eventdescriptors;

import java.util.List;

import de.uni.konstanz.models.Tweet;

public class PopularityEventDescriptor extends EventDescriptor {

	public PopularityEventDescriptor(List<Tweet> tweets,
			double classificationThreshold) {
		super(tweets, classificationThreshold);
	}

	@Override
	public double computeScore(List<Tweet> tweets) {
		return -10000000;
	}
	
	public String toString() {
		return "Popularity";
	}

}
