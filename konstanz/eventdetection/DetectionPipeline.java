package de.uni.konstanz.eventdetection;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.uni.konstanz.analysis.ClustersAnalyzer;
import de.uni.konstanz.eventdescriptors.ActiveUsersSupportEventDescriptor;
import de.uni.konstanz.eventdescriptors.ConversationalEventDescriptor;
import de.uni.konstanz.eventdescriptors.DescribedUsersSupportEventDescriptor;
import de.uni.konstanz.eventdescriptors.DifferentUsersEventDescriptor;
import de.uni.konstanz.eventdescriptors.EmoticonsEventDescriptor;
import de.uni.konstanz.eventdescriptors.EventDescriptor;
import de.uni.konstanz.eventdescriptors.EventDescriptorType;
import de.uni.konstanz.eventdescriptors.GeoMetaDensityEventDescriptor;
import de.uni.konstanz.eventdescriptors.HashtagsDensityEventDescriptor;
import de.uni.konstanz.eventdescriptors.IntensificationEventDescriptor;
import de.uni.konstanz.eventdescriptors.MobileSourceEventDescriptor;
import de.uni.konstanz.eventdescriptors.NearUsersByTimeZoneEventDescriptor;
import de.uni.konstanz.eventdescriptors.NegativeSentimentEventDescriptor;
import de.uni.konstanz.eventdescriptors.NeutralSentimentEventDescriptor;
import de.uni.konstanz.eventdescriptors.ObjectivityEventDescriptor;
import de.uni.konstanz.eventdescriptors.PastEventDescriptor;
import de.uni.konstanz.eventdescriptors.PopularUsersSupportEventDescriptor;
import de.uni.konstanz.eventdescriptors.PopularityComputer;
import de.uni.konstanz.eventdescriptors.PopularityEventDescriptor;
import de.uni.konstanz.eventdescriptors.PositiveSentimentEventDescriptor;
import de.uni.konstanz.eventdescriptors.PresencyEventDescriptor;
import de.uni.konstanz.eventdescriptors.QuestioningEventDescriptor;
import de.uni.konstanz.eventdescriptors.RetweetDensityEventDescriptor;
import de.uni.konstanz.eventdescriptors.SentimentComputer;
import de.uni.konstanz.eventdescriptors.SlangEventDescriptor;
import de.uni.konstanz.eventdescriptors.SwearWordsEventDescriptor;
import de.uni.konstanz.eventdescriptors.TextualLocationSupportEventDescriptor;
import de.uni.konstanz.eventdescriptors.TweetLengthEventDescriptor;
import de.uni.konstanz.eventdescriptors.URLDensityEventDescriptor;
import de.uni.konstanz.eventdescriptors.WeirdCharsSaturationEventDescriptor;
import de.uni.konstanz.models.Cluster;
import de.uni.konstanz.models.Tweet;
import de.uni.konstanz.utils.ClustersUtils;

public class DetectionPipeline {
	private Map<Long, EventDetector> eventsDetectors;
	private Map<Long, EventDetector> nonEventsDetectors;
	private Map<Long, Event> globalEventsMap;
	private double globalClassificationThreshold;
	
	private static SentimentComputer sentComputer = new SentimentComputer();
	private static PopularityComputer popComputer = new PopularityComputer();
	public static double classificationThreshold = 0.5;
	
	public DetectionPipeline(double globalClassificationThreshold) {
		eventsDetectors = new LinkedHashMap<Long, EventDetector>();
		nonEventsDetectors = new LinkedHashMap<Long, EventDetector>();
		globalEventsMap = new LinkedHashMap<Long, Event>();
		this.globalClassificationThreshold = globalClassificationThreshold;
	}
	
	public void updateDescriptorsMap( Map<Long, ? extends Cluster> clusters,
			ClustersAnalyzer clusterAnalyzer,
			Map<Long, Tweet> tweets) {
		
		popComputer.computeAndUpdateScores(clusters);
		Map<Long, Event> updatedGlobalEventsMap = 
				new LinkedHashMap<Long, Event>();
		for ( Long clusterID : clusters.keySet() ) {
			Cluster cluster = clusters.get(clusterID);
			List<Tweet> clusterTweets =
					ClustersUtils.getClusterTweets(cluster, tweets);
			Map<EventDescriptorType, EventDescriptor> descriptorsMap = 
					new LinkedHashMap<EventDescriptorType, EventDescriptor>();
			
			//NOW CREATE descriptors, pass the clusterTweets to them
			//and put them in the descriptorsMap
			EventDescriptor popularity = new PopularityEventDescriptor(clusterTweets, classificationThreshold);
			//popularity.setScore( clusterAnalyzer.getPopularityThreshold(clusterID) );
			popularity.setScore(popComputer.getScore(cluster.getClusterSize()));
			descriptorsMap.put(EventDescriptorType.POPULAR, popularity);
			
			sentComputer.computeAndUpdateScores(clusterTweets);
			EventDescriptor negative_sent = new NegativeSentimentEventDescriptor(clusterTweets, classificationThreshold);
			negative_sent.setScore( sentComputer.getNegativeSentimentScore() );
			descriptorsMap.put(EventDescriptorType.NEGATIVE_SENTIMENT, negative_sent);
			
			EventDescriptor positive_sent = new PositiveSentimentEventDescriptor(clusterTweets, classificationThreshold);
			positive_sent.setScore( sentComputer.getPositiveSentimentScore() );
			descriptorsMap.put(EventDescriptorType.POSITIVE_SENTIMENT, positive_sent);
			
			EventDescriptor neutral_sent = new NeutralSentimentEventDescriptor(clusterTweets, classificationThreshold);
			neutral_sent.setScore( sentComputer.getNeutralSentimentScore() );
			descriptorsMap.put(EventDescriptorType.NEUTRAL_SENTIMENT, neutral_sent);
			
			EventDescriptor conversational = 
					new ConversationalEventDescriptor(clusterTweets, classificationThreshold);
			descriptorsMap.put( EventDescriptorType.CONVERSATIONAL , conversational);
			
			EventDescriptor url_density = 
					new URLDensityEventDescriptor(clusterTweets, classificationThreshold);
			descriptorsMap.put(EventDescriptorType.URL_DENSITY, url_density);
			
			EventDescriptor hashtag_density = 
					new HashtagsDensityEventDescriptor(clusterTweets, classificationThreshold);
			descriptorsMap.put(EventDescriptorType.HASHTAG_DENSITY, hashtag_density);
			
			EventDescriptor objectivity = 
					new ObjectivityEventDescriptor(clusterTweets, classificationThreshold);
			descriptorsMap.put(EventDescriptorType.OBJECTIVITY, objectivity);
			
			EventDescriptor presence = 
					new PresencyEventDescriptor(clusterTweets, classificationThreshold);
			descriptorsMap.put(EventDescriptorType.PRESENT_ORRIENTED, presence);
			
			EventDescriptor past = 
					new PastEventDescriptor(clusterTweets, classificationThreshold);
			descriptorsMap.put(EventDescriptorType.PAST_ORIENTED, past);
			
			EventDescriptor locationSupport = 
					new TextualLocationSupportEventDescriptor(clusterTweets, classificationThreshold);
			descriptorsMap.put(EventDescriptorType.LOCATION_SUPPORT, locationSupport);
			
			EventDescriptor retweetDensity = 
					new RetweetDensityEventDescriptor(clusterTweets, classificationThreshold);
			descriptorsMap.put(EventDescriptorType.RETWEET_DENSTIY, retweetDensity);
			
			EventDescriptor mobileDensity = 
					new MobileSourceEventDescriptor(clusterTweets, classificationThreshold);
			descriptorsMap.put(EventDescriptorType.MOBILE_SUPPORT, mobileDensity);
			
			EventDescriptor differentUsers = 
					new DifferentUsersEventDescriptor(clusterTweets, classificationThreshold);
			descriptorsMap.put(EventDescriptorType.DIFFERENT_USERS, differentUsers);
			
			EventDescriptor weirdChars = 
					new WeirdCharsSaturationEventDescriptor(clusterTweets, classificationThreshold);
			descriptorsMap.put(EventDescriptorType.WEIRD_CHARS_SATURATION, weirdChars);
			
			EventDescriptor questioning = 
					new QuestioningEventDescriptor(clusterTweets, classificationThreshold);
			descriptorsMap.put(EventDescriptorType.QUESTIONING, questioning);
			
			EventDescriptor emoticons_sat = new
					EmoticonsEventDescriptor(clusterTweets, classificationThreshold);
			descriptorsMap.put(EventDescriptorType.EMOTICONS_SATURATION, emoticons_sat);
			
			
			EventDescriptor swearingWords = new
					SwearWordsEventDescriptor(clusterTweets, classificationThreshold);
			descriptorsMap.put(EventDescriptorType.SWEARING_WORDS_SATURATION, swearingWords);
			
			EventDescriptor slangWords = new 
					SlangEventDescriptor(clusterTweets, classificationThreshold);
			descriptorsMap.put(EventDescriptorType.SLANGS_SATURATION, slangWords);
			
			EventDescriptor intensification = 
					new IntensificationEventDescriptor(clusterTweets, classificationThreshold);
			descriptorsMap.put(EventDescriptorType.INTENSIFICATION, intensification);
			
			EventDescriptor geo_meta = new
					GeoMetaDensityEventDescriptor(clusterTweets, classificationThreshold);
			descriptorsMap.put(EventDescriptorType.GEO_META_SUPPORT, geo_meta);
			
			EventDescriptor tweetLength = 
					new TweetLengthEventDescriptor(clusterTweets, classificationThreshold);
			descriptorsMap.put(EventDescriptorType.TWEETS_LENGTH, tweetLength);
			
			EventDescriptor popular_users = 
					new PopularUsersSupportEventDescriptor(clusterTweets, classificationThreshold);
			descriptorsMap.put(EventDescriptorType.POPULAR_USERS_SUPPORT, popular_users);
			
			EventDescriptor activeUsers = 
					new ActiveUsersSupportEventDescriptor(clusterTweets, classificationThreshold);
			descriptorsMap.put(EventDescriptorType.ACTIVE_USERS_SUPPORT, activeUsers);
			
			EventDescriptor describedUsers = 
					new DescribedUsersSupportEventDescriptor(clusterTweets, classificationThreshold);
			descriptorsMap.put(EventDescriptorType.DESCRIBED_USERS_SUPPORT, describedUsers);
			
			EventDescriptor near_users = new
					NearUsersByTimeZoneEventDescriptor(clusterTweets, classificationThreshold);
			descriptorsMap.put(EventDescriptorType.NEAR_USERS_SUPPORT, near_users);
			
			//
			Event eventCandidate = new Event( clusterID, descriptorsMap );
			updatedGlobalEventsMap.put(clusterID, eventCandidate);
		}
		
		setGlobalEventsMap(updatedGlobalEventsMap);
	}
	
	public void updateDetectors() {
		
		if ( !getGlobalEventsMap().isEmpty() ) {
			for ( Long detectorID : eventsDetectors.keySet() ) {
				EventDetector detector = eventsDetectors.get(detectorID);
				detector.updateEventCandidates(getGlobalEventsMap());
			}
		}
		
	}
	
	public void updateDetector( Long eventDetectorID ) {
		EventDetector detector = eventsDetectors.get(eventDetectorID);
		detector.updateEventCandidates(getGlobalEventsMap());
	}

	public Map<Long, EventDetector> getEventsDetectors() {
		return eventsDetectors;
	}

	public void setEventsDetectors(Map<Long, EventDetector> eventsDetectors) {
		this.eventsDetectors = eventsDetectors;
	}

	public Map<Long, EventDetector> getNonEventsDetectors() {
		return nonEventsDetectors;
	}

	public void setNonEventsDetectors(Map<Long, EventDetector> nonEventsDetectors) {
		this.nonEventsDetectors = nonEventsDetectors;
	}

	public Map<Long, Event> getGlobalEventsMap() {
		return globalEventsMap;
	}

	public void setGlobalEventsMap(Map<Long, Event> globalEventsMap) {
		this.globalEventsMap = globalEventsMap;
	}

	public double getGlobalClassificationThreshold() {
		return globalClassificationThreshold;
	}

	public void setGlobalClassificationThreshold(
			double globalClassificationThreshold) {
		this.globalClassificationThreshold = globalClassificationThreshold;
	}
	
	public Event getEvent(long eventID) {
		return globalEventsMap.get(eventID);
	}
	
}



















