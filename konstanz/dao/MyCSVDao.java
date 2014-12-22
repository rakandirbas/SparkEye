package de.uni.konstanz.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import de.uni.konstanz.models.Tweet;

public class MyCSVDao {
	
	private static Logger logger = Logger.getLogger(MyCSVDao.class);
	
	public static LinkedList<Tweet> read(File path, int numberOfLines) {
		LinkedList<Tweet> tweets = new LinkedList<Tweet>();
		FileReader fileReader = null;
		BufferedReader reader = null;
		try {
			fileReader = new FileReader(path);
			reader = new BufferedReader(fileReader);
			String line = "";


			for ( int i = 0; ((line = reader.readLine()) != null) & 
					i <= numberOfLines; i++ ) {
				tweets.add(fromCSVLineToTweet(line));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error( "Error while trying to open csv file.", e );
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Error while trying to read line from csv file.", e);
		}
		finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error( "Error while closing the csv file.", e );
			}
		}
		return tweets;
	}
	
	public static LinkedList<Tweet> read(File path, int from, int to) {
		LinkedList<Tweet> tweets = new LinkedList<Tweet>();
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(path);
			BufferedReader reader = new BufferedReader(fileReader);
			String line = "";


			for ( int i = 0; ((line = reader.readLine()) != null)
					& i <= to; i++ ) {

				if ( i >= from ) {
					tweets.add(fromCSVLineToTweet(line));
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error( "Error while trying to open csv file.", e );
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Error while trying to read line from csv file.", e);
		}
		finally {
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error( "Error while closing the csv file.", e );
			}
		}
		return tweets;
	}
	
	public static LinkedList<Tweet> read(File path) {
		LinkedList<Tweet> tweets = new LinkedList<Tweet>();
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(path);
			BufferedReader reader = new BufferedReader(fileReader);
			String line = "";


			for ( int i = 0; ((line = reader.readLine()) != null); i++ ) {
				tweets.add(fromCSVLineToTweet(line));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error( "Error while trying to open csv file.", e );
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Error while trying to read line from csv file.", e);
		}
		finally {
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error( "Error while closing the csv file.", e );
			}
		}
		return tweets;
	}
	
	public static Tweet fromCSVLineToTweet( String line ) {
		Tweet tweet = new Tweet();
		String values[] = line.split("\t");
		tweet.getUser().setScreenName(values[0]);
		tweet.setCreatedAt( new Timestamp( Long.parseLong(values[1]) ) );
		tweet.setText( values[2] );
		tweet.getUser().setFriendsCount( Integer.parseInt( values[3] ) );
		tweet.getUser().setFollowersCount( Integer.parseInt( values[4] ) );
		tweet.getUser().setTweetsCount( Integer.parseInt( values[5] ) );
		return tweet;
	}
}
