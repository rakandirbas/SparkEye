package de.uni.konstanz.models;

import java.sql.Timestamp;

import de.uni.konstanz.utils.IDGenerator;

public class VoltTweet {
	private long id;
	private Timestamp createdAt;
	private String text;
	private String[] tokens;
	private long retweetCount;
	private String[] hashtags;
	private String[] urls;
	private String[] userMentions;
	private double[] geoLocation;
	private boolean retweet, sentFromWeb, sentFromMobile, favorited;
	
	private String userScreenName, userRealName, 
		userDescription, userLocation;
	private Timestamp userRegistrationDate;
	private int userFollowersCount, userFriendsCount, 
	userNumbTweets, userListedCount;
	
	public VoltTweet() {
		this.id = IDGenerator.getID();
		tokens = new String[0];
		hashtags = new String[0];
		urls = new String[0];
		userMentions = new String[0];
		geoLocation = new double[0];
	}
	
	public long getId() {
		return id;
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

	public String[] getTokens() {
		return tokens;
	}

	public void setTokens(String[] tokens) {
		this.tokens = tokens;
	}

	public long getRetweetCount() {
		return retweetCount;
	}

	public void setRetweetCount(long retweetCount) {
		this.retweetCount = retweetCount;
	}

	public String[] getHashtags() {
		return hashtags;
	}

	public void setHashtags(String[] hashtags) {
		this.hashtags = hashtags;
	}

	public String[] getUrls() {
		return urls;
	}

	public void setUrls(String[] urls) {
		this.urls = urls;
	}

	public double[] getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(double[] geoLocation) {
		this.geoLocation = geoLocation;
	}

	public boolean isRetweet() {
		return retweet;
	}

	public void setRetweet(boolean retweet) {
		this.retweet = retweet;
	}

	public boolean isSentFromWeb() {
		return sentFromWeb;
	}

	public void setSentFromWeb(boolean sentFromWeb) {
		this.sentFromWeb = sentFromWeb;
	}

	public boolean isFavorited() {
		return favorited;
	}

	public void setFavorited(boolean favorited) {
		this.favorited = favorited;
	}

	public String getUserScreenName() {
		return userScreenName;
	}

	public void setUserScreenName(String userScreenName) {
		this.userScreenName = userScreenName;
	}

	public String getUserRealName() {
		return userRealName;
	}

	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}

	public Timestamp getUserRegistrationDate() {
		return userRegistrationDate;
	}

	public void setUserRegistrationDate(Timestamp userRegistrationDate) {
		this.userRegistrationDate = userRegistrationDate;
	}

	public int getUserFollowersCount() {
		return userFollowersCount;
	}

	public void setUserFollowersCount(int userFollowersCount) {
		this.userFollowersCount = userFollowersCount;
	}

	public int getUserFriendsCount() {
		return userFriendsCount;
	}

	public void setUserFriendsCount(int userFriendsCount) {
		this.userFriendsCount = userFriendsCount;
	}

	public int getUserNumbTweets() {
		return userNumbTweets;
	}

	public void setUserNumbTweets(int userNumbTweets) {
		this.userNumbTweets = userNumbTweets;
	}

	public int getUserListedCount() {
		return userListedCount;
	}

	public void setUserListedCount(int userListedCount) {
		this.userListedCount = userListedCount;
	}

	public boolean isSentFromMobile() {
		return sentFromMobile;
	}

	public void setSentFromMobile(boolean sentFromMobile) {
		this.sentFromMobile = sentFromMobile;
	}

	public String getUserDescription() {
		return userDescription;
	}

	public void setUserDescription(String userDescription) {
		this.userDescription = userDescription;
	}

	public String getUserLocation() {
		return userLocation;
	}

	public void setUserLocation(String userLocation) {
		this.userLocation = userLocation;
	}

	public String[] getUserMentions() {
		return userMentions;
	}

	public void setUserMentions(String[] userMentions) {
		this.userMentions = userMentions;
	}
	
	
}















