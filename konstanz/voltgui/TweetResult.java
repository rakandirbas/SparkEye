package de.uni.konstanz.voltgui;

import de.uni.konstanz.models.VoltTweet;

public class TweetResult {
	private VoltTweet tweet;
	
	public TweetResult(VoltTweet tweet) {
		this.tweet = tweet;
	}
	
	public VoltTweet getTweet() {
		return tweet;
	}
	
	public String toString() {
		return tweet.getText();
	}
}
