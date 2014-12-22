package de.uni.konstanz.eventdescriptors;

import java.util.List;

import de.uni.konstanz.models.Tweet;

public abstract class EventDescriptor {
	EventDescriptorType type;
	private double classificationThreshold;
	private double score;
	private boolean negation;

	public EventDescriptor(List<Tweet> tweets, double classificationThreshold) {
		double score = computeScore(tweets);
		this.score = score;
		this.classificationThreshold = classificationThreshold;
	}
	
	/**
	 * This constructor acts to make a copy of another EventDescriptor
	 * See https://bitly.com/ZE7Xv
	 * @param descriptor
	 */
//	public EventDescriptor(EventDescriptor descriptor) {
//		this.classificationThreshold = descriptor.getThreshold();
//		this.score = descriptor.getScore();
//		this.type = descriptor.getType();
//	}
	
	public abstract double computeScore(List<Tweet> tweets);
	public boolean isAboveThreshold() {
		if ( score > classificationThreshold )
			return true;
		else 
			return false;
	}
	
//	public boolean isAboveThreshold(double classificationThreshold) {
//		if ( score > classificationThreshold )
//			return true;
//		else 
//			return false;
//	}
	
	public boolean isWithinRange(double lowVal, double highVal) {
		if ( score >= lowVal && score <= highVal ) 
			return true;
		else
			return false;
	}
	
	public double getThreshold() {
		return classificationThreshold;
	}
	public void setThreshold(double threshold) {
		this.classificationThreshold = threshold;
	}
	public double getScore() {
		return score;
	}

	public EventDescriptorType getType() {
		return type;
	}

	public void setType(EventDescriptorType type) {
		this.type = type;
	}

	public void setScore(double score) {
		this.score = score;
	}
	
	public String toString() {
		return "EventDescrpitor";
	}
	
}
