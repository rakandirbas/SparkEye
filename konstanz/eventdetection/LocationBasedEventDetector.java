package de.uni.konstanz.eventdetection;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import de.uni.konstanz.locationextractors.LocationExtractor;
import de.uni.konstanz.locationextractors.NERBasedLocationExtractor;
import de.uni.konstanz.models.Cluster;
import de.uni.konstanz.models.Tweet;

public class LocationBasedEventDetector {
	private Map<String, Cluster> clusters;
	private LocationExtractor locationExtractor;
	
	public LocationBasedEventDetector() {
		clusters = new LinkedHashMap<String, Cluster>();
		//locationExtractor = new TableBasedLocationExtractor();
		locationExtractor = new NERBasedLocationExtractor();
	}
	
	public Map<String, Cluster> detect( List<Tweet> tweetsList ) {
		for ( Tweet tweet : tweetsList ) {
			List<String> locations = locationExtractor.getLocations(tweet);
			
//			if ( !locations.isEmpty() ) {
//				System.out.println(tweet.getText());
//				System.out.println(locations);
//				System.out.println("-------------");
//			}
			
			for ( String loc : locations ) {
				if ( clusters.containsKey(loc) ) {
					Cluster c = clusters.get(loc);
					c.addDocumentToCluster(tweet);
				}
				else {
					Cluster c = new Cluster();
					c.addDocumentToCluster(tweet);
					clusters.put(loc, c);
				}
			}
			
		}
		
		return getClusters();
	}
	
	/**
	 * Returns only tweets that contain a location.
	 * @param tweets
	 * @return
	 */
	public List<Tweet> filterTweets( List<Tweet> tweets )  {
		List<Tweet> filteredTweets = new LinkedList<Tweet>();
		
		for ( Tweet tweet : tweets ) {
			List<String> locations = locationExtractor.getLocations(tweet);
			if ( !locations.isEmpty() )
				filteredTweets.add(tweet);
		}
		
		return filteredTweets;
	}

	public Map<String, Cluster> getClusters() {
		return clusters;
	}
	
	
}
