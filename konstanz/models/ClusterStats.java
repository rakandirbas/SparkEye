package de.uni.konstanz.models;

import java.util.LinkedHashMap;
import java.util.Map;

public class ClusterStats {
	
	//Holds the value of the cluster size per unit. First integer is the unit,
	//second integer is the size.
	private Map<Integer, Integer> sizesPerUnit;
	private Map<Integer, Integer> assignmentsPerUnit;
	private Map<Integer, Double> ratiosPerUnit;
	private double average;
	private double std;
	private int sum;
	private int squaredSum;
	private double grubsTestValue;
	private int alpha;
	private int previousClusterSizeValue;
	private boolean isTrending;
	
	private double A = 0; //the number of documents in the cluster at the current time slice.
	private double C = 0; //the number of documents in all other clusters at the current time slice. (total documents in this slice - A)
	
	private double B = 0; //the number of documents in the cluster before the current time slice.
	private double D = 0; //the number of documents in the all other clusters before the current time slice. (total documents seen so far before the time slice - B)
	private double logLikeliHoodRatio = 0;
	private double likelihoodThreshold;
	
	/**
	 * 
	 * @param alpha the number of standard devs. to check
	 * @param likelihoodThreshold
	 */
	public ClusterStats(int alpha, double likelihoodThreshold) {
		this.alpha = alpha;
		this.likelihoodThreshold = likelihoodThreshold;
		sizesPerUnit = new
				LinkedHashMap<Integer, Integer>();
		assignmentsPerUnit = new 
				LinkedHashMap<Integer, Integer>();
		ratiosPerUnit = new
				LinkedHashMap<Integer, Double>();
	}
	
	/**
	 * Updates the simple statistics for a cluster and calculate the
	 * log-likelihood ratio for the cluster to see if it's a trend
	 * @param clusterSize
	 * @param totalTweetsCount the total number of tweets that were retrieved during the time unit.
	 * this includes the number of tweets assigned to the cluster at this unit plus all other
	 * tweets that came in the stream at this unit. 
	 * @param unit
	 */
	public void update( int clusterSize, int totalTweetsCount, int unit ) {
		int assignments = clusterSize - previousClusterSizeValue;
		previousClusterSizeValue = clusterSize;
		sizesPerUnit.put(unit, clusterSize);
		assignmentsPerUnit.put(unit, assignments);
		
		/* Grubs test calculations preparations */
		grubsTestValue = computeGrubsTest(assignments, average, std);
		sum += assignments;
		squaredSum += assignments * assignments;
		average = (double) ( ( (unit - 1) * average ) + assignments ) / unit;
		if (unit > 1) {
			std = Math.sqrt( (double) ( (squaredSum) - ( (double) sum * sum / unit ) ) / (unit-1) );
		}
		else {
			std = 0;
		}
		
		/* ==================================== */
		
		
		/* Log-likelihood ratio calculations */
		A = assignments;
		C = totalTweetsCount - A;
		
		logLikeliHoodRatio = calculateLogLikelihood(A, B, C, D);
		ratiosPerUnit.put(unit, logLikeliHoodRatio);
		
		B += A;
		D += C;
		
		if (logLikeliHoodRatio > likelihoodThreshold)
			isTrending = true;
		else
			isTrending = false;
		/*  ================================ */
			
		
	}
	
	public double getPopularityThreshold() {
		double value = Math.abs(grubsTestValue);
		double scaledValue = 0;
		if (value >= 10) {
			scaledValue = 1.0;
		}
		else {
			scaledValue = (double) value / 10;
		}
		return scaledValue;
	}
	
	public static double calculateLogLikelihood( double A, double B, double C, double D ) {
		double ratio = 0;
		
		if ( A == 0 || B == 0 || C == 0 || D == 0 )
			return 0;
		
		double N = A + B + C + D;
		
		ratio = ( A * Math.log10( (A/(A+B)) / ((A+C)/N) ) ) +
				( B * Math.log10( (B/(A+B)) / ((B+D)/N) ) ) +
				( C * Math.log10( (C/(C+D)) / ((A+C)/N) ) ) +
				( D * Math.log10( (D/(C+D)) / ((B+D)/N) ) );
				
		
		return ratio;
	}
	
	public boolean isTrending() {
		return isTrending;
	}
	
	public int getAlpha() {
		return alpha;
	}
	
	public double getAverage() {
		return average;
	}
	public double getStd() {
		return std;
	}
	public int getSum() {
		return sum;
	}
	public int getSquaredSum() {
		return squaredSum;
	}

	public Map<Integer, Integer> getSizesPerUnit() {
		return sizesPerUnit;
	}

	public Map<Integer, Integer> getAssignmentsPerUnit() {
		return assignmentsPerUnit;
	}

	public int getPrevious_value() {
		return previousClusterSizeValue;
	}

	public Map<Integer, Double> getRatiosPerUnit() {
		return ratiosPerUnit;
	}

	public double getLogLikeliHoodRatio() {
		return logLikeliHoodRatio;
	}
	
	public static double computeGrubsTest(double value, double average, double std) {
		if ( std == 0 ) {
			return 0.0;
		}
		double G = (double) ( value - average ) / std;
		return G;
	}
	
}
