package de.uni.konstanz.eventdescriptors;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uni.konstanz.models.Cluster;
import de.uni.konstanz.utils.MathUtils;

public class PopularityComputer {
	//The sum of all documents in all cluster
	private int sum;
	//The mean of the sizes of all clusters
	private double mean;
	//The standard dev.
	private double std;
	private int max;
	
	public void computeAndUpdateScores( Map<Long, ? extends Cluster> clusters ) {
		sum = 0;
		mean = 0;
		std = 0;
		List<Integer> sizes = new LinkedList<Integer>();
		
		for ( long clusterID : clusters.keySet() ) {
			Cluster cluster = clusters.get(clusterID);
			sizes.add(cluster.getClusterSize());
		}
		sum = MathUtils.getSum(sizes);
		mean = MathUtils.getAverage(sizes);
		std = MathUtils.getStd(sizes, mean);
		max = Collections.max(sizes);
	}
	
	public double getScore( int size ) {
		return (double) size/max;
	}
	
	
}














