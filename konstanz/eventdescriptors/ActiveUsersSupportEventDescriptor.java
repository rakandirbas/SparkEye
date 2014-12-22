package de.uni.konstanz.eventdescriptors;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;

import de.uni.konstanz.models.Tweet;

public class ActiveUsersSupportEventDescriptor extends EventDescriptor {

	public ActiveUsersSupportEventDescriptor(List<Tweet> tweets,
			double classificationThreshold) {
		super(tweets, classificationThreshold);
	}

	@Override
	public double computeScore(List<Tweet> tweets) {
		double score = 0;
		int counter = 0;
		
		for ( Tweet tweet : tweets ) {
			int statsuses = tweet.getUser().getTweetsCount();
			DateTime currentDate = new DateTime(System.currentTimeMillis());
			DateTime regDate = new DateTime(tweet.getUser().getRegistrationDate().getTime());
			int daysSinceReg = Days.daysBetween(regDate, currentDate).getDays();
			double ratio = (double) statsuses / daysSinceReg;
			
			if ( ratio > 1 ) {
				//System.out.println(statsuses);
				//System.out.println(regDate);
				counter++;
			}
				
		}
		
		score = (double) counter / tweets.size();
		
		return score;
	}
	
	public String toString() {
		return "ActiveUsers";
	}

}













