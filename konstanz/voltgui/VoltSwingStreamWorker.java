package de.uni.konstanz.voltgui;

import java.io.File;
import java.util.List;

import javax.swing.SwingWorker;
import javax.swing.event.SwingPropertyChangeSupport;

import de.uni.konstanz.models.VoltTweet;
import de.uni.konstanz.preprocessing.FilterChain;
import de.uni.konstanz.stream.StreamListener;
import de.uni.konstanz.stream.VoltCSVStream;

public class VoltSwingStreamWorker extends SwingWorker<Void, VoltTweet>
	implements StreamListener {
	
	private SwingPropertyChangeSupport changeFirer;
	private VoltCSVStream stream;
	private File file;
	private FilterChain filterChains;
	
	public VoltSwingStreamWorker(File file, FilterChain filterChains, SwingPropertyChangeSupport changeFirer) {
		this.file = file;
		this.changeFirer = changeFirer;
		this.filterChains = filterChains;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		stream = new VoltCSVStream(file, this);
		stream.start();
		return null;
	}

	@Override
	public void onReceiving(VoltTweet tweet) {
		
		if ( !isCancelled()) {
			if ( filterChains.pass(tweet) )
				publish(tweet);
		}
		else {
			stream.stop();
		}
	}

	protected void process(List<VoltTweet> tweetsList) {
		try {
			VoltTweet tweet = tweetsList.remove(0);
			changeFirer.firePropertyChange("newTweet", null, tweet);
		}
		catch (IndexOutOfBoundsException e) {
			//e.printStackTrace();
		}
	}

}
