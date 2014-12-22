package de.uni.konstanz.dao;

import de.uni.konstanz.models.Tweet;

public interface CSVListener {

	public void onReceiving(Tweet tweet);

}
