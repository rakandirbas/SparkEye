package de.uni.konstanz.eventdescriptors;

import java.util.List;

import de.uni.konstanz.models.Tweet;

public class GeoMetaDensityEventDescriptor extends EventDescriptor {

	public GeoMetaDensityEventDescriptor(List<Tweet> tweets,
			double classificationThreshold) {
		super(tweets, classificationThreshold);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double computeScore(List<Tweet> tweets) {
		double score = 0;
		int counter = 0;
		
		for ( Tweet tweet : tweets ) {
			if ( tweet.geoLocation != null ) {
				//System.out.println(tweet.getText());
				//System.out.println(tweet.getGeoLocation().getLatitude());
				counter++;
			}
		}
		
		score = (double) counter / tweets.size();
		
		return score;
	}
	
	public String toString() {
		return "Geo-metadata";
	}

}
