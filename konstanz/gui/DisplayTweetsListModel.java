package de.uni.konstanz.gui;

import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractListModel;

import de.uni.konstanz.models.Tweet;

public class DisplayTweetsListModel extends AbstractListModel {
	private List<Tweet> tweets;
	
	public DisplayTweetsListModel() {
		tweets = new LinkedList<Tweet>();
	}
	
	public void setTweets(EventResult eventResult) {
		tweets = eventResult.getTweetsList();
		fireContentsChanged(this, 0, (getSize()-1) );
	}

	@Override
	public int getSize() {
		return tweets.size();
	}

	@Override
	public Object getElementAt(int index) {
		return tweets.get(index);
	}
	
	
}
