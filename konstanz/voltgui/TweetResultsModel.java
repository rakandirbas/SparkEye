package de.uni.konstanz.voltgui;

import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractListModel;

public class TweetResultsModel extends AbstractListModel {
	private List<TweetResult> results;
	
	public TweetResultsModel() {
		this.results = new LinkedList<TweetResult>();
	}
	
	public void addElement(TweetResult result) {
		results.add(result);
		int index = results.indexOf(result);
		fireIntervalAdded(this, index, index);
	}
	
	public void editElement(TweetResult result) {
		int index = results.indexOf(result);
		fireContentsChanged(this, index, index);
	}
	
	public void removeElement(TweetResult result) {
		int index = results.indexOf(result);
		results.remove(index);
		fireIntervalRemoved(this, index, index);
	}
	
	@Override
	public int getSize() {
		return results.size();
	}

	public TweetResult getElementAt(int index) {
		return results.get(index);
	}
	
	public int getElementIndex(TweetResult result) {
		return results.indexOf(result);
	}
}
