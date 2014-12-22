package de.uni.konstanz.gui;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import de.uni.konstanz.eventdescriptors.EventDescriptor;
import de.uni.konstanz.eventdescriptors.EventDescriptorType;
import de.uni.konstanz.eventdetection.DescriptorBounds;
import de.uni.konstanz.eventdetection.DetectionPipeline;
import de.uni.konstanz.eventdetection.Event;
import de.uni.konstanz.eventdetection.EventDetector;
import de.uni.konstanz.gui.server.AppState;
import de.uni.konstanz.gui.server.GTweetsConsumer;
import de.uni.konstanz.gui.server.GTweetsProducer;
import de.uni.konstanz.models.Cluster;
import de.uni.konstanz.models.LeaderFollowerCluster;
import de.uni.konstanz.models.Tweet;
import de.uni.konstanz.utils.ClustersUtils;
import de.uni.konstanz.utils.FileUtils;

public class GUIController {

	private DetectionPipeline detectionPipeline;
	private ExecutorService executor;
	private BlockingQueue<Tweet> buffer;
	private volatile AppState appState;
	private File csvFileLocation;
	private GTweetsProducer producer;
	private GTweetsConsumer consumer;
	private int burstinessThreshold;

	public static Map<EventDescriptorType, String> eventDescriptorsTypesShortcuts;

	static {
		eventDescriptorsTypesShortcuts = new LinkedHashMap<EventDescriptorType, String>();
		eventDescriptorsTypesShortcuts.put(EventDescriptorType.POPULAR, "1");
		eventDescriptorsTypesShortcuts.put(EventDescriptorType.POSITIVE_SENTIMENT, "2");
		eventDescriptorsTypesShortcuts.put(EventDescriptorType.NEGATIVE_SENTIMENT, "3");
		eventDescriptorsTypesShortcuts.put(EventDescriptorType.NEUTRAL_SENTIMENT, "4");
		eventDescriptorsTypesShortcuts.put(EventDescriptorType.CONVERSATIONAL, "5");
		eventDescriptorsTypesShortcuts.put(EventDescriptorType.URL_DENSITY, "6");
		eventDescriptorsTypesShortcuts.put(EventDescriptorType.HASHTAG_DENSITY, "7");
		eventDescriptorsTypesShortcuts.put(EventDescriptorType.OBJECTIVITY, "8");
		eventDescriptorsTypesShortcuts.put(EventDescriptorType.PRESENT_ORRIENTED, "9");
		eventDescriptorsTypesShortcuts.put(EventDescriptorType.PAST_ORIENTED, "10");
		eventDescriptorsTypesShortcuts.put(EventDescriptorType.LOCATION_SUPPORT, "11");
		eventDescriptorsTypesShortcuts.put(EventDescriptorType.RETWEET_DENSTIY, "12");
		eventDescriptorsTypesShortcuts.put(EventDescriptorType.MOBILE_SUPPORT, "13");
		eventDescriptorsTypesShortcuts.put(EventDescriptorType.DIFFERENT_USERS, "14");
		eventDescriptorsTypesShortcuts.put(EventDescriptorType.WEIRD_CHARS_SATURATION, "15");
		eventDescriptorsTypesShortcuts.put(EventDescriptorType.QUESTIONING, "16");
		eventDescriptorsTypesShortcuts.put(EventDescriptorType.EMOTICONS_SATURATION, "17");
		eventDescriptorsTypesShortcuts.put(EventDescriptorType.SWEARING_WORDS_SATURATION, "18");
		eventDescriptorsTypesShortcuts.put(EventDescriptorType.SLANGS_SATURATION, "19");
		eventDescriptorsTypesShortcuts.put(EventDescriptorType.INTENSIFICATION, "20");
		eventDescriptorsTypesShortcuts.put(EventDescriptorType.GEO_META_SUPPORT, "21");
		eventDescriptorsTypesShortcuts.put(EventDescriptorType.TWEETS_LENGTH, "22");
		eventDescriptorsTypesShortcuts.put(EventDescriptorType.POPULAR_USERS_SUPPORT, "23");
		eventDescriptorsTypesShortcuts.put(EventDescriptorType.ACTIVE_USERS_SUPPORT, "24");
		eventDescriptorsTypesShortcuts.put(EventDescriptorType.DESCRIBED_USERS_SUPPORT, "25");
		eventDescriptorsTypesShortcuts.put(EventDescriptorType.NEAR_USERS_SUPPORT, "26");

	}

	public GUIController(double globalClassificationThreshold) {
		detectionPipeline = new DetectionPipeline(globalClassificationThreshold);
		executor = Executors.newCachedThreadPool();
		buffer = new LinkedBlockingQueue<Tweet>(100000);
		appState = AppState.CLEAN_START;
	}

	public EventDetector newDetector( String detectorName, Color detectorColor, 
			Map<EventDescriptorType, DescriptorBounds> descriptorsTypes ) {
		EventDetector eventDetector = new EventDetector(detectorName, detectorColor, descriptorsTypes);
		detectionPipeline.getEventsDetectors().put(eventDetector.getId(), eventDetector);
		return eventDetector;
	}


	public void removeDetector(Long eventDetectorID) {
		detectionPipeline.getEventsDetectors().remove(eventDetectorID);
	}

	public void removeDetector( EventDetector eventDetector ) {
		removeDetector(eventDetector.getId());
	}

	/**
	 * Returns all events detectors
	 * @return
	 */
	public Map<Long, EventDetector> getEventsDetectors() {
		return detectionPipeline.getEventsDetectors();
	}

	public File getCsvFileLocation() {
		return csvFileLocation;
	}

	public void setCsvFileLocation(File csvFileLocation) {
		this.csvFileLocation = csvFileLocation;
	}

	public void startClustering(int chunkSize, double similarityThreshold, int clusterLivetime) {
		appState = AppState.RUNNING;
		executor = Executors.newCachedThreadPool();
		producer = new GTweetsProducer(buffer, csvFileLocation);
		consumer = new GTweetsConsumer(buffer, chunkSize, similarityThreshold, 10, clusterLivetime);
		executor.execute( producer );
		executor.execute( consumer );
		executor.shutdown();
	}

	public void continueClustering() {
		appState = AppState.RUNNING;
		producer.wakeup();
		synchronized (producer) {
			producer.notify();
		}

		consumer.wakeup();
		synchronized (consumer) {
			consumer.notify();
		}
	}

	public void pauseClustering() {
		appState = AppState.PAUSED;
		producer.pause();
		consumer.pause();
		//Block the app here until they are really waiting
		while( !producer.isSynched() ) {
			//System.out.println("Producer Not synched in paused yet!");
		}
		while( !consumer.isSynched() ) {
			//System.out.println("Consumer Not synched in paused yet!");
		}
	}

	public void cancelClustering() {
		appState = AppState.CLEAN_START;
		producer.die();
		consumer.die();
		synchronized (producer) {
			producer.notify();
		}
		synchronized (consumer) {
			consumer.notify();
		}
		//Block here until they are both dead;
		while( !producer.isSynched() ) {
			//System.out.println("Producer Not Synched yet in cancel.");
		}

		while( !consumer.isSynched()  ) {
			//System.out.println("Consumer Not Synched yet in cancel.");
		}
		//Clean the stuff
		buffer.clear();
		executor.shutdown();
	}

	public int getNumberOfWindows() {
		return consumer.getNumberOfWindows();
	}

	public int getNumberOfClusters() {
		return consumer.getNumberOfClusters();
	}
	
	public int getNumberOfBurstyClusters() {
		int numberOfBurstyClusters = 0;
		
		for ( Long clusterID : consumer.getLeaderFollowerClusters().keySet() ) {
			if( consumer.getLeaderFollowerClusters().get(clusterID).getClusterSize() >= getBurstinessThreshold() ) {
				numberOfBurstyClusters++;
			}
		}
		
		return numberOfBurstyClusters;
	}

	public List<EventResult> getEventResults() {
		List<EventResult> eventResultsList = new LinkedList<EventResult>();

		detectionPipeline.updateDescriptorsMap(consumer.getLeaderFollowerClusters(),
				consumer.getClustersAnalyzer(), 
				consumer.getTweetsInClustersDao().getTweetsInClusters());

		detectionPipeline.updateDetectors();

		//Constructs EventResults
		for ( Long clusterID : consumer.getLeaderFollowerClusters().keySet() ){
			LeaderFollowerCluster cluster = consumer.getLeaderFollowerClusters().get(clusterID);
			Event event = detectionPipeline.getEvent(clusterID);
			//The detectors for each event / i.e. to see which detectors detected which event
			List<EventDetector> eventDetectors = new LinkedList<EventDetector>();

			for ( long eventDetectorID : getEventsDetectors().keySet() ) {
				EventDetector eventDetector = getEventsDetectors().get(eventDetectorID);
				if ( eventDetector.getEvents().contains(event) ) {
					eventDetectors.add(eventDetector);
				}
			}

			EventResult eventResult = new EventResult(event, cluster, eventDetectors, 
					consumer.getTweetsInClustersDao(), Color.BLACK);
			eventResultsList.add(eventResult);
		}

		//remove ... just for Lin
//		try {
//			writeResultsToCSV();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return getBurstyEventResults(eventResultsList);
	}

	public List<EventResult> updateEventResults() {
		List<EventResult> eventResultsList = new LinkedList<EventResult>();
		
		if ( !detectionPipeline.getGlobalEventsMap().isEmpty() ) {
			detectionPipeline.updateDetectors();

			//Constructs EventResults
			for ( Long clusterID : consumer.getLeaderFollowerClusters().keySet() ){
				LeaderFollowerCluster cluster = consumer.getLeaderFollowerClusters().get(clusterID);
				Event event = detectionPipeline.getEvent(clusterID);
				//The detectors for each event / i.e. to see which detectors detected which event
				List<EventDetector> eventDetectors = new LinkedList<EventDetector>();

				for ( long eventDetectorID : getEventsDetectors().keySet() ) {
					EventDetector eventDetector = getEventsDetectors().get(eventDetectorID);
					if ( eventDetector.getEvents().contains(event) ) {
						eventDetectors.add(eventDetector);
					}
				}

				EventResult eventResult = new EventResult(event, cluster, eventDetectors, 
						consumer.getTweetsInClustersDao(), Color.BLACK);
				eventResultsList.add(eventResult);
			}
		}
		
		return getBurstyEventResults(eventResultsList);
	}
	
	public List<EventResult> getBurstyEventResults(List<EventResult> normalResults) {
		List<EventResult> burstyResults = new LinkedList<EventResult>();
		
		for ( EventResult eventResult : normalResults ) {
			if ( eventResult.getCluster().getClusterSize() >= getBurstinessThreshold() ) {
				burstyResults.add(eventResult);
			}
		}
		
		return burstyResults;
	}

	public LinkedHashSet getDimensions() {
		LinkedHashSet<String> dimensions = new LinkedHashSet<String>();
		for ( int i = 1; i <= 26; i++ ) {
			dimensions.add( String.format("%d", i) );
		}
		return dimensions;
	}

	public int getBurstinessThreshold() {
		return burstinessThreshold;
	}

	public void setBurstinessThreshold(int burstinessThreshold) {
		this.burstinessThreshold = burstinessThreshold;
	}
	
	public void writeResultsToCSV() throws IOException {
		BufferedWriter featuresWriter = FileUtils.getFileWriter(new File( "/Users/rockyrock/Desktop/features.csv" ));
		BufferedWriter tweetsWriter = FileUtils.getFileWriter( new File( "/Users/rockyrock/Desktop/tweets.csv" ) );
		
		featuresWriter.write( "ClusterID\t" );
		
		//Cumbersom just to write the CSV headers
		for ( Long clusterID : consumer.getLeaderFollowerClusters().keySet() ){
			Event event = detectionPipeline.getEvent(clusterID);
			
			for ( EventDescriptorType eventDescType : eventDescriptorsTypesShortcuts.keySet() ) {
				EventDescriptor eventDesc = event.getDescriptorsMap().get(eventDescType);
				String descName = eventDesc.toString();
				featuresWriter.write( String.format("%s\t", descName) );
			}
			
			System.out.println("Number of features: " + eventDescriptorsTypesShortcuts.keySet().size());
			break;
		}
		
		featuresWriter.write( "\n" );
		
		System.out.println("Now writing featurs...");
		
		for ( Long clusterID : consumer.getLeaderFollowerClusters().keySet() ){
			//LeaderFollowerCluster cluster = consumer.getLeaderFollowerClusters().get(clusterID);
			Event event = detectionPipeline.getEvent(clusterID);
			
			featuresWriter.write( String.format("%d\t", clusterID) );
			
			for ( EventDescriptorType eventDescType : eventDescriptorsTypesShortcuts.keySet() ) {
				EventDescriptor eventDesc = event.getDescriptorsMap().get(eventDescType);
				featuresWriter.write( String.format("%f\t", eventDesc.getScore()) );
			}
			
			featuresWriter.write( "\n" );
			
		}
		
		featuresWriter.close();
		
		
		System.out.println("Finished writing features.csv");
		
		//Writing the tweetsCSV
		
		tweetsWriter.write( "ClusterID\t" );
		tweetsWriter.write("tweet\n");
		
		for ( Long clusterID : consumer.getLeaderFollowerClusters().keySet() ){
			Cluster cluster = consumer.getLeaderFollowerClusters().get(clusterID);
			List<Tweet> clusterTweets = 
					ClustersUtils.getClusterTweets(cluster, consumer.getTweetsInClustersDao().getTweetsInClusters());
			for ( Tweet tweet : clusterTweets ) {
				tweetsWriter.write( String.format("%d\t%s\n", clusterID, tweet.getText()) );
			}
			
		}
		tweetsWriter.close();
		System.out.println("Finished writing tweets.csv");
		
	}

}

















