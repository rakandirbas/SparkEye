package de.uni.konstanz.tests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.uni.konstanz.models.VoltTweet;
import de.uni.konstanz.preprocessing.Filter;
import de.uni.konstanz.preprocessing.FilterChain;
import de.uni.konstanz.preprocessing.KeywordFilter;
import de.uni.konstanz.stream.StreamListener;
import de.uni.konstanz.stream.VoltCSVStream;


public class TestStreamPipeline implements StreamListener {
	
	FilterChain filterChain;
	public int counter = 0;
	List<VoltTweet> list;

	public static void main(String[] args) throws Exception {
		String f1 = "/Users/rockyrock/Desktop/testCSV/2013_04_15_22.csv";
		String f2 = "/Users/rockyrock/Desktop/testCSV/2013_04_15_22_filtered_sorted.csv";
		TestStreamPipeline pipeline = new TestStreamPipeline();
		VoltCSVStream stream = new VoltCSVStream(new File(f1), pipeline);
		System.out.println("Started");
		long t1 = System.currentTimeMillis();
		stream.start();
		long t2 = System.currentTimeMillis();

		long  d = t2 - t1;

		d /= 1000;
		System.out.println(pipeline.counter);
		System.out.println("Done " + d);
	}
	
	public TestStreamPipeline() {
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new KeywordFilter("+boston"));
		filterChain = new FilterChain(filters);
		list = new ArrayList<VoltTweet>();
	}

	@Override
	public void onReceiving(VoltTweet tweet) {
		if( filterChain.pass(tweet) ) {
//			Map<String, Double> features = NumericTweetFeatures.makeFeatures(tweet);
			System.out.println(tweet.getText());
			System.err.printf("Followers: %d, Friends: %d, Statuses: %d, Listed: %d, Location: %s.\nDescription: %s.\n",
					tweet.getUserFollowersCount(), tweet.getUserFriendsCount(), tweet.getUserNumbTweets(),
					tweet.getUserListedCount(), tweet.getUserLocation(), tweet.getUserDescription());
			counter++;
		}
//		counter++;
//		list.add(tweet);
//		if (counter % 500000 == 0)
//			System.out.println("500000");
		
	}

}
