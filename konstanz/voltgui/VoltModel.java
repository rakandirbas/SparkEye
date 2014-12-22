package de.uni.konstanz.voltgui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.swing.event.SwingPropertyChangeSupport;

import com.rakblog.classification.utils.GeneralClassifier;

import de.uni.konstanz.models.VoltTweet;
import de.uni.konstanz.preprocessing.Filter;
import de.uni.konstanz.preprocessing.FilterChain;
import de.uni.konstanz.preprocessing.KeywordFilter;


public class VoltModel implements PropertyChangeListener {
	private SwingPropertyChangeSupport changeFirer;
	private SwingPropertyChangeSupport incomingTweetFirer;
	private File streamFile;
	private List<GeneralClassifier> classifersList;
	private VoltSwingStreamWorker voltSwingStream;
	private FilterChain filtersChain;
	private List<Filter> filters;
	private List<VoltTweet> tweetsList;

	public VoltModel() {
		changeFirer = new SwingPropertyChangeSupport(this);
		incomingTweetFirer = new SwingPropertyChangeSupport(this);
		incomingTweetFirer.addPropertyChangeListener(this);
		classifersList = new ArrayList<GeneralClassifier>();
		filters = new ArrayList<Filter>();
		filtersChain = new FilterChain(filters);
		tweetsList = new ArrayList<VoltTweet>();
	}

	
	public void setSearchQuery(String query) {
		if ( !query.isEmpty() ) {
			Set<String> keywords = new LinkedHashSet<String>();
			for ( String word : query.toLowerCase().split(",") ) {
				keywords.add(word);
			}
			filters.clear();
			filters.add( new KeywordFilter(keywords) );
		}
	}
	
	public void startStream() {
		voltSwingStream = new 
				VoltSwingStreamWorker(getStreamFile(), getFiltersChain(), incomingTweetFirer);
		voltSwingStream.execute();
	}
	
	public void stopStream() {
		voltSwingStream.cancel(true);
	}

	public File getStreamFile() {
		return streamFile;
	}

	public void setStreamFile(File streamFile) {
		this.streamFile = streamFile;
	}

	public SwingPropertyChangeSupport getChangeFirer() {
		return changeFirer;
	}

	public void setChangeFirer(SwingPropertyChangeSupport changeFirer) {
		this.changeFirer = changeFirer;
	}


	public List<GeneralClassifier> getClassifersList() {
		return classifersList;
	}


	public void setClassifersList(List<GeneralClassifier> classifersList) {
		this.classifersList = classifersList;
	}


	public FilterChain getFiltersChain() {
		return filtersChain;
	}


	public void setFiltersChain(FilterChain filtersChain) {
		this.filtersChain = filtersChain;
	}

	public List<Filter> getFilters() {
		return filters;
	}


	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}


	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		VoltTweet tweet = (VoltTweet) evt.getNewValue();
		tweetsList.add(tweet);
		changeFirer.firePropertyChange("newTweet", null, new TweetResult(tweet));
	}
	

}
