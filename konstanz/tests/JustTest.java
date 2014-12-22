package de.uni.konstanz.tests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import de.uni.konstanz.Parameters;
import de.uni.konstanz.csvtasks.CSVToSorted;
import de.uni.konstanz.dao.CSVDao;
import de.uni.konstanz.models.Tweet;
import de.uni.konstanz.utils.FileUtils;
import de.uni.konstanz.utils.MapUtils;


public class JustTest {

	public static void main(String[] args) throws IOException  {
//		String text = "Paris this Damascus hama @back obama is the Newtown a #fucking homs text! man ;) Syria and New York city will Be A free!";
//		
		List<Tweet> tweets = CSVDao.read(Parameters.testFile, 200);
//		LocationBasedEventDetector eventDetector = new 
//				LocationBasedEventDetector();
//		System.out.println("Started to detect locations ...");
//		long t0 = System.currentTimeMillis();
//		Map<String, Cluster> clusters = eventDetector.detect(tweets);
//		long t1 = System.currentTimeMillis();
//		long passed = t1 - t0;
//		System.out.println(passed/1000);
//		
//		
//		List<Cluster> clustersList = new ArrayList<Cluster>(clusters.values());
//
//		Map<Long, Tweet> tweetsMap = new LinkedHashMap<Long, Tweet>();
//		for ( Tweet t : tweets ) {
//			tweetsMap.put(t.getId(), t);
//		}
//
//		StreamUtils.printClusters(new File("locClusters"), clustersList, tweetsMap, 1);
		
//		int x = 5;
//		int y = 2;
//		
//		System.out.println( (double) x/y );
		
//		for ( String s : Twokenize.tokenize("she's so it's he's good") ) {
//			System.out.println(s);
//		}
		
		System.out.println("Started...");
		for ( Tweet tweet : tweets ) {
			//System.out.println(tweet.getUser().getTimeZone());
			
		}
		
		
		Map<String, Integer> timezones = new
				LinkedHashMap<String, Integer>();
		
		
		for ( Tweet tweet : tweets ) {
			
			if ( tweet.getUser().getTimeZone() != null ) {
				if( !timezones.containsKey( tweet.getUser().getTimeZone() ) ) {
					timezones.put(tweet.getUser().getTimeZone(), 1);
				}
				else {
					int count = timezones.get(tweet.getUser().getTimeZone());
					count++;
					timezones.put(tweet.getUser().getTimeZone(), count);
				}
			}
			
			
		}
		
		timezones = MapUtils.sortByValue(timezones, true);
		
		for ( String key : timezones.keySet() ) {
			int f = timezones.get(key);
			System.out.println("Key: " + key + ", N: " + f);
		}
		
		if ( timezones.entrySet().size() > 0 ) {
			Map.Entry<String, Integer> entry = timezones.entrySet().iterator().next();
			System.out.printf("The Max is: %s, counter = %d\n", entry.getKey(), entry.getValue());
		}
			
	}


	public static void writePart() {
		File csvFileLocation = new 
				File("/Users/rockyrock/Desktop/testCSV/2013_04_19_22.csv");
		System.out.println("Reading tweets ....");
		List<String> lines = new LinkedList<String>();
		List<Integer> indices = new ArrayList<Integer>();

		for ( int i = 470; i <= 500; i++ ) {
			indices.add(i);
		}

		String header = CSVToSorted.getLine(csvFileLocation, 0);
		lines.add(header);

		for ( Integer index : indices ) {
			String line = CSVToSorted.getLine(csvFileLocation, index);
			lines.add(line);
		}
		System.out.println("Done reading tweets");

		String path = FilenameUtils.getFullPath(csvFileLocation.getAbsolutePath());
		path += FilenameUtils.getBaseName(csvFileLocation.getAbsolutePath());
		path += "part.csv";
		File outputFile = new File(path);
		BufferedWriter writer = FileUtils.getFileWriter(outputFile);
		try {
			for ( String l : lines ) {
				writer.write(l + "\n");
			}
		}
		catch( IOException e ) {
			e.printStackTrace();
		}

		try {
			writer.close();
		}
		catch( IOException e ) {
			e.printStackTrace();
		}

		System.out.println("Done writing tweets.");
	}
	
	public static String getFullURL(String url) throws MalformedURLException, IOException {
		String location = url;
		HttpURLConnection connection = (HttpURLConnection) new URL(location).openConnection();
		connection.setInstanceFollowRedirects(false);
		while (connection.getResponseCode() / 100 == 3) {
		    location = connection.getHeaderField("location");
		    connection = (HttpURLConnection) new URL(location).openConnection();
		}
		
		return connection.toString();
	}

}










