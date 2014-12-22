package de.uni.konstanz.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import de.uni.konstanz.analysis.Preprocessor;
import de.uni.konstanz.models.GeoLocation;
import de.uni.konstanz.models.Place;
import de.uni.konstanz.models.Source;
import de.uni.konstanz.models.Tweet;
import de.uni.konstanz.models.UserMentionEntity;
import de.uni.konstanz.utils.FileUtils;

public class CSVDao {

	private static Logger logger = Logger.getLogger(CSVDao.class);

	public static String hashtagRegex = "^#\\w+|\\s#\\w+";
	public static Pattern hashtagPattern = Pattern.compile(hashtagRegex);

	public static String urlRegex = "http+://[\\S]+|https+://[\\S]+";
	public static Pattern urlPattern = Pattern.compile(urlRegex);

	public static String mentionRegex = "^@\\w+|\\s@\\w+";
	public static Pattern mentionPattern = Pattern.compile(mentionRegex);

	public static LinkedList<Tweet> read(File path, int from, int to) {
		LinkedList<Tweet> tweets = new LinkedList<Tweet>();
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(path);
			BufferedReader reader = new BufferedReader(fileReader);
			String line = "";


			for ( int i = 0; ((line = reader.readLine()) != null)
					& i <= to; i++ ) {

				if ( i == 0)
					continue;
				if ( i >= from ) {

					if ( line.split("\t").length == 25 ) {
						tweets.add(fromCSVLineToTweet(line));
					}
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

				if ( i == 0)
					continue;
				if ( line.split("\t").length == 25 ) {
					Tweet tweet = fromCSVLineToTweet(line);
					if ( tweet != null ) {
						tweets.add(tweet);
					}
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

	public static LinkedList<Tweet> read(File path, int numberOfLines) {
		LinkedList<Tweet> tweets = new LinkedList<Tweet>();
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(path);
			BufferedReader reader = new BufferedReader(fileReader);
			String line = "";

			

			for ( int i = 0; ((line = reader.readLine()) != null) & 
					i <= numberOfLines; ) {

				
				
				if ( i == 0) {
					i++;
					continue;
				}
				
				if ( line.split("\t").length == 25 ) {
					
					Tweet tweet = fromCSVLineToTweet(line);
					
					if ( tweet != null ) {
						tweets.add(tweet);
						i++;
					}
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

	public static Tweet fromCSVLineToTweet( String line ) throws IOException {
		Tweet tweet = new Tweet();
		try {
			String values[] = line.split( "\t" );
			tweet.id = UUID.randomUUID().getLeastSignificantBits();;
			tweet.status_id = Long.parseLong(values[0]);
			tweet.harvestingDate = new Timestamp(tweet.id);

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "EEE MMM dd HH:mm:ss Z yyyy", 
					Locale.ENGLISH );
			Date createdAt = simpleDateFormat.parse( values[1] );
			tweet.createdAt = new Timestamp(createdAt.getTime());

			tweet.text = values[2];

			//Turn this on if you want to enable lang detection
//			if ( !Preprocessor.preprocessor.pass(tweet.text) ) {
//				return null;
//			}

			//Turn the fastTokensList off to get tagged token
			tweet.tokensList = 
					Preprocessor.preprocessor.getTokensList( tweet.text );

			tweet.simpleTokens = Preprocessor.preprocessor.getSimpleTokensList(tweet.text);

			if ( values[3].equals("null") ) {
				tweet.isRetweet = false;
				tweet.retweetCount = 0;
			}
			else {
				tweet.isRetweet = Boolean.parseBoolean( values[3] );
				tweet.retweetCount = Long.parseLong(values[4]);
			}
			if ( !values[5].equals("null") ) {
				tweet.geoLocation = new GeoLocation();
				String[] cords = values[5].split(",");
				tweet.geoLocation.setLatitude( Double.parseDouble(cords[0]) );
				tweet.geoLocation.setLongitude( Double.parseDouble(cords[1]) );

			}
			tweet.source = Source.getSource( values[6] );
			tweet.isFavorited = Boolean.parseBoolean( values[7] );

			if ( !values[8].equals("null") ) {
				String[] placeValues= values[8].split(",");
				if ( placeValues.length == 5 ) {
					tweet.place = new Place();
					tweet.place.setCountry( placeValues[2] );
					tweet.place.setName( placeValues[0] );
					tweet.place.setPlaceType( placeValues[4] );
				}
			}
			
			String inReplyToStatusId = values[9];
			if ( inReplyToStatusId.equals("null") || inReplyToStatusId.equals("")  ) {
				tweet.inReplyToStatusId = 0;
			}
			else {
				tweet.inReplyToStatusId = Long.parseLong( values[9] );
			}
			
			String inReplyToUserId = values[10];
			if ( inReplyToUserId.equals("null") || inReplyToUserId.equals("") ) {
				tweet.inReplyToUserId = 0;
			}
			else {
				tweet.inReplyToUserId = Long.parseLong( values[10] );
			}
			
			
			String inReplyToScreenName = values[11];
			if ( !inReplyToScreenName.equals("null") ) {
				tweet.inReplyToScreenName = inReplyToScreenName;
			}
			else {
				tweet.inReplyToScreenName = null;
			}

			tweet.user.setId( Long.parseLong( values[12] ) );
			tweet.user.setName( values[13] );
			tweet.user.setScreenName( values[14] );

			Date registrationDate = simpleDateFormat.parse( values[15] );
			tweet.user.setRegistrationDate( new Timestamp(registrationDate.getTime()) );

			tweet.user.setLang( values[16] );
			tweet.user.setTweetsCount( Integer.parseInt( values[17] ) );
			tweet.user.setFollowersCount( Integer.parseInt( values[18] ) );
			tweet.user.setLocation( values[19] );

			String userDescription = values[20];
			if ( !userDescription.equals("null") & !userDescription.equals("") ) {
				tweet.user.setDescription( userDescription );
			}
			else {
				tweet.user.setDescription("");
			}

			tweet.user.setFriendsCount( Integer.parseInt( values[21] ) );

			String timezone = values[22];
			if ( !timezone.equals("null") ) {
				tweet.user.setTimeZone(timezone);
			}

			tweet.user.setListedCount( Integer.parseInt( values[23] ) );

			//			String hashtagRegex = "^#\\w+|\\s#\\w+";
			//			Pattern hashtagPattern = Pattern.compile(hashtagRegex);
			Matcher m = hashtagPattern.matcher(tweet.text);
			List<String> hashtagMatches = new ArrayList<String>();
			while(m.find()){
				hashtagMatches.add(m.group().replace(" ", ""));
			}

			if ( hashtagMatches.size() > 0 ) {
				tweet.hashTags = hashtagMatches.toArray( 
						new String[ hashtagMatches.size() ] );
			}

			if ( tweet.hashTags != null ) {
				for ( int i = 0; i < tweet.hashTags.length; i++ ) {
					String tag = tweet.hashTags[i];
					tag = tag.replace("#", "");
					tweet.hashTags[i] = tag;
				}
			}

			//			String mentionRegex = "^@\\w+|\\s@\\w+";
			//			Pattern mentionPattern = Pattern.compile(mentionRegex);
			m = mentionPattern.matcher(tweet.text);
			List<String> mentionMatches = new ArrayList<String>();
			while( m.find() ) {
				mentionMatches.add( (m.group().replace(" ", "")).replace("@", "") );
			}

			if ( mentionMatches.size() > 0 ) {
				tweet.userMentionEntites = 
						new UserMentionEntity[ mentionMatches.size() ];
				for ( int i = 0; i < mentionMatches.size(); i++ ) {
					tweet.userMentionEntites[i] = new UserMentionEntity();
					tweet.userMentionEntites[i].setScreenName( mentionMatches.get(i) );
				}
			}

			//			String urlRegex = "http+://[\\S]+|https+://[\\S]+";
			//			Pattern urlPattern = Pattern.compile(urlRegex);
			m = urlPattern.matcher(tweet.text);
			List<String> urlMatches = new ArrayList<String>();
			while( m.find() ) {
				urlMatches.add( m.group() );
			}

			if( urlMatches.size() > 0 ) {
				tweet.URLs = urlMatches.toArray( new String[ urlMatches.size() ] );
			}

		} catch (ParseException e) {
			e.printStackTrace();
			logger.error( "Error while parsing stuff in " +
					"converting from CSV line to Tweet."
					, e);
		} //catch (IOException e) {
//			e.printStackTrace();
//			logger.error("Error while tokenizing the tweet text in from CSV to" +
//					"a Tweet object", e);
//		}

		return tweet;

	}

	public static void printCSVLine( String line ) {
		String values[] = line.split("\t");

		for ( int i = 0; i < values.length; i++ ) {
			System.out.println( (i+1) + ": " + values[i] ); 
		}


	}

	public static void printCSVLine( File path, int lineNumber ) {
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(path);
			BufferedReader reader = new BufferedReader(fileReader);
			String line = "";


			for ( int i = 0; ((line = reader.readLine()) != null) & 
					i <= lineNumber; i++ ) {

				if ( i == 0)
					continue;
				if ( i == lineNumber ) {
					printCSVLine(line);
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

	}

	public static void printCSVLine( File path, int from, int to ) {
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(path);
			BufferedReader reader = new BufferedReader(fileReader);
			String line = "";


			for ( int i = 0; ((line = reader.readLine()) != null) & 
					i <= to; i++ ) {

				if ( i == 0)
					continue;
				if ( i >= from ) {
					printCSVLine(line);
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
	}

	public static void printCSVLine( File path ) {
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(path);
			BufferedReader reader = new BufferedReader(fileReader);
			String line = "";
			int total = 0;

			for ( int i = 0; ((line = reader.readLine()) != null); i++) {

				if ( i == 0)
					continue;
				//System.out.println(i);
				total++;
			}

			System.out.println( "Total: " + total );

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
	}

	public static int getNumberOfLines( File path ) {
		int total = 0;

		FileReader fileReader = null;
		try {
			fileReader = new FileReader(path);
			BufferedReader reader = new BufferedReader(fileReader);
			String line = "";

			for ( int i = 0; ((line = reader.readLine()) != null); i++) {

				if ( i == 0)
					continue;
				total++;
			}

			//System.out.println( "Total: " + total );

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

		return total;
	}

	public static int getNumberOfTweets( File path ) {
		BufferedReader reader = FileUtils.getFileReader(path);
		String line = "";
		int total = 0;

		try {
			for ( int i = 0; ((line = reader.readLine()) != null); i++ ) {

				if ( i == 0)
					continue;
				if ( line.split("\t").length == 25 ) {
					total++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Error while reading line in getNumberOfTweets", e);
		}
		finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("Error while closing reader" +
						" in getNumberOfTweets", e);
			}
		}

		return total;

	}

}
