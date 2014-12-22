package de.uni.konstanz.tweetfeatures;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import de.uni.konstanz.classifiers.SentimentClassifier;
import de.uni.konstanz.models.VoltTweet;

public class NumericTweetFeatures {
	public static Set<String> numericFeaturesNames;
	public static final String positive_sentiment = "positive_sentiment";
	public static final String negative_sentiment = "negative_sentiment";
	public static final String numb_of_mentions = "numb_of_mentions";
	public static final String numb_of_urls = "numb_of_urls";
	public static final String numb_of_hashtags = "numb_of_hashtags";
	public static final String is_tweet_favorited = "is_tweet_favorited";
	public static final String numb_of_personal_pronouns = "numb_of_personal_pronouns";
	public static final String numb_of_present_tenses = "numb_of_present_tenses";
	public static final String numb_of_past_tenses = "numb_of_past_tenses";
	public static final String numb_of_named_entites = "numb_of_named_entites";
	public static final String numb_of_retweets = "numb_of_retweets";
	public static final String sent_from_mobile = "sent_from_mobile";
	public static final String sent_from_web = "sent_from_web";
	public static final String numb_of_weird_chars = "numb_of_weird_chars";
	public static final String numb_of_questions = "numb_of_questions";
	public static final String numb_of_emoticons = "numb_of_emoticons";
	public static final String numb_of_swearing_words = "numb_of_swearing_word";
	public static final String numb_of_slang_words = "numb_of_slang_words";
	public static final String numb_of_intensifiers = "numb_of_intensifiers";
	public static final String has_geolocation = "has_geolocation";
	public static final String tweet_length = "tweet_length";
	public static final String userFollowersCount = "userFollowersCount";
	public static final String userFriendsCount = "userFriendsCount";
	public static final String userRegistrationDays = "userRegistrationDays";
	public static final String user_numb_of_tweets = "user_numb_of_tweets";
	public static final String numb_of_user_description_chars = "numb_of_user_description_chars";
	public static final String user_listed_count = "user_listed_count";
	
	static {
		numericFeaturesNames = new LinkedHashSet<String>();
		numericFeaturesNames.add(positive_sentiment);
		numericFeaturesNames.add(negative_sentiment);
		numericFeaturesNames.add(numb_of_mentions);
		numericFeaturesNames.add(numb_of_urls);
		numericFeaturesNames.add(numb_of_hashtags);
		numericFeaturesNames.add(is_tweet_favorited);
		numericFeaturesNames.add(numb_of_personal_pronouns);
		numericFeaturesNames.add(numb_of_present_tenses);
		numericFeaturesNames.add(numb_of_past_tenses);
		//numericFeaturesNames.add(numb_of_named_entites);
		numericFeaturesNames.add(numb_of_retweets);
		numericFeaturesNames.add(sent_from_mobile);
		numericFeaturesNames.add(sent_from_web);
		numericFeaturesNames.add(numb_of_weird_chars);
		numericFeaturesNames.add(numb_of_questions);
		numericFeaturesNames.add(numb_of_emoticons);
		numericFeaturesNames.add(numb_of_swearing_words);
		numericFeaturesNames.add(numb_of_slang_words);
		numericFeaturesNames.add(numb_of_intensifiers);
		numericFeaturesNames.add(has_geolocation);
		numericFeaturesNames.add(tweet_length);
		numericFeaturesNames.add(userFollowersCount);
		numericFeaturesNames.add(userFriendsCount);
		numericFeaturesNames.add(userRegistrationDays);
		numericFeaturesNames.add(user_numb_of_tweets);
		numericFeaturesNames.add(numb_of_user_description_chars);
		numericFeaturesNames.add(user_listed_count);
		
	}
	
	
	public static Map<String, Double> makeFeatures(VoltTweet tweet) {
		Map<String, Double> features = new LinkedHashMap<String, Double>();
		
		double posScore = SentimentClassifier.getStrengths(tweet.getText())[0];
		features.put(positive_sentiment, posScore);
		double negScore = SentimentClassifier.getStrengths(tweet.getText())[1];
		features.put(negative_sentiment, negScore);
		
		features.put(numb_of_mentions, (double) tweet.getUserMentions().length);
		features.put(numb_of_urls, (double) tweet.getUrls().length);
		features.put(numb_of_hashtags, (double) tweet.getHashtags().length);
		features.put(is_tweet_favorited, tweet.isFavorited()?1.0:0);
		features.put(numb_of_personal_pronouns, ObjectivityTweetFeature.getScore(tweet));
		features.put(numb_of_present_tenses, PresentTenseTweetFeature.getScore(tweet));
		features.put(numb_of_past_tenses, PastTenseTweetFeature.getScore(tweet));
//		features.put(numb_of_named_entites, (double) 
//				NamedEntityClassifier.getNamedEntites(tweet.getText().replace("#", " ")).size());
		features.put(numb_of_retweets, (double) tweet.getRetweetCount());
		features.put(sent_from_mobile, tweet.isSentFromMobile()?1.0:0);
		features.put(sent_from_web, tweet.isSentFromWeb()?1.0:0);
		features.put(numb_of_weird_chars, (double)WeirdCharsSaturationTweetFeature.getScore(tweet));
		
		int numbOfQuestions = 0;
		for ( char c : tweet.getText().toCharArray() ) {
			if ( c == '?' )
				numbOfQuestions++;
		}
		features.put(numb_of_questions, (double) numbOfQuestions);
		features.put(numb_of_emoticons,  EmoticonsTweetFeature.getScore(tweet));
		features.put(numb_of_swearing_words, SwearingTweetFeature.getScore(tweet));
		features.put(numb_of_slang_words, SlanginessTweetFeature.getScore(tweet));
		features.put(numb_of_intensifiers, IntensificationTweetFeature.getScore(tweet));
		features.put(has_geolocation, tweet.getGeoLocation().length>0?1.0:0);
		features.put(tweet_length, (double)tweet.getText().length());
		features.put(userFollowersCount, (double) tweet.getUserFollowersCount());
		features.put(userFriendsCount, (double) tweet.getUserFriendsCount());
		features.put(userRegistrationDays, UserActivenessTweetFeature.getScore(tweet));
		features.put(user_numb_of_tweets, (double)tweet.getUserNumbTweets());
		features.put(numb_of_user_description_chars, (double)tweet.getUserDescription().length());
		features.put(user_listed_count, (double)tweet.getUserListedCount());
		
		return features;
	}
}










