package de.uni.konstanz.eventdescriptors;

import java.util.List;

import uk.ac.wlv.sentistrength.SentiStrength;
import de.uni.konstanz.models.Tweet;

public class SentimentComputer  {
	
	public static String inis[] = new String[]{"sentidata",
			"resources/sentistrength/SentStrength_Data_Sept2011/",
			"trinary"};
	public static SentiStrength sentiStrength = new SentiStrength();
	
	static {
		sentiStrength.initialise(inis);
	}

	private double positiveSentScore = 0;
	private double negativeSentScore = 0;
	private double neutralSentScore = 0;
	
	public void computeAndUpdateScores(List<Tweet> tweets) {
		int pos_sen_counter = 0;
		int neg_sen_counter = 0;
		int neu_sen_counter = 0;
		
		for ( Tweet tweet : tweets ) {
			String result = sentiStrength.computeSentimentScores(tweet.getText());
			int classRes = Integer.parseInt( result.split(" ")[2] );
			
			if ( classRes == 1 )
				pos_sen_counter++;
			else if ( classRes == -1 )
				neg_sen_counter++;
			else 
				neu_sen_counter++;
		}
		
		positiveSentScore = (double) pos_sen_counter / tweets.size();
		negativeSentScore = (double) neg_sen_counter / tweets.size();
		neutralSentScore = (double) neu_sen_counter / tweets.size();
		
	}
	
	public double getPositiveSentimentScore() {
		return positiveSentScore;
	}
	
	public double getNegativeSentimentScore() {
		return negativeSentScore;
	}
	
	public double getNeutralSentimentScore() {
		return neutralSentScore;
	}
	
}













