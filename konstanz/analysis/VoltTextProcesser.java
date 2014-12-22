package de.uni.konstanz.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.uni.konstanz.models.VoltTweet;

public class VoltTextProcesser {
	
	public static SimpleDateFormat simpleDateFormat =
			new SimpleDateFormat( "EEE MMM dd HH:mm:ss Z yyyy", 
			Locale.ENGLISH );
	
	public static Set<String> mobileSourcesNames = 
			new LinkedHashSet<String>();
	
	static {
		mobileSourcesNames.add("Twitter for iPhone");
		mobileSourcesNames.add("Twitter for Android");
		mobileSourcesNames.add("Twitter for BlackBerry�");
	}
	
	//Regexes
	public static Matcher matcher;
	public static String tweetSourceRegex = "<a.*href=\"(.*)\" .*>(.*)</a>";
	public static Pattern tweetSourcePattern = Pattern.compile(tweetSourceRegex);
	
	public static String hashtagRegex = "^#\\w+|\\s#\\w+";
	public static Pattern hashtagPattern = Pattern.compile(hashtagRegex);

	public static String urlRegex = "http+://[\\S]+|https+://[\\S]+";
	public static Pattern urlPattern = Pattern.compile(urlRegex);

	public static String mentionRegex = "^@\\w+|\\s@\\w+";
	public static Pattern userMentionPattern = Pattern.compile(mentionRegex);
	
	
	public static void main(String[] args) throws Exception {
		String f1 = "/Users/rockyrock/Desktop/testCSV/2013_04_15_22.csv";
		String f2 = "/Users/rockyrock/Desktop/testCSV/2013_04_19_22.csv";
		BufferedReader reader = new BufferedReader( 
				new FileReader(new File(f1))); 

		String line = "";
		
		long t1 = System.currentTimeMillis();
		
		System.out.println("Reading");
		String headerLine = line = reader.readLine();
		String[] values;
		while( (line = reader.readLine()) != null ) {
			values = line.split( "\t" );
			
			if ( values.length == 25 ) {
				convertCSVLineToTweet(values);
			}
		}
		
		long t2 = System.currentTimeMillis();
		
		long  d = t2 - t1;
		
		d /= 1000;
		
		System.out.println("Done " + d);
	}
	
	public static VoltTweet convertCSVLineToTweet(String[] values) throws Exception {
		
		if ( values.length != 25 )
			throw new Exception("Can't covert to volt tweet because " +
					"the number of columns is different than 25");
		
		VoltTweet tweet = new VoltTweet();
		
		Date createdAt = simpleDateFormat.parse( values[1] );
		tweet.setCreatedAt( new Timestamp(createdAt.getTime()) );
		
		tweet.setText(values[2]);
		String[] tokens = values[2].toLowerCase().split(" ");
		for ( int i = 0; i < tokens.length; i++ ) {
			tokens[i] = tokens[i].trim();
		}
		tweet.setTokens(tokens);
		
		if ( values[3].equals("null") ) {
			tweet.setRetweet(false);
			tweet.setRetweetCount(0);
		}
		else {
			tweet.setRetweet(Boolean.parseBoolean( values[3] ));
			tweet.setRetweetCount(Long.parseLong(values[4]));
		}
		
		if ( !values[5].equals("null") ) {
			double[] geolocation = new double[2];
			String[] cords = values[5].split(",");
			//latitude
			geolocation[0] = Double.parseDouble(cords[0]);
			//longitude
			geolocation[1] = Double.parseDouble(cords[1]);
			tweet.setGeoLocation(geolocation);
		}
		
		matcher = tweetSourcePattern.matcher(values[6]);
		if ( !matcher.find() ) {
			tweet.setSentFromWeb(true);
			tweet.setSentFromMobile(false);
		}
		else {
			tweet.setSentFromWeb(false);
			if (mobileSourcesNames.contains(matcher.group(2))) {
				tweet.setSentFromMobile(true);
			}
		}
		
		tweet.setFavorited(Boolean.parseBoolean( values[7] ));
		
		tweet.setUserRealName(values[13]);
		tweet.setUserScreenName(values[14]);
		Date userRegistrationDate = simpleDateFormat.parse( values[15] );
		tweet.setUserRegistrationDate(new Timestamp(userRegistrationDate.getTime()));
		tweet.setUserNumbTweets( Integer.parseInt( values[17] ) );
		tweet.setUserFollowersCount( Integer.parseInt( values[18] ) );
		tweet.setUserLocation(values[19]);
		
		String userDescription = values[20];
		if ( !userDescription.equals("null") & !userDescription.equals("") ) {
			tweet.setUserDescription( userDescription );
		}
		else {
			tweet.setUserDescription("");
		}
		
		tweet.setUserFriendsCount( Integer.parseInt( values[21] ) );
		tweet.setUserListedCount( Integer.parseInt( values[23] ) );
		
		//Matches:
		
		//Matching hashtags
		matcher = hashtagPattern.matcher(tweet.getText());
		List<String> matchesList = new ArrayList<String>();
		while( matcher.find() ) {
			matchesList.add( matcher.group().trim().replace("#", "") );
		}
		if( !matchesList.isEmpty() ) {
			tweet.setHashtags( matchesList.toArray(new String[ matchesList.size() ]) );
		}
		
		//Matching user mentions
		matcher = userMentionPattern.matcher(tweet.getText());
		matchesList.clear();
		while( matcher.find() ) {
			matchesList.add( matcher.group().trim() );
		}
		if( !matchesList.isEmpty() ) {
			tweet.setUserMentions( matchesList.toArray(new String[ matchesList.size() ]) );
		}
		
		//Matching URLs
		matcher = urlPattern.matcher(tweet.getText());
		matchesList.clear();
		while( matcher.find() ) {
			matchesList.add( matcher.group().trim() );
		}
		
		if( !matchesList.isEmpty() ) {
			tweet.setUrls( matchesList.toArray(new String[ matchesList.size() ]) );
		}
		
		return tweet;
	}
}


















