package de.uni.konstanz.server;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

import de.uni.konstanz.dao.CSVDao;
import de.uni.konstanz.models.Tweet;
import de.uni.konstanz.utils.FileUtils;

public class TweetsProducer implements Runnable {
	
	private final BlockingQueue<Tweet> buffer;
	private final File csvFileLocation;
	private CountingBuffer counter = new CountingBuffer();
	
	private static Logger logger = Logger.getLogger(TweetsProducer.class);
	
	public TweetsProducer( BlockingQueue<Tweet> sharedLocation, File csvFileLocation ) {
		buffer = sharedLocation;
		this.csvFileLocation = csvFileLocation;
	}
	
	class CountingBuffer {
		private int counter = 0;
		public synchronized void update() {
			counter++;
		}
		
		public synchronized int reset() {
			int temp = counter;
			counter = 0;
			return temp;
		}
	}
	
	class MinutesTimerTask extends TimerTask {
		CountingBuffer buffer;
		BlockingQueue<Tweet> queue;
		public MinutesTimerTask( CountingBuffer buffer, BlockingQueue<Tweet> queue ) {
			this.buffer = buffer;
			this.queue = queue;
		}
		
		@Override
		public void run() {
			int numTweets = buffer.reset();
			System.out.println("#Tweets/Min: " + numTweets);
			System.out.println( "Queue size/Min: " + queue.size() );
		}
		
	}

	@Override
	public void run() {
		Timer timer = new Timer();
		TimerTask timerTask = new MinutesTimerTask(counter, buffer);
		long minute = 1000 * 60;
		timer.schedule(timerTask, minute, minute);
		
		BufferedReader reader = FileUtils.getFileReader(csvFileLocation);
		try {
			String line = "";

			for ( int i = 0; ((line = reader.readLine()) != null); i++ ) {

				if ( i == 0)
					continue;
				if ( line.split("\t").length == 25 ) {
					buffer.put(CSVDao.fromCSVLineToTweet(line));
					counter.update();
				}
				/* This to act as a delay to get almost the same number
				 * of tweets per minute as of twitter stream.
				 * 
				 */
				//Thread.sleep(2);
			}

		}
		catch ( InterruptedException e ) {
			e.printStackTrace();
			logger.error("The producer thread got interrupted", e);
		}
		catch (IOException e) {
			e.printStackTrace();
			
			logger.error("Error while reading line from " +
					"the CSV file in Tweets Producer", e);
		}
		finally {
			try {
				reader.close();
				timer.cancel();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("Error while closing the reader" +
						" in the Tweets Producer", e);
			}
		}
		
	}

}
