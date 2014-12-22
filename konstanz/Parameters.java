package de.uni.konstanz;

import java.io.File;
import java.util.prefs.Preferences;

public class Parameters {
	private static final Preferences prefs = Preferences.systemRoot();
	public static final File testFile
		= new File("/Users/rockyrock/Desktop/testCSV/2013_04_19_22_filtered_sorted.csv");
	
	public static final int leader_follower_chunkSize;
	public static final double  leader_follower_clustering_threshold;
	public static final double leader_follower_merging_threshold;
	public static final String outputpath;
	public static final int cluster_stats_std_alpha;
	public static final double leader_follower_likelihoodThreshold;
	public static final int leader_follower_merging_period;
	public static final int leader_follower_clusters_livetime_period;
	public static final int leader_follower_monitoring_threshold;
	public static final String POSTaggerModelFileName;
	
	static {
		Preferences prefs = getParameters();
		leader_follower_chunkSize = 
				prefs.getInt("leader_follower_chunkSize", 1000);
		leader_follower_clustering_threshold = 
				prefs.getDouble("leader_follower_clustering_threshold", 0.4);
		leader_follower_merging_threshold = 
				prefs.getDouble("leader_follower_merging_threshold", 0.4);
		outputpath = 
				prefs.get("outputpath", "/Users/rockyrock/Desktop/");
		cluster_stats_std_alpha = 
				prefs.getInt("cluster_stats_std_alpha", 2);
		leader_follower_likelihoodThreshold =
				prefs.getDouble("leader_follower_likelihoodThreshold", 2.0);
		leader_follower_merging_period = 
				prefs.getInt("leader_follower_merging_period", 10);
		leader_follower_clusters_livetime_period = 
				prefs.getInt("leader_follower_clusters_livetime_period", 10);
		leader_follower_monitoring_threshold = 
				prefs.getInt("leader_follower_monitoring_threshold", 10);
		POSTaggerModelFileName = 
				prefs.get("POSTaggerModelFileName", "resources/model.20120919");
	}
	
	public static Preferences getParameters() {
		prefs.putInt("leader_follower_chunkSize", 1000);
		prefs.putDouble("leader_follower_clustering_threshold", 0.4);
		prefs.putDouble("leader_follower_merging_threshold", 0.4);
		prefs.put("outputpath", "/Users/rockyrock/Desktop/");
		prefs.putInt("cluster_stats_std_alpha", 2);
		prefs.putDouble("leader_follower_likelihoodThreshold", 2.0);
		prefs.putInt("leader_follower_merging_period", 10);
		prefs.putInt("leader_follower_clusters_livetime_period", 10);
		prefs.putInt("leader_follower_monitoring_threshold", 10);
		prefs.put("POSTaggerModelFileName", "resources/model.20120919");
		return prefs;
	}
	
}
