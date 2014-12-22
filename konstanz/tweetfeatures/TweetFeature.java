package de.uni.konstanz.tweetfeatures;

import java.util.LinkedHashSet;
import java.util.Set;

import de.uni.konstanz.models.VoltTweet;

public abstract class TweetFeature {
	public static Set<String> features;
	public static String positive_sentiment = "positive_sentiment";
	public static String negatuve_sentiment = "negatuve_sentiment";
	public static String neutral_sentiment = "neutral_sentiment";
	public static String conversational = "conversational";
	public static String url = "url";
	public static String hashtag = "hashtag";
	public static String favorited = "favorited";
	public static String objectivity = "objectivity";
	public static String present_tense = "present_tense";
	public static String past_tense = "past_tense";
	public static String named_entity = "named_entity";
	public static String retweet = "retweet";
	public static String mobile = "mobile";
	public static String weird_chars = "weird_chars";
	public static String question = "question";
	public static String emoticons = "emoticons";
	public static String swearing = "swearing";
	public static String slanginess = "slanginess";
	public static String intensification = "intensification";
	public static String geo_location = "geo_location";
	public static String lengthness = "lengthness";
	public static String popular_user = "popular_user";
	public static String active_user = "active_user";
	public static String described_user = "described_user";
	
	static {
		features = new LinkedHashSet<String>();
		features.add(positive_sentiment);
		features.add(negatuve_sentiment);
		features.add(neutral_sentiment);
		features.add(conversational);
		features.add(url);
		features.add(hashtag);
		features.add(favorited);
		features.add(objectivity);
		features.add(present_tense);
		features.add(past_tense);
		features.add(named_entity);
		features.add(retweet);
		features.add(mobile);
		features.add(weird_chars);
		features.add(question);
		features.add(emoticons);
		features.add(swearing);
		features.add(slanginess);
		features.add(intensification);
		features.add(geo_location);
		features.add(lengthness);
		features.add(popular_user);
		features.add(active_user);
		features.add(described_user);
	}
	
	public abstract boolean classify(VoltTweet tweet);
}
