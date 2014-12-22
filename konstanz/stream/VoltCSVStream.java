package de.uni.konstanz.stream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import de.uni.konstanz.analysis.VoltTextProcesser;
import de.uni.konstanz.models.VoltTweet;

public class VoltCSVStream {
	private File file;
	private StreamListener streamListener;
	private BufferedReader reader;
	
	public VoltCSVStream(File csvFile, StreamListener streamListener) throws Exception {
		file = csvFile;
		this.streamListener = streamListener;
	}
	
	public void start() throws Exception {
		reader = new BufferedReader( new FileReader(file) );
		String line = "";
		String headerLine = reader.readLine();
		
		while((line = reader.readLine()) != null) {
			String values[] = line.split("\t");
			if ( values.length == 25 ) {
				VoltTweet tweet = VoltTextProcesser.convertCSVLineToTweet(values);
				streamListener.onReceiving(tweet);
			}
		}
		reader.close();
	}
	
	public void stop() {
		try {
			reader.close();
		} catch (IOException e) {
			//e.printStackTrace();
			System.err.println("The stream is already closed.");
		}
	}
	
}
