package de.uni.konstanz.eventdescriptors;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import de.uni.konstanz.models.Tweet;

public class ObjectivityEventDescriptor extends EventDescriptor {
	
	public static Set<String> first_person_pronouns = 
			new LinkedHashSet<String>();
	static {
		first_person_pronouns.add("i");
		first_person_pronouns.add("me");
		first_person_pronouns.add("my");
		first_person_pronouns.add("mine");
		first_person_pronouns.add("myself");
		first_person_pronouns.add("we");
		first_person_pronouns.add("us");
		first_person_pronouns.add("our");
		first_person_pronouns.add("ours");
		first_person_pronouns.add("ourselfves");
	}

	public ObjectivityEventDescriptor(List<Tweet> tweets,
			double classificationThreshold) {
		super(tweets, classificationThreshold);
	}

	@Override
	public double computeScore(List<Tweet> tweets) {
		double score = 0;
		int counter = 0;
		
		for ( Tweet tweet : tweets ) {
			for ( String token : tweet.getSimpleTokens() ) {
				if ( first_person_pronouns.contains(token) ) {
					counter++;
					break;
				}
			}
		}
		
		score = (double) counter / tweets.size();
		
		score = 1 - score;
		
		return score;
	}
	
	public String toString() {
		return "Objectivity";
	}

}
