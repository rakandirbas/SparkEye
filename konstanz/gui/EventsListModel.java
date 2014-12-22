package de.uni.konstanz.gui;

import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractListModel;

public class EventsListModel extends AbstractListModel {
	private List<EventResult> eventResults;
	
	public EventsListModel() {
		this.eventResults = new LinkedList<EventResult>();
	}
	
	public void setEventResults( List<EventResult> newEventResults ) {
		eventResults.clear();
		eventResults.addAll(newEventResults);
		fireContentsChanged(this, 0, (getSize()-1) );
	}

	@Override
	public int getSize() {
		return eventResults.size();
	}

	@Override
	public Object getElementAt(int index) {
		return eventResults.get(index);
	}
}
