package de.uni.konstanz.eventdescriptors;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.uni.konstanz.models.Tweet;
import de.uni.konstanz.utils.MapUtils;

public class NearUsersByTimeZoneEventDescriptor extends EventDescriptor {

	public NearUsersByTimeZoneEventDescriptor(List<Tweet> tweets,
			double classificationThreshold) {
		super(tweets, classificationThreshold);
	}

	public double computeScore(List<Tweet> tweets) {
		double score = 0;
		int counter = 0;
		Map<String, Integer> timezones = new
				LinkedHashMap<String, Integer>();
		
		
		for ( Tweet tweet : tweets ) {
			if ( tweet.getUser().getTimeZone() != null ) {
				//System.out.println( tweet.getUser().getTimeZone() );
				if( !timezones.containsKey( tweet.getUser().getTimeZone() ) ) {
					timezones.put(tweet.getUser().getTimeZone(), 1);
				}
				else {
					int count = timezones.get(tweet.getUser().getTimeZone());
					count++;
					timezones.put(tweet.getUser().getTimeZone(), count);
				}
			}
		}
		
		timezones = MapUtils.sortByValue(timezones, true);
		
		if ( timezones.entrySet().size() > 0 ) {
			Map.Entry<String, Integer> entry = timezones.entrySet().iterator().next();
			counter = entry.getValue();
		}
		
		score = (double) counter / tweets.size();
		
		return score;
	}
	
	public String toString() {
		return "NearUsers";
	}

}
