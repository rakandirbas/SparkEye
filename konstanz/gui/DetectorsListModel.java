package de.uni.konstanz.gui;

import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractListModel;

import de.uni.konstanz.eventdetection.EventDetector;

public class DetectorsListModel extends AbstractListModel {
	
	private List<EventDetector> detectors;
	
	public DetectorsListModel() {
		detectors = new LinkedList<EventDetector>();
	}

	public void addElement(EventDetector detector) {
		detectors.add(detector);
		int index = detectors.indexOf(detector);
		fireIntervalAdded(this, index, index);
	}
	
	public void editElement(EventDetector detector) {
		int index = detectors.indexOf(detector);
		fireContentsChanged(this, index, index);
	}
	
	public void removeElement(EventDetector detector) {
		int index = detectors.indexOf(detector);
		detectors.remove(index);
		fireIntervalRemoved(this, index, index);
	}
	
	@Override
	public int getSize() {
		return detectors.size();
	}

	public EventDetector getElementAt(int index) {
		return detectors.get(index);
	}
	
	public int getElementIndex(EventDetector detector) {
		return detectors.indexOf(detector);
	}
	
}
