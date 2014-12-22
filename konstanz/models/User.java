package de.uni.konstanz.models;

import java.sql.Timestamp;

public class User {
	long id;
	Timestamp registrationDate;
	String name, screenName, description, 
	lang, location, timeZone, url;
	int followersCount, friendsCount, 
	tweetsCount, favouritesCount, listedCount;
	String descriptionURLs[];//Table

	String originalProfileImageURL, biggerProfileImageURL, 
	profileImageURL, profileBannerURL;  

	String profileBackgroundColor, profileLinkColor, 
	profileSidebarBorderColor, profileSidebarFillColor,  
	profileTextColor;

	boolean isGeoEnabled, isProfileBackgroundTiled, 
	isProfileUseBackgroundImage, isProtected, isContributorsEnabled,
	isShowAllInlineMedia, isTranslator, isVerified;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getFollowersCount() {
		return followersCount;
	}

	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}

	public int getFriendsCount() {
		return friendsCount;
	}

	public void setFriendsCount(int friendsCount) {
		this.friendsCount = friendsCount;
	}

	public int getTweetsCount() {
		return tweetsCount;
	}

	public void setTweetsCount(int tweetsCount) {
		this.tweetsCount = tweetsCount;
	}

	public int getFavouritesCount() {
		return favouritesCount;
	}

	public void setFavouritesCount(int favouritesCount) {
		this.favouritesCount = favouritesCount;
	}

	public String[] getDescriptionURLs() {
		return descriptionURLs;
	}

	public void setDescriptionURLs(String[] descriptionURLs) {
		this.descriptionURLs = descriptionURLs;
	}

	public String getOriginalProfileImageURL() {
		return originalProfileImageURL;
	}

	public void setOriginalProfileImageURL(String originalProfileImageURL) {
		this.originalProfileImageURL = originalProfileImageURL;
	}

	public String getBiggerProfileImageURL() {
		return biggerProfileImageURL;
	}

	public void setBiggerProfileImageURL(String biggerProfileImageURL) {
		this.biggerProfileImageURL = biggerProfileImageURL;
	}

	public String getProfileImageURL() {
		return profileImageURL;
	}

	public void setProfileImageURL(String profileImageURL) {
		this.profileImageURL = profileImageURL;
	}

	public String getProfileBannerURL() {
		return profileBannerURL;
	}

	public void setProfileBannerURL(String profileBannerURL) {
		this.profileBannerURL = profileBannerURL;
	}

	public String getProfileBackgroundColor() {
		return profileBackgroundColor;
	}

	public void setProfileBackgroundColor(String profileBackgroundColor) {
		this.profileBackgroundColor = profileBackgroundColor;
	}

	public String getProfileLinkColor() {
		return profileLinkColor;
	}

	public void setProfileLinkColor(String profileLinkColor) {
		this.profileLinkColor = profileLinkColor;
	}

	public String getProfileSidebarBorderColor() {
		return profileSidebarBorderColor;
	}

	public void setProfileSidebarBorderColor(String profileSidebarBorderColor) {
		this.profileSidebarBorderColor = profileSidebarBorderColor;
	}

	public String getProfileSidebarFillColor() {
		return profileSidebarFillColor;
	}

	public void setProfileSidebarFillColor(String profileSidebarFillColor) {
		this.profileSidebarFillColor = profileSidebarFillColor;
	}

	public String getProfileTextColor() {
		return profileTextColor;
	}

	public void setProfileTextColor(String profileTextColor) {
		this.profileTextColor = profileTextColor;
	}

	public boolean isGeoEnabled() {
		return isGeoEnabled;
	}

	public void setGeoEnabled(boolean isGeoEnabled) {
		this.isGeoEnabled = isGeoEnabled;
	}

	public boolean isProfileBackgroundTiled() {
		return isProfileBackgroundTiled;
	}

	public void setProfileBackgroundTiled(boolean isProfileBackgroundTiled) {
		this.isProfileBackgroundTiled = isProfileBackgroundTiled;
	}

	public boolean isProfileUseBackgroundImage() {
		return isProfileUseBackgroundImage;
	}

	public void setProfileUseBackgroundImage(boolean isProfileUseBackgroundImage) {
		this.isProfileUseBackgroundImage = isProfileUseBackgroundImage;
	}

	public boolean isProtected() {
		return isProtected;
	}

	public void setProtected(boolean isProtected) {
		this.isProtected = isProtected;
	}

	public boolean isContributorsEnabled() {
		return isContributorsEnabled;
	}

	public void setContributorsEnabled(boolean isContributorsEnabled) {
		this.isContributorsEnabled = isContributorsEnabled;
	}

	public boolean isShowAllInlineMedia() {
		return isShowAllInlineMedia;
	}

	public void setShowAllInlineMedia(boolean isShowAllInlineMedia) {
		this.isShowAllInlineMedia = isShowAllInlineMedia;
	}

	public boolean isTranslator() {
		return isTranslator;
	}

	public void setTranslator(boolean isTranslator) {
		this.isTranslator = isTranslator;
	}

	public boolean isVerified() {
		return isVerified;
	}

	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}

	public Timestamp getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Timestamp registrationDate) {
		this.registrationDate = registrationDate;
	}

	public int getListedCount() {
		return listedCount;
	}

	public void setListedCount(int listedCount) {
		this.listedCount = listedCount;
	}

}
