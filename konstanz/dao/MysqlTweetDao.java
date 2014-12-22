package de.uni.konstanz.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.rakblog.MysqlConnection;

import de.uni.konstanz.models.MediaEntity;
import de.uni.konstanz.models.Tweet;
import de.uni.konstanz.models.UserMentionEntity;

public class MysqlTweetDao implements TweetDao {
	private Connection con;
	private PreparedStatement pstmTweetQuery, pstmSourceQuery,
		pstmHashtagQuery, pstmMediaEntityQuery, pstmUrlQuery,
		pstmUserMentionEntitesQuery, pstmPlaceQuery, 
		pstmGeoLocationQuery, pstmContributersQuery,
		pstmUserQuery, pstmDescriptionURLQuery;
	private String dbName;
	private String tweetQuery, sourceQuery, hashtagQuery, 
		mediaEntityQuery, urlQuery, userMentionEntitesQuery, 
		placeQuery, geoLocationQuery, contributersQuery, userQuery,
		descriptionURLQuery;
		

	public MysqlTweetDao( String dbName ) {
		this.dbName = dbName;
		initializeQueries();
		MysqlConnection mysqlCon = new 
				MysqlConnection("" + dbName + "", "127.0.0.1", 3306, "root", "root");
		con = mysqlCon.getConnection();
		
		try {
			pstmTweetQuery = con.prepareStatement(tweetQuery);
			pstmSourceQuery = con.prepareStatement(sourceQuery);
			pstmHashtagQuery = con.prepareStatement(hashtagQuery);
			pstmMediaEntityQuery = con.prepareStatement(mediaEntityQuery);
			pstmUrlQuery = con.prepareStatement(urlQuery);
			pstmUserMentionEntitesQuery = con.prepareStatement(userMentionEntitesQuery);
			pstmPlaceQuery = con.prepareStatement(placeQuery);
			pstmGeoLocationQuery = con.prepareStatement(geoLocationQuery);
			pstmContributersQuery = con.prepareStatement(contributersQuery);
			pstmUserQuery = con.prepareStatement(userQuery);
			pstmDescriptionURLQuery = con.prepareStatement(descriptionURLQuery);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void put(Tweet tweet) {
		
		try {
			con.setAutoCommit(false);
			java.sql.Statement stat = con.createStatement();
		    String query = "SET NAMES utf8mb4";
		    stat.execute(query);
			stat.close();
		    
			pstmTweetQuery.setLong( 1, tweet.id );
			pstmTweetQuery.setLong( 2, tweet.status_id );
			pstmTweetQuery.setTimestamp(3, tweet.harvestingDate);
			pstmTweetQuery.setTimestamp(4, tweet.createdAt);
			pstmTweetQuery.setString(5, tweet.text);
			pstmTweetQuery.setString( 6, tweet.inReplyToScreenName );
			pstmTweetQuery.setLong( 7, tweet.inReplyToStatusId );
			pstmTweetQuery.setLong(8, tweet.inReplyToUserId);
			pstmTweetQuery.setLong( 9, tweet.retweetCount );
			pstmTweetQuery.setLong(10, tweet.currentUserRetweetId);
			pstmTweetQuery.setBoolean(11, tweet.isFavorited);
			pstmTweetQuery.setBoolean(12, tweet.isPossiblySensitive);
			pstmTweetQuery.setBoolean(13, tweet.isRetweet);
			pstmTweetQuery.setBoolean(14, tweet.isRetweetedByMe);
			pstmTweetQuery.setBoolean(15, tweet.isTruncated);
			pstmTweetQuery.executeUpdate();
			
			pstmSourceQuery.setString(1, tweet.source.getName());
			pstmSourceQuery.setString(2, tweet.source.getUrl());
			pstmSourceQuery.setLong(3, tweet.id);
			pstmSourceQuery.executeUpdate();
			
			
			if (tweet.hashTags != null) {
				for (String h : tweet.hashTags) {
					pstmHashtagQuery.setString(1, h);
					pstmHashtagQuery.setLong(2, tweet.id);
					pstmHashtagQuery.executeUpdate();
				}
			}
			
			if (tweet.mediaEntities != null) {
				for (MediaEntity me : tweet.mediaEntities) {
					pstmMediaEntityQuery.setString(1, me.getUrl());
					pstmMediaEntityQuery.setString(2, me.getMediaURL());
					pstmMediaEntityQuery.setString(3, me.getType());
					pstmMediaEntityQuery.setLong(4, tweet.id);
					pstmMediaEntityQuery.executeUpdate();
				}
			}
			
			if (tweet.URLs != null) {
				for (String url : tweet.URLs) {
					pstmUrlQuery.setString(1, url);
					pstmUrlQuery.setLong(2, tweet.id);
					pstmUrlQuery.executeUpdate();
				}
			}
			
			if ( tweet.userMentionEntites != null ) {
				for ( UserMentionEntity ume : tweet.userMentionEntites ) {
					pstmUserMentionEntitesQuery.setLong(1, ume.getId());
					pstmUserMentionEntitesQuery.setString(2, ume.getName());
					pstmUserMentionEntitesQuery.setString(3, ume.getScreenName());
					pstmUserMentionEntitesQuery.setLong(4, tweet.id);
					pstmUserMentionEntitesQuery.executeUpdate();
				}
			}
			
			if (tweet.place != null) {
				pstmPlaceQuery.setString(1, tweet.place.getId());
				pstmPlaceQuery.setString(2, tweet.place.getCountry());
				pstmPlaceQuery.setString(3, tweet.place.getCountryCode());
				pstmPlaceQuery.setString(4, tweet.place.getFullName());
				pstmPlaceQuery.setString(5, tweet.place.getName());
				pstmPlaceQuery.setString(6, tweet.place.getPlaceType());
				pstmPlaceQuery.setString(7, tweet.place.getStreetAddress());
				pstmPlaceQuery.setString(8, tweet.place.getUrl());
				pstmPlaceQuery.setString(9, tweet.place.getGeometryType());
				pstmPlaceQuery.setLong(10, tweet.id);
				pstmPlaceQuery.executeUpdate();
			}
			
			if ( tweet.geoLocation != null ) {
				pstmGeoLocationQuery.setDouble(1, tweet.geoLocation.getLatitude());
				pstmGeoLocationQuery.setDouble(2, tweet.geoLocation.getLongitude());
				pstmGeoLocationQuery.setLong(3, tweet.id);
				pstmGeoLocationQuery.executeUpdate();
			}
			
			if (tweet.contributors != null) {
				for (long cont : tweet.contributors) {
					pstmContributersQuery.setLong(1, cont);
					pstmContributersQuery.setLong(2, tweet.id);
					pstmContributersQuery.executeUpdate();
				}
			}
			
			pstmUserQuery.setLong(1, tweet.user.getId());
			pstmUserQuery.setString(2, tweet.user.getName());
			pstmUserQuery.setString(3, tweet.user.getScreenName());
			pstmUserQuery.setString(4, tweet.user.getDescription());
			pstmUserQuery.setString(5, tweet.user.getLang());
			pstmUserQuery.setString(6, tweet.user.getLocation());
			pstmUserQuery.setString(7, tweet.user.getTimeZone());
			pstmUserQuery.setString(8, tweet.user.getUrl());
			pstmUserQuery.setInt(9, tweet.user.getFollowersCount());
			pstmUserQuery.setInt(10, tweet.user.getFriendsCount());
			pstmUserQuery.setInt(11, tweet.user.getTweetsCount());
			pstmUserQuery.setInt(12, tweet.user.getFavouritesCount());
			pstmUserQuery.setString(13, tweet.user.getOriginalProfileImageURL());
			pstmUserQuery.setString(14, tweet.user.getBiggerProfileImageURL());
			pstmUserQuery.setString(15, tweet.user.getProfileImageURL());
			pstmUserQuery.setString(16, tweet.user.getProfileBannerURL());
			pstmUserQuery.setString(17, tweet.user.getProfileBackgroundColor());
			pstmUserQuery.setString(18, tweet.user.getProfileLinkColor());
			pstmUserQuery.setString(19, tweet.user.getProfileSidebarBorderColor());
			pstmUserQuery.setString(20, tweet.user.getProfileSidebarFillColor());
			pstmUserQuery.setString(21, tweet.user.getProfileTextColor());
			pstmUserQuery.setBoolean(22, tweet.user.isGeoEnabled());
			pstmUserQuery.setBoolean(23, tweet.user.isProfileBackgroundTiled());
			pstmUserQuery.setBoolean(24, tweet.user.isProfileUseBackgroundImage());
			pstmUserQuery.setBoolean(25, tweet.user.isProtected());
			pstmUserQuery.setBoolean(26, tweet.user.isContributorsEnabled());
			pstmUserQuery.setBoolean(27, tweet.user.isShowAllInlineMedia());
			pstmUserQuery.setBoolean(28, tweet.user.isTranslator());
			pstmUserQuery.setBoolean(29, tweet.user.isVerified());
			pstmUserQuery.setLong(30, tweet.id);
			pstmUserQuery.executeUpdate();
			
			
			if ( tweet.user.getDescriptionURLs() != null ) {
				for (String url : tweet.user.getDescriptionURLs()) {
					pstmDescriptionURLQuery.setString(1, url);
					pstmDescriptionURLQuery.setLong(2, tweet.user.getId());
					pstmDescriptionURLQuery.executeUpdate();
				}
			}
			
			con.commit();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Tweet get() {
		return null;
	}
	
	public void initializeQueries() {
		tweetQuery = "INSERT INTO `" + dbName + "`.`Tweet` " +
				"(`id`,`status_id`, `harvestingDate`, `createdAt`, `text`, " +
				"`inReplyToScreenName`, `inReplyToStatusId`, " +
				"`inReplyToUserId`, `retweetCount`, `currentUserRetweetId`, " +
				"`isFavorited`, `isPossiblySensitive`, `isRetweet`, " +
				"`isRetweetedByMe`, `isTruncated`) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		
		sourceQuery = "INSERT INTO `" + dbName + "`.`Source` " +
				"(`name`, `url`, `tweet_id`) " +
				"VALUES (?, ?, ?);";
		
		hashtagQuery = "INSERT INTO `" + dbName + "`.`Hashtag` " +
				"(`hashtag`, `tweet_id`) " +
				"VALUES (?, ?);";
		mediaEntityQuery = "INSERT INTO `" + dbName + "`.`MediaEntity` " +
				"(`url`, `mediaURL`, `type`, `tweet_id`) " +
				"VALUES (?, ?, ?, ?);";
		
		urlQuery = "INSERT INTO `" + dbName + "`.`URL` " +
				"(`url`, `tweet_id`) VALUES (?, ?);";
		
		userMentionEntitesQuery = "INSERT INTO `" + dbName + "`.`UserMentionEntity` " +
				"(`mention_id`, `name`, `screenName`, `tweet_id`) " +
				"VALUES (?, ?, ?, ?);";
		
		placeQuery = "INSERT INTO `" + dbName + "`.`Place` " +
				"(`placeID`, `country`, `countryCode`, `fullName`," +
				" `name`, `placeType`, `streetAddress`, `url`," +
				" `geometryType`, `tweet_id`) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		
		geoLocationQuery = "INSERT INTO `" + dbName + "`.`GeoLocation` " +
				"(`latitude`, `longitude`, `tweet_id`) " +
				"VALUES (?, ?, ?);";

		contributersQuery = "INSERT INTO `" + dbName + "`.`Contributors` " +
				"(`contributor`, `tweet_id`) " +
				"VALUES (?, ?);";

		userQuery = "INSERT INTO `" + dbName + "`.`User` " +
				"(`user_id`, `name`, `screenName`, `description`, " +
				"`lang`, `location`, `timeZone`, `url`, " +
				"`followersCount`, `friendsCount`, `tweetsCount`, " +
				"`favouritesCount`, `originalProfileImageURL`, " +
				"`biggerProfileImageURL`, `profileImageURL`, " +
				"`profileBannerURL`, `profileBackgroundColor`, " +
				"`profileLinkColor`, `profileSidebarBorderColor`, " +
				"`profileSidebarFillColor`, `profileTextColor`, " +
				"`isGeoEnabled`, `isProfileBackgroundTiled`, " +
				"`isProfileUseBackgroundImage`, `isProtected`, " +
				"`isContributorsEnabled`, `isShowAllInlineMedia`, " +
				"`isTranslator`, `isVerified`, `tweet_id`) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, " +
				"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
				"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		
		descriptionURLQuery = "INSERT INTO `" + dbName + "`.`DescriptionURL` " +
				"(`url`, `user_id`) VALUES (?, ?);";
	}

}

















