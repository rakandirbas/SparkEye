package de.uni.konstanz.dao;

import de.uni.konstanz.models.Tweet;

public interface TweetDao {
	public void put(Tweet tweet);
	public Tweet get();
}
