package de.uni.konstanz.preprocessing;

import de.uni.konstanz.models.VoltTweet;

public interface Filter {

	public boolean pass(VoltTweet tweet);
	
}
