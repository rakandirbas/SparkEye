package de.uni.konstanz.gui.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

import de.uni.konstanz.dao.CSVDao;
import de.uni.konstanz.models.Tweet;
import de.uni.konstanz.utils.FileUtils;

public class GTweetsProducer implements Runnable {

	private volatile ThreadState state;
	private volatile ThreadState innerState;
	private BlockingQueue<Tweet> buffer;
	private File csvFileLocation;

	private static Logger logger = Logger.getLogger(GTweetsProducer.class);

	public GTweetsProducer( BlockingQueue<Tweet> sharedLocation, File csvFileLocation ) {
		buffer = sharedLocation;
		this.csvFileLocation = csvFileLocation;
		state = ThreadState.RUNNING;
	}

	@Override
	public void run() {
		BufferedReader reader = FileUtils.getFileReader(csvFileLocation);
		String line = "";
		int i = 0;
		try {
			/*
			 * The first while loop is to keep getting items from the stream
			 */
			while( state != ThreadState.DYING ) {
				line = reader.readLine();
				if ( i == 0) {
					i++;
					continue;
				}
				if ( line != null ) {
					Tweet item;
					if ( line.split("\t").length == 25 ) {
						Tweet tempTweet = CSVDao.fromCSVLineToTweet(line);
						if ( tempTweet != null ) {
							item = tempTweet;;
						}
						else {
							continue;
						}
					}
					else {
						continue;
					}


					/*
					 * The second while loop is to not lose items if the 
					 * thread has to wait, so it process the item when the thread
					 * is running again.
					 */
					while( state != ThreadState.DYING ) {

						if ( state == ThreadState.RUNNING ) {
							//Check to see if buffer has free space
							boolean freeBuffer = false;
							synchronized (buffer) {
								freeBuffer = buffer.offer(item);
							}

							while ( (!freeBuffer) && (state == ThreadState.RUNNING)) {
								//if it doesn't, then wait...
								synchronized (this) {
									try {
										//System.out.println("Producer is WAIITING for space.");
										innerState = ThreadState.WAITING;
										wait(1);
									} catch (InterruptedException e) {
										//e.printStackTrace();
									}
								}
								//check to see if the buffer has free space now
								synchronized (buffer) {
									freeBuffer = buffer.offer(item);
								}
							}

							if ( (freeBuffer) && (state == ThreadState.RUNNING) ) {
								synchronized (this) {
									innerState = ThreadState.RUNNING;
								}
								//... continue with the stuff if you need
								//...
								//System.out.println(item);
								//..then break
								break;
							}

						}
						else if ( state == ThreadState.WAITING ) {
							synchronized (this) {
								try {
									innerState = ThreadState.WAITING;
									wait();
								} catch (InterruptedException e) {
									//e.printStackTrace();
									//innerState = ThreadState.RUNNING;
								}
							}
						}
					}
				}//when the stream is done.
				else if ( state == ThreadState.WAITING ) {
					synchronized (this) {
						try {
							innerState = ThreadState.WAITING;
							wait();
						} catch (InterruptedException e) {
							//e.printStackTrace();
							if ( state == ThreadState.WAITING )
								innerState = ThreadState.RUNNING;
							else
								innerState = ThreadState.DYING;
						}
					}
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			logger.error("Error while reading line from " +
					"the CSV file in Tweets Producer", e);
		}


		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Error while closing the reader" +
					" in the Tweets Producer", e);
		}

		synchronized (this) {
			innerState = ThreadState.DYING;
		}

	}

	public void pause() {
		state = ThreadState.WAITING;
	}

	public void die() {
		state = ThreadState.DYING;
	}

	public void wakeup() {
		state = ThreadState.RUNNING;
	}

	public ThreadState getState() {
		return state;
	}


	public ThreadState getInnerState() {
		return innerState;
	}

	public boolean isSynched() {
		if ( state == innerState )
			return true;
		else 
			return false;
	}

}
