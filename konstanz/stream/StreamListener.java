package de.uni.konstanz.stream;

import de.uni.konstanz.models.VoltTweet;


public interface StreamListener {
	public void onReceiving(VoltTweet tweet);
}
