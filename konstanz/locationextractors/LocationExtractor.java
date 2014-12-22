package de.uni.konstanz.locationextractors;

import java.util.List;
import java.util.Set;

import de.uni.konstanz.models.Tweet;

public interface LocationExtractor {
	public List<String> getLocations(Tweet tweet);
	public List<String> getLocations(String text);
	public Set<String> getUniqueLocations(Tweet tweet);
	public Set<String> getUniqueLocations(String text);
}
