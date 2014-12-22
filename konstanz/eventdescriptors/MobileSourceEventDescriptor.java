package de.uni.konstanz.eventdescriptors;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import de.uni.konstanz.models.Tweet;

public class MobileSourceEventDescriptor extends EventDescriptor {
	
	public static Set<String> sources_names = 
			new LinkedHashSet<String>();
	
	static {
		sources_names.add("Twitter for iPhone");
		sources_names.add("Twitter for Android");
		sources_names.add("Twitter for BlackBerry�");
	}

	public MobileSourceEventDescriptor(List<Tweet> tweets,
			double classificationThreshold) {
		super(tweets, classificationThreshold);
	}

	@Override
	public double computeScore(List<Tweet> tweets) {
		double score = 0;
		int counter = 0;
		
		for ( Tweet tweet : tweets ) {
			if ( sources_names.contains( tweet.getSource().getName() ) ) {
				//System.out.println(tweet.getSource().getName());
				counter++;
			}
		}
		
		score = (double) counter / tweets.size();
		
		return score;
	}
	
	public String toString() {
		return "MobileAuthority";
	}

}
