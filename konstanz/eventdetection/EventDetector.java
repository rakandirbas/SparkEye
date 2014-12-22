package de.uni.konstanz.eventdetection;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uni.konstanz.eventdescriptors.EventDescriptor;
import de.uni.konstanz.eventdescriptors.EventDescriptorType;
import de.uni.konstanz.utils.IDGenerator;

public class EventDetector {
	private long id;
	private String name;
	private Color color;
	
	//Holds the descriptors types of this detectors and their classification scores 
	private Map<EventDescriptorType, DescriptorBounds> descriptorsTypes;
	private List<Event> events;
	
	public EventDetector( String name, Color color, Map<EventDescriptorType, DescriptorBounds> descriptorsTypes ) {
		this.name = name;
		this.color = color;
		this.descriptorsTypes = descriptorsTypes;
		id = IDGenerator.getID();
		events = new LinkedList<Event>();
	}
	
	public void updateEventCandidates( Map<Long, Event> globalCandidatesMap ) {
		List<Event> updatedEvents = new LinkedList<Event>();
		for ( Long eventID : globalCandidatesMap.keySet() ) {
			Event eventCandidate = globalCandidatesMap.get(eventID);
			double score = 0;
			for ( EventDescriptorType descriptorType : descriptorsTypes.keySet() ) {
				EventDescriptor eventDescriptor = 
						eventCandidate.getDescriptorsMap().get(descriptorType);
				DescriptorBounds descriptorBounds = descriptorsTypes.get(descriptorType);
				if ( eventDescriptor.isWithinRange(descriptorBounds.getLowValue(), descriptorBounds.getHighValue()) )
					score += 1;
			}
			
			score = (double) score / descriptorsTypes.size();
			if ( score == 1.0 ) {
				updatedEvents.add(eventCandidate);
			}
		}
		events = updatedEvents;
		
	}
	
	
	public List<Event> getEvents() {
		return events;
	}
	
	public Map<EventDescriptorType, DescriptorBounds> getDescriptorsTypes() {
		return descriptorsTypes;
	}

	public void setDescriptorsTypes(Map<EventDescriptorType, DescriptorBounds> descriptorsTypes) {
		this.descriptorsTypes = descriptorsTypes;
	}

	public long getId() {
		return id;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public String toString() {
		return name;
	}
	
}
