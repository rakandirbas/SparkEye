package de.uni.konstanz.gui;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import de.uni.konstanz.analysis.FastCosineSimilarityCalculator;
import de.uni.konstanz.dao.TweetsInClustersDao;
import de.uni.konstanz.eventdetection.Event;
import de.uni.konstanz.eventdetection.EventDetector;
import de.uni.konstanz.models.LeaderFollowerCluster;
import de.uni.konstanz.models.Tweet;
import de.uni.konstanz.utils.ClustersUtils;
import de.uni.konstanz.utils.MapUtils;

public class EventResult {
	private Event event;
	//Holds the detectors that detected this event
	private List<EventDetector> eventDetectors;
	private String name;
	private LeaderFollowerCluster cluster;
	private TweetsInClustersDao tweetsDao;
	private Color color;

	public EventResult(Event event, LeaderFollowerCluster cluster,
			List<EventDetector> eventDetectors, TweetsInClustersDao tweetsDao, Color color) {
		this.event = event;
		this.eventDetectors = eventDetectors;
		this.cluster = cluster;
		this.tweetsDao = tweetsDao;
		this.color = color;
		this.name = genereateName(cluster, 3);
	}

	public String genereateName( LeaderFollowerCluster cluster, int numberOfTokens ) {
		String name = "";

		Map<String, Double> weightsMap = cluster.getTfIdf_weightsMap();
		weightsMap = MapUtils.sortByValue(weightsMap, true);

		int counter = 1;

		for ( String term : weightsMap.keySet() ) {
			if (term.equals(FastCosineSimilarityCalculator.NORMALIZATION_ID)) {
				continue;
			}
			if ( counter <= numberOfTokens  ) {
				name = name + "[" +  term + "] ";
				counter++;
			}
			else {
				break;
			}

		}
		name = name.trim();
		return name;
	}

	/**
	 * Returns the list of tweets that belong to this event/cluster
	 * @return
	 */
	public List<Tweet> getTweetsList() {
		return ClustersUtils.getClusterTweets(cluster, tweetsDao.getTweetsInClusters());
	}

	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public Event getEvent() {
		return event;
	}

	public LeaderFollowerCluster getCluster() {
		return cluster;
	}



	public TweetsInClustersDao getTweetsDao() {
		return tweetsDao;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Returns the event detectors that detected this event
	 * @return
	 */
	public List<EventDetector> getEventDetectors() {
		return eventDetectors;
	}
}
