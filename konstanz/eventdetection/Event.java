package de.uni.konstanz.eventdetection;

import java.util.Map;

import de.uni.konstanz.eventdescriptors.EventDescriptor;
import de.uni.konstanz.eventdescriptors.EventDescriptorType;

public class Event {
	
	private long id;
	private Map<EventDescriptorType, EventDescriptor> descriptorsMap;
	private double aggregateScore;
	
	public Event(long id, Map<EventDescriptorType, EventDescriptor> descriptorsMap) {
		this.id = id;
		this.descriptorsMap = descriptorsMap;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Map<EventDescriptorType, EventDescriptor> getDescriptorsMap() {
		return descriptorsMap;
	}

	public void setDescriptorsMap(Map<EventDescriptorType, EventDescriptor> descriptorsMap) {
		this.descriptorsMap = descriptorsMap;
	}

	public double getAggregateScore() {
		return aggregateScore;
	}

	public void setAggregateScore(double aggregateScore) {
		this.aggregateScore = aggregateScore;
	}
	

}
