package de.uni.konstanz.models;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;

import twitter4j.HashtagEntity;
import twitter4j.Status;
import twitter4j.URLEntity;
import de.uni.konstanz.analysis.Preprocessor;
import de.uni.konstanz.utils.IDGenerator;

public class Tweet implements Comparable<Tweet> {
	
	private static Logger logger = Logger.getLogger(Tweet.class);

	public long id;
	public long status_id;
	public Timestamp harvestingDate;
	public Timestamp createdAt;
	public String text;
	public List<String> simpleTokens;
	public List<Token> tokensList;
	public Source source;//Table
	public String inReplyToScreenName;
	public long inReplyToStatusId;
	public long inReplyToUserId;
	public long retweetCount;
	public long currentUserRetweetId;
	public String[] hashTags;//Table
	public MediaEntity[] mediaEntities;//Table
	public String[] URLs;//Table
	public String[] locations;
	public UserMentionEntity[] userMentionEntites;//Table
	public Place place;//Table
	public GeoLocation geoLocation;//Table
	public User user;//Table (and inner table)
	public long[] contributors;//Table

	public boolean isFavorited, isPossiblySensitive, 
	isRetweet, isRetweetedByMe, isTruncated;

	public Tweet() {
		//initialize the fields!!!
		//initiazlise Strings to ""!!!! even in inner classes :/
		source = new Source();
		user = new User();
	}

	public static Tweet getTweet( Status status ) {
		Tweet tweet = new Tweet();
		tweet.id = IDGenerator.getID();
		tweet.status_id = status.getId();
		tweet.harvestingDate = new Timestamp( System.currentTimeMillis() );
		tweet.createdAt = new Timestamp(status.getCreatedAt().getTime());
		tweet.text = status.getText();
		try {
			tweet.tokensList = Preprocessor.preprocessor.getTokensList( tweet.text );
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Error while tokenizing the tweet text in from Twitter API to" +
					"a Tweet object", e);
		}
		tweet.source = Source.getSource(status.getSource());
		tweet.inReplyToScreenName = status.getInReplyToScreenName();
		tweet.inReplyToStatusId = status.getInReplyToStatusId();
		tweet.inReplyToUserId = status.getInReplyToUserId();
		tweet.retweetCount = status.getRetweetCount();
		tweet.currentUserRetweetId = status.getCurrentUserRetweetId();

		if ( status.getHashtagEntities() != null ) {
			HashtagEntity[] hashtagEntities = status.getHashtagEntities();
			tweet.hashTags = new String[hashtagEntities.length];
			for ( int i = 0; i < tweet.hashTags.length; i++ ) {
				tweet.hashTags[i] = hashtagEntities[i].getText();
			}
		}

		if ( status.getMediaEntities() != null ) {
			twitter4j.MediaEntity[] twitter4jMediaEntities = status.getMediaEntities(); 
			tweet.mediaEntities = new MediaEntity[ twitter4jMediaEntities.length ];
			for ( int i = 0; i < tweet.mediaEntities.length; i++ ) {
				de.uni.konstanz.models.MediaEntity mediaEntity = new MediaEntity();
				mediaEntity.mediaURL = twitter4jMediaEntities[i].getMediaURL();
				mediaEntity.url = twitter4jMediaEntities[i].getExpandedURL();
				mediaEntity.type = twitter4jMediaEntities[i].getType();
				tweet.mediaEntities[i] = mediaEntity;
			}
		}

		if ( status.getURLEntities() != null ) {
			URLEntity[] URLEntities = status.getURLEntities();
			tweet.URLs = new String[ URLEntities.length ];
			for ( int i = 0; i < tweet.URLs.length; i++ ) {
				tweet.URLs[i] = URLEntities[i].getExpandedURL();
			}
		}

		if ( status.getUserMentionEntities() != null ) {
			twitter4j.UserMentionEntity[] userMentionEntities = 
					status.getUserMentionEntities();
			tweet.userMentionEntites = new 
					UserMentionEntity[ userMentionEntities.length ];
			for ( int i = 0; i < tweet.userMentionEntites.length; i++ ) {
				UserMentionEntity userMentionEntity = new UserMentionEntity();
				userMentionEntity.id = userMentionEntities[i].getId();
				userMentionEntity.name = userMentionEntities[i].getName();
				userMentionEntity.screenName = userMentionEntities[i].getScreenName();
				tweet.userMentionEntites[i] = userMentionEntity;
			}
		}

		if ( status.getPlace() != null ) {
			tweet.place = new Place();
			tweet.place.id = status.getPlace().getId();
			tweet.place.name = status.getPlace().getName();
			tweet.place.fullName = status.getPlace().getFullName();
			tweet.place.country = status.getPlace().getCountry();
			tweet.place.countryCode = status.getPlace().getCountryCode();
			tweet.place.geometryType = status.getPlace().getGeometryType();
			tweet.place.placeType = status.getPlace().getPlaceType();
			tweet.place.streetAddress = status.getPlace().getStreetAddress();
			tweet.place.url = status.getPlace().getURL();
		}

		if ( status.getGeoLocation() != null ) {
			tweet.geoLocation = new GeoLocation();
			tweet.geoLocation.latitude = status.getGeoLocation().getLatitude();
			tweet.geoLocation.longitude = status.getGeoLocation().getLongitude();
		}

		if ( status.getUser() != null ) {
			tweet.user = new User();
			tweet.user.id = status.getUser().getId();
			tweet.user.registrationDate = new Timestamp(status.getUser().getCreatedAt().getTime());
			tweet.user.name = status.getUser().getName();
			tweet.user.screenName = status.getUser().getScreenName();
			tweet.user.tweetsCount = status.getUser().getStatusesCount();
			tweet.user.followersCount = status.getUser().getFollowersCount();
			tweet.user.friendsCount = status.getUser().getFriendsCount();
			tweet.user.favouritesCount = status.getUser().getFavouritesCount();
			tweet.user.listedCount = status.getUser().getListedCount();
			tweet.user.description = status.getUser().getDescription();
			tweet.user.url = status.getUser().getURLEntity().getExpandedURL();
			tweet.user.profileImageURL = status.getUser().getProfileImageURL();
			tweet.user.biggerProfileImageURL = status.getUser().getBiggerProfileImageURL();
			tweet.user.originalProfileImageURL = status.getUser().getOriginalProfileImageURL();
			tweet.user.lang = status.getUser().getLang();
			tweet.user.location = status.getUser().getLocation();
			tweet.user.timeZone = status.getUser().getTimeZone();

			tweet.user.isContributorsEnabled = status.getUser().isContributorsEnabled();
			tweet.user.isGeoEnabled = status.getUser().isGeoEnabled();
			tweet.user.isProfileBackgroundTiled = status.getUser().isProfileBackgroundTiled();
			tweet.user.isProfileUseBackgroundImage = status.getUser().isProfileUseBackgroundImage();
			tweet.user.isProtected = status.getUser().isProtected();
			tweet.user.isShowAllInlineMedia = status.getUser().isShowAllInlineMedia();
			tweet.user.isTranslator = status.getUser().isTranslator();
			tweet.user.isVerified = status.getUser().isVerified();

			tweet.user.profileBackgroundColor = status.getUser().getProfileBackgroundColor();
			tweet.user.profileBannerURL = status.getUser().getProfileBannerURL();
			tweet.user.profileLinkColor = status.getUser().getProfileLinkColor();//
			tweet.user.profileSidebarFillColor = status.getUser().getProfileSidebarFillColor();
			tweet.user.profileSidebarBorderColor = status.getUser().getProfileSidebarBorderColor();
			tweet.user.profileTextColor = status.getUser().getProfileTextColor();

			if ( status.getUser().getDescriptionURLEntities() != null ) {
				tweet.user.descriptionURLs = new 
						String[ status.getUser().getDescriptionURLEntities().length ];
				for ( int i = 0; i < tweet.user.descriptionURLs.length; i++ ) {
					tweet.user.descriptionURLs[i] = 
							status.getUser().getDescriptionURLEntities()[i].getExpandedURL();
				}
			}


			tweet.contributors = status.getContributors();
			tweet.isFavorited = status.isFavorited();
			tweet.isPossiblySensitive = status.isPossiblySensitive();
			tweet.isRetweet = status.isRetweet();
			tweet.isRetweetedByMe = status.isRetweetedByMe();
			tweet.isTruncated = status.isTruncated();
		}
		return tweet;
	}

	public String toString() {
		return getText();
//		String dump = "\n######### Printing Tweet Dump #########\n";
//
//		dump += "Tweet.id: " + this.id + "\n";
//		dump += "Tweet.harvestingDate: " + this.harvestingDate + "\n";
//		dump += "Tweet.createdAt: " + this.createdAt + "\n";
//		dump += "Tweet.text: " + this.text + "\n";
//		dump += "Tweet.source: \n";
//		dump += "\tTweet.source.name: " + this.source.name + "\n";
//		dump += "\tTweet.source.url: " + this.source.url + "\n";
//
//		dump += "Tweet.inReplyToScreenName: " + this.inReplyToScreenName + "\n";
//		dump += "Tweet.inReplyToStatusId: " + this.inReplyToStatusId + "\n";
//		dump += "Tweet.inReplyToUserId: " + this.inReplyToUserId + "\n";
//		dump += "Tweet.retweetCount: " + this.retweetCount + "\n";
//		dump += "Tweet.currentUserRetweetId: " + this.currentUserRetweetId + "\n";
//
//		dump += "Tweet.hashTags: ";
//		if ( this.hashTags != null ) {
//			for ( String h : this.hashTags ) {
//				dump += h + ", ";
//			}
//		}
//		dump += "\n";
//
//		dump += "Tweet.mediaEntities: \n";
//		if ( this.mediaEntities != null ) {
//			for ( MediaEntity mE : this.mediaEntities ) {
//				dump += "\tMedia Entity: \n";
//				dump += "\t\tMedial URL: " + mE.mediaURL + "\n";
//				dump += "\t\tURL: " + mE.url + "\n";
//				dump += "\t\tType: " + mE.type + "\n";
//			}
//		}
//
//		dump += "Tweet.URLs: \n";
//		if ( this.URLs != null ) {
//			for ( String url : this.URLs ) {
//				dump += "\t" + url + "\n";
//			}
//		}
//
//		dump += "Tweet.userMentionEntities: \n";
//		if ( this.userMentionEntites != null ) {
//			for ( UserMentionEntity ume : this.userMentionEntites ) {
//				dump += "\tUser Mention Entitiy:\n";
//				dump += "\t\tID: " + ume.id + "\n";
//				dump += "\t\tName: " + ume.name + "\n";
//				dump += "\t\tScreenName: " + ume.screenName + "\n";
//			}
//		}
//
//		dump += "Tweet.place: \n";
//		if ( this.place != null ) {
//			dump += "\tID: " + this.place.id + "\n";
//			dump += "\tName: " + this.place.name + "\n";
//			dump += "\tfullName: " + this.place.fullName + "\n";
//			dump += "\tCountry: " + this.place.country + "\n";
//			dump += "\tCountryCode: " + this.place.countryCode + "\n";
//			dump += "\tGeometryType: " + this.place.geometryType + "\n";
//			dump += "\tPlaceType: " + this.place.placeType + "\n";
//			dump += "\tStreetAddress: " + this.place.streetAddress + "\n";
//			dump += "\tURL: " + this.place.url + "\n";
//		}
//
//		dump += "Tweet.geolocation: \n";
//		if ( this.geoLocation != null ) {
//			dump += "\tLatitude: " + this.geoLocation.latitude + "\n";
//			dump += "\tLongitude: " + this.geoLocation.longitude + "\n";
//		}
//
//		dump += "Tweet.user: \n";
//		dump += "\t ID: " + this.user.id + "\n";
//		dump += "\t registrationDate: " + this.user.registrationDate + "\n";
//		dump += "\t name: " + this.user.name + "\n";
//		dump += "\t screenName: " + this.user.screenName + "\n";
//		dump += "\t tweetsCount: " + this.user.tweetsCount + "\n";
//		dump += "\t followersCount: " + this.user.followersCount + "\n";
//		dump += "\t friendsCount: " + this.user.friendsCount + "\n";
//		dump += "\t favouritesCount: " + this.user.favouritesCount + "\n";
//		dump += "\t listedCount: " + this.user.listedCount + "\n";
//		dump += "\t description: " + this.user.description + "\n";
//		dump += "\t url: " + this.user.url + "\n";
//		dump += "\t profileImageURL: " + this.user.profileImageURL + "\n";
//		dump += "\t biggerProfileImageURL: " + this.user.biggerProfileImageURL + "\n";
//		dump += "\t originalProfileImageURL: " + this.user.originalProfileImageURL + "\n";
//		dump += "\t lang: " + this.user.lang + "\n";
//		dump += "\t location: " + this.user.location + "\n";
//		dump += "\t timezone: " + this.user.timeZone + "\n";
//		dump += "\t isContributorsEnabled: " + this.user.isContributorsEnabled + "\n";
//		dump += "\t isGeoEnabled: " + this.user.isGeoEnabled + "\n";
//		dump += "\t isProfileBackgroundTiled: " + this.user.isProfileBackgroundTiled + "\n";
//		dump += "\t isProfileUseBackgroundImage: " + this.user.isProfileUseBackgroundImage + "\n";
//		dump += "\t isProtected: " + this.user.isProtected + "\n";
//		dump += "\t isShowAllInlineMedia: " + this.user.isShowAllInlineMedia + "\n";
//		dump += "\t isTranslator: " + this.user.isTranslator + "\n";
//		dump += "\t isVerified: " + this.user.isVerified + "\n";
//		dump += "\t profileBackgroundColor: " + this.user.profileBackgroundColor + "\n";
//		dump += "\t profileBannerURL: " + this.user.profileBannerURL + "\n";
//		dump += "\t profileLinkColor: " + this.user.profileLinkColor + "\n";
//		dump += "\t profileSidebarFillColor: " + this.user.profileSidebarFillColor + "\n";
//		dump += "\t profileSidebarBorderColor: " + this.user.profileSidebarBorderColor + "\n";
//		dump += "\t profileTextColor: " + this.user.profileTextColor + "\n";
//		dump += "\t DescriptionURLs: \n";
//		if ( this.user.descriptionURLs != null ) {
//			for ( String url : this.user.descriptionURLs ) {
//				dump += "\t\t" + url + "\n";
//			}
//		}
//
//		dump += "Tweet.isFavorited: " + this.isFavorited + "\n";
//		dump += "Tweet.isPossiblySensitive: " + this.isPossiblySensitive + "\n";
//		dump += "Tweet.isRetweet: " + this.isRetweet + "\n";
//		dump += "Tweet.isRetweetedByMe: " + this.isRetweetedByMe + "\n";
//		dump += "Tweet.isTruncated: " + this.isTruncated + "\n";
//
//		dump += "Tweet.contributors: \n";
//		if ( this.contributors != null ) {
//			for ( long cont : this.contributors ) {
//				dump += "\tContributor: " + cont + "\n";
//			}
//		}
//		dump += "------------------------------\n";
//
//		return dump;
	}
	
	public String[] getTextTokens() {
		if ( getTokenedText().isEmpty() ) 
			return new String[0];
		return getTokenedText().split(" ");
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getStatus_id() {
		return status_id;
	}

	public void setStatus_id(long status_id) {
		this.status_id = status_id;
	}

	public Timestamp getHarvestingDate() {
		return harvestingDate;
	}

	public void setHarvestingDate(Timestamp harvestingDate) {
		this.harvestingDate = harvestingDate;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTokenedText() {
		return Preprocessor.preprocessor.getTokensAsText(tokensList);
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public String getInReplyToScreenName() {
		return inReplyToScreenName;
	}

	public void setInReplyToScreenName(String inReplyToScreenName) {
		this.inReplyToScreenName = inReplyToScreenName;
	}

	public long getInReplyToStatusId() {
		return inReplyToStatusId;
	}

	public void setInReplyToStatusId(long inReplyToStatusId) {
		this.inReplyToStatusId = inReplyToStatusId;
	}

	public long getInReplyToUserId() {
		return inReplyToUserId;
	}

	public void setInReplyToUserId(long inReplyToUserId) {
		this.inReplyToUserId = inReplyToUserId;
	}

	public long getRetweetCount() {
		return retweetCount;
	}

	public void setRetweetCount(long retweetCount) {
		this.retweetCount = retweetCount;
	}

	public long getCurrentUserRetweetId() {
		return currentUserRetweetId;
	}

	public void setCurrentUserRetweetId(long currentUserRetweetId) {
		this.currentUserRetweetId = currentUserRetweetId;
	}

	public String[] getHashTags() {
		return hashTags;
	}

	public void setHashTags(String[] hashTags) {
		this.hashTags = hashTags;
	}

	public MediaEntity[] getMediaEntities() {
		return mediaEntities;
	}

	public void setMediaEntities(MediaEntity[] mediaEntities) {
		this.mediaEntities = mediaEntities;
	}

	public String[] getURLs() {
		return URLs;
	}

	public void setURLs(String[] uRLs) {
		URLs = uRLs;
	}

	public UserMentionEntity[] getUserMentionEntites() {
		return userMentionEntites;
	}

	public void setUserMentionEntites(UserMentionEntity[] userMentionEntites) {
		this.userMentionEntites = userMentionEntites;
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	public GeoLocation getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(GeoLocation geoLocation) {
		this.geoLocation = geoLocation;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public long[] getContributors() {
		return contributors;
	}

	public void setContributors(long[] contributors) {
		this.contributors = contributors;
	}

	public boolean isFavorited() {
		return isFavorited;
	}

	public void setFavorited(boolean isFavorited) {
		this.isFavorited = isFavorited;
	}

	public boolean isPossiblySensitive() {
		return isPossiblySensitive;
	}

	public void setPossiblySensitive(boolean isPossiblySensitive) {
		this.isPossiblySensitive = isPossiblySensitive;
	}

	public boolean isRetweet() {
		return isRetweet;
	}

	public void setRetweet(boolean isRetweet) {
		this.isRetweet = isRetweet;
	}

	public boolean isRetweetedByMe() {
		return isRetweetedByMe;
	}

	public void setRetweetedByMe(boolean isRetweetedByMe) {
		this.isRetweetedByMe = isRetweetedByMe;
	}

	public boolean isTruncated() {
		return isTruncated;
	}

	public void setTruncated(boolean isTruncated) {
		this.isTruncated = isTruncated;
	}

	public List<Token> getTokensList() {
		return tokensList;
	}

	public void setTokensList(List<Token> tokensList) {
		this.tokensList = tokensList;
	}

	@Override
	public int compareTo(Tweet o) {
		if ( createdAt.getTime() > o.getCreatedAt().getTime() )
			return 1;
		else if( createdAt.getTime() < o.getCreatedAt().getTime() )
			return -1;
		else return 0;
	}

	public String[] getLocations() {
		return locations;
	}

	public void setLocations(String[] locations) {
		this.locations = locations;
	}

	public List<String> getSimpleTokens() {
		return simpleTokens;
	}

	public void setSimpleTokens(List<String> simpleTokens) {
		this.simpleTokens = simpleTokens;
	}
	
	
	
}


