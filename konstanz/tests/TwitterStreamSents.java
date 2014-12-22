package de.uni.konstanz.tests;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import twitter4j.Paging;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import uk.ac.wlv.sentistrength.SentiStrength;
import de.uni.konstanz.utils.FileUtils;

public class TwitterStreamSents {
	public static void main(String[] args) {
		saveUserTweetsToFile("SimaDiab", "SimaDiab.txt", 10);
		saveUserTweetSentsToFile("SimaDiab.txt", "SimaDiab_sents.txt");

	}

	public static StatusListener getStatusListener() {
		StatusListener listener = new StatusListener() {
			public void onStatus(Status status) {

			}

			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
			}

			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
			}

			@SuppressWarnings("unused")
			public void onScrubGeo(long userId, long upToStatusId) {
			}

			public void onException(Exception ex) {
				ex.printStackTrace();
			}

			public void onScrubGeo(int arg0, long arg1) {

			}

			@Override
			public void onStallWarning(StallWarning arg0) {

			}
		};

		return listener;
	}
	
	public static void saveUserTweetSentsToFile(String tweetFileName, String toSaveFileName) {
		String inis[] = new String[]{"sentidata",
				"resources/sentistrength/SentStrength_Data_Sept2011/",
		"trinary"};
		SentiStrength sentiStrength = new SentiStrength();
		sentiStrength.initialise(inis);


		//System.out.println( sentiStrength.computeSentimentScores("") );

		try {
			BufferedReader reader = FileUtils.getFileReader( new File(tweetFileName) );
			BufferedWriter writer = FileUtils.getFileWriter( new File(toSaveFileName) );
			String line = "";

			while((line = reader.readLine()) != null) {
				if(!line.isEmpty()) {
					String result = sentiStrength.computeSentimentScores(line);
					writer.write(String.format("%s\n", result));
				}
			}
			
			writer.close();
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void saveUserTweetsToFile(String username, String saveToFileName, int max_pages) {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey("hRD5rkoMKMtxFxmQLbVNiQ")
		.setOAuthConsumerSecret("mvHJzaulVMsYICLMCyv5qQbWWSjAK62XuwFUQeN6U")
		.setOAuthAccessToken("93020906-fNLOnNC0Q6RO69jJZBzjNwCGodNqfRLbTmOWBihrI")
		.setOAuthAccessTokenSecret("jh92vRVIoiXSXdYkbEpoOQrud5sSKn1UWBPaQBSfs0");
		System.out.println("Started ....");

		BufferedWriter writer = FileUtils.getFileWriter(new File(saveToFileName));

		try {
			Twitter twitter = new TwitterFactory( cb.build() ).getInstance();

			for ( int i = 1; i <= max_pages; i++ ) {
				for ( Status s : twitter.getUserTimeline(username, new Paging(i)) ){
					writer.write(String.format("%s\n", s.getText()));
				}
			}
			System.out.println("Done.");

			writer.close();

		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to delete status: " + te.getMessage());
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
