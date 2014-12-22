package de.uni.kostanz.dataUtils;

import java.io.File;

import de.uni.konstanz.dao.CSVListener;
import de.uni.konstanz.dao.CSVStream;
import de.uni.konstanz.models.Tweet;

public class TweetFilter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File path = new File("/Users/rockyrock/Desktop/SyriaTweets/2013_08_21_10.csv");
		CSVListener listener = null;

		listener = new CSVListener() {

			@Override
			public void onReceiving(Tweet tweet) {
				System.out.println( tweet.getCreatedAt() + "---- " + tweet.getUser().getScreenName() + "---- " + tweet.getText() );
			}
		};

		CSVStream stream = new CSVStream(listener, path);

		stream.startStream();
	}

}
