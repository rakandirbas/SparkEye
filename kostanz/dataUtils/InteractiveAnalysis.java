package de.uni.kostanz.dataUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

import com.rakblog.DataBundle;
import com.rakblog.ParallelCoordinates;

import de.uni.konstanz.dao.CSVListener;
import de.uni.konstanz.dao.CSVStream;
import de.uni.konstanz.eventdescriptors.EventDescriptor;
import de.uni.konstanz.eventdescriptors.EventDescriptorType;
import de.uni.konstanz.eventdetection.DetectionPipeline;
import de.uni.konstanz.eventdetection.Event;
import de.uni.konstanz.gui.GUIController;
import de.uni.konstanz.models.Cluster;
import de.uni.konstanz.models.Tweet;
import de.uni.konstanz.utils.GeoUtils;


public class InteractiveAnalysis {
	
	static ParallelCoordinates parallelCoordinates;
	static GUIController guiController;
	static LinkedHashSet<String> dimensions;
	static List<DataBundle> dataBundles;
	static List<Map<String,Double>> valuesList;
	
	static DetectionPipeline detectionPipeline;
	static Map<Long, Tweet> tweetsInCluster;
	static Cluster cluster;
	static int counter = 0;

	public static void main(String[] args) {
		long t1 = System.currentTimeMillis();
		inits(); 
		setupGUI();
		String pathToCSVFile = "/Volumes/Passport/Twitter_Data/VizTests/20/input/2013_04_20_23.csv";
		File csvFileLocation = new File(pathToCSVFile);
		CSVListener csvListener = getCSVListener();
		CSVStream csvStream = new CSVStream(csvListener, csvFileLocation);
		csvStream.startStream();
		
		while(!csvStream.isStreamOver()) {
		}
		
		
		Map<Long, Cluster> clusters = new LinkedHashMap<Long, Cluster>();
		clusters.put(cluster.getId(), cluster);
		System.out.println("calculating features ...");
		detectionPipeline.updateDescriptorsMap(clusters, null, tweetsInCluster);
		System.out.println("Done calculating.");
		Event event = detectionPipeline.getEvent(cluster.getId());
		
		Map<String, Double> row = new LinkedHashMap<String, Double>();
		
		for ( EventDescriptorType eventDescType : EventDescriptorType.values() ) {
			EventDescriptor eventDesc = event.getDescriptorsMap().get(eventDescType);
			row.put(GUIController.eventDescriptorsTypesShortcuts.get(eventDescType), eventDesc.getScore());
		}
		valuesList.add(row);
		updateViz(valuesList);
		long t2 = System.currentTimeMillis();
		
		long time_diff = t2 - t1;
		System.out.println("Counter: " + counter);
		System.out.println("Time: " + TimeUnit.MILLISECONDS.toMinutes(time_diff));
	}
	
	public static void inits() {
		guiController = new GUIController(0.5);
		dimensions = guiController.getDimensions();
		dataBundles = new LinkedList<DataBundle>();
		LinkedHashSet<String> dimensionsToHighlight = new LinkedHashSet<String>();
		parallelCoordinates = 
				new ParallelCoordinates(dimensions, dataBundles, dimensionsToHighlight );
		parallelCoordinates.setPlottingLineThickness(5);
		valuesList = new 
				LinkedList<Map<String,Double>>();
		DataBundle dataBundle = new DataBundle("Name", valuesList, Color.RED);
		dataBundles.add(dataBundle);
		parallelCoordinates.setDimensions(dimensions);
		parallelCoordinates.setDataBundles(dataBundles);
		
		detectionPipeline = new DetectionPipeline(0.5);
		 cluster = new Cluster();
		 tweetsInCluster = new LinkedHashMap<Long,Tweet>();
	}
	
	public static void setupGUI() {
		JFrame frame = new JFrame("SparkEye");
		frame.setPreferredSize( new Dimension(1300, 800));
		frame.setLayout( new BorderLayout() );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.getContentPane().add(parallelCoordinates, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void updateViz(List<Map<String,Double>> newValuesList) {
		valuesList = newValuesList;
		parallelCoordinates.repaint();
	}
	
	
	public static CSVListener getCSVListener() {
		return new CSVListener() {
			
			@Override
			public void onReceiving(Tweet tweet) {
				processTweet(tweet);
			}
		};
	}
	
	public static void processTweet(Tweet tweet) {
		//Do your stuff here .. if tweet should be aassigned
		
		if ( tweet.getGeoLocation() != null ) {
			double distance = GeoUtils.distance(42.358056, -71.063611, 
					tweet.getGeoLocation().getLatitude(), tweet.getGeoLocation().getLongitude(), "K");
			
			//System.out.println(	tweet.getGeoLocation().getLatitude() + " , " + tweet.getGeoLocation().getLongitude());
			//System.out.println("Distance = " + distance);
			
			if ( distance < 300 ) {
				cluster.addDocumentToCluster(tweet);
				tweetsInCluster.put(tweet.getId(), tweet);
				counter++;
				//System.out.println(++counter);
				//System.out.println(tweet.getText());
			}
			
		}
		
		
	}

}






















