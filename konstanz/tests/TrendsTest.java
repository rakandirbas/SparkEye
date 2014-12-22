package de.uni.konstanz.tests;

import de.uni.konstanz.models.ClusterStats;
import de.uni.konstanz.utils.MathUtils;

public class TrendsTest {
	public static void main(String[] args) {
		//myMethod();
		//MMethod();
		//loglikeMethod();
		//testLikeliMethod();
		grubsTest();
		myMethodGrubsTest();
		//stdMethodTest();
	}
	
	public static void stdMethodTest() {
		String v = "123,200,70,5,8,9";
		int[] vector = getVector(v, ",");
		
		double average = MathUtils.getAverage(vector);
		double std = MathUtils.getStd(vector, average);
		double value = 200;
		double stdCount = 1;
		double threshold = average + ( stdCount * std );
		boolean state = MathUtils.isWithInSTD(value, average, std, stdCount);
		boolean isTrend = false;
		
		if ( state == false )
			isTrend = true;
		
		System.out.printf( "value = %f, average = %f, std = %f, threshold = %f, isTrend = %b\n",
				value, average, std, threshold, isTrend );
		
	}
	
	public static void grubsTest() {
		//String clusterAssignmentOverTime = "1,40,80,120";
		String clusterAssignmentOverTime = "1,2,5";
		int[] vector = getVector(clusterAssignmentOverTime, ",");
		double average = 0;
		double std = 0;
		double value = 0;
		double G = 0;

		average = getAverage(vector);
		std = getStd(vector, average);
		
		System.out.printf("average = %f\n", average);
		System.out.printf("std = %f\n", std);
		
		value = 100;
		
		G = ( value - average ) / std;
		
		System.out.println("G = " + G);
	}
	
	public static void myMethodGrubsTest() {
		ClusterStats cSts = new ClusterStats(1, 1.0);
		
		for ( int i = 1; i <= 1000; i++ ) {
			cSts.update(5, 1000, i);
		}
		
//		cSts.update(5, 1000, 1);
//		System.out.println( cSts.getPopularityThreshold() );
//		cSts.update(5, 1000, 2);
//		System.out.println( cSts.getPopularityThreshold() );
//		cSts.update(6, 1000, 3);
//		System.out.println( cSts.getPopularityThreshold() );
		cSts.update(6, 1000, 1001);
		System.out.println( cSts.getPopularityThreshold() );
	}

	public static void testLikeliMethod() {
		//String clusterAssignmentOverTime = "0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	2	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	2";
		//String clusterAssignmentOverTime = "0	0	2	0	3	0	0	2	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	2	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0";
		//String clusterAssignmentOverTime = "4	2	4	6	9	6	6	5	5	7	7	6	7	0	0	6	6	7	6	2	0	4	4	3	7	8	4	6	6	8	9	4	3	5	4	3	3	2	0	2	7	4	4	6	0	6	3	5	6	2	11	4	6	7	5	5	4	5	3	4	3	4	4	7	4	7	8	3	4	4	3	4	7	2	3	3	4	0	0	4	3	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0";
		//String clusterAssignmentOverTime = "4	2	4	6	9	6	6	5	5	7	7	6	7	0	0	6	6	7	6	2	0	4	4	3	7	8	4	6	6	8	9	4	3	5	4	3	3	2	0	2	7	4	4	6	0	6	3	5	6	2	11	4	6	7	5	5	4	5	3	4	3	4	4	7	4	7	8	3	4	4	3	4	7	2	3	3	4	0	0	4	3	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0";
		String clusterAssignmentOverTime = "50	50	100	100	150	150";
		int[] vector = getVector(clusterAssignmentOverTime, "\t");
		
		System.out.println("Vector length: " + vector.length);
		for ( int i = 0; i < vector.length; i++ ) {
			System.out.print( "T" + i + "\t" );
		}

		System.out.println();

		for ( int i = 0; i < vector.length; i++ ) {
			System.out.print( vector[i] + "\t" );
		}
		
		System.out.println();
		
		double A = 0; //the number of documents in the cluster at the current time slice.
		double C = 0; //the number of documents in all other clusters at the current tiem slice. (total documents in this slice - A)

		double B = 0; //the number of documents in the cluster before the current time slice.
		double D = 0; //the number of documents in the all other clusters before the current time slice. (total documents seen so far before the time slice - B)

		double ratio = 0;

		for ( int i = 0; i < vector.length; i++ ) {
			int assignmnets = vector[i];
			A = assignmnets;
			C = 1000 - A;

			ratio = calculateLogLikelihood(A, B, C, D);
			System.out.printf( "A = %f, C = %f, B = %f, D = %f, ratio = %f\n", A, C, B, D, ratio);
			B += A;
			D += C;

		}
		
//		///Testing ClusterStats
//		//-4790326327692131380
//		String clusterSizeOverTime = "0	0	2	2	5	5	5	7	7	7	7	7	7	7	7	7	7	7	7	7	7	7	7	7	7	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9	9";
//		int[] clusterSize = getVector(clusterSizeOverTime, "\t");
//		ClusterStats clusterStats = new ClusterStats(1, 2);
//		for ( int i = 0; i < clusterSize.length; i++ ) {
//			clusterStats.update(clusterSize[i], 1000, i);
//			System.out.println( clusterStats.getLogLikeliHoodRatio() );
//		}
//		
//		System.out.println("###############");
//		System.out.println(clusterSize.length);
//		System.out.println(clusterStats.getRatiosPerUnit().size());
//		System.out.println(clusterStats.getAssignmentsPerUnit().size());
//		for (Integer key : clusterStats.getRatiosPerUnit().keySet()) {
//			Double b = clusterStats.getRatiosPerUnit().get(key);
//			System.out.println( b );
//		}
		
	}

	public static void loglikeMethod() {
		//String clusterSizeOverTime = "1,10,20,30,40,50,60,70";
		String clusterSizeOverTime = "1,2,4,6,6,7,9,10";
		//String clusterSizeOverTime = "0,0,0,0,0,3,0,3";
		String documentsOverTime = "5,20,30,40,50,60,70,80";
		int[] vector = getVector(clusterSizeOverTime, ",");
		int[] vector2 = getVector( documentsOverTime, "," );
		System.out.println("Vector length: " + vector.length);

		for ( int i = 0; i < vector.length; i++ ) {
			System.out.print( "T" + i + "\t" );
		}

		System.out.println();

		for ( int i = 0; i < vector.length; i++ ) {
			System.out.print( vector[i] + "\t" );
		}

		System.out.println();

		System.out.println( "Tweets in each slice: " );

		for ( int i = 0; i < vector2.length; i++ ) {
			System.out.print( vector2[i] + "\t" );
		}

		System.out.println();

		double A = 0; //the number of documents in the cluster at the current time slice.
		double C = 0; //the number of documents in all other clusters at the current tiem slice. (total documents in this slice - A)

		double B = 0; //the number of documents in the cluster before the current time slice.
		double D = 0; //the number of documents in the all other clusters before the current time slice. (total documents seen so far before the time slice - B)

		double ratio = 0;

		for ( int i = 0; i < vector.length; i++ ) {
			int assignmnets = vector[i];
			A = assignmnets;
			C = vector2[i] - A;

			ratio = calculateLogLikelihood(A, B, C, D);
			System.out.printf( "A = %f, C = %f, B = %f, D = %f, ratio = %f\n",
					A, C, B, D, ratio);
			B += A;
			D += C;

		}

	}

	public static double calculateLogLikelihood( double A, double B, double C, double D ) {
		double ratio = 0;

		if ( A == 0 || B == 0 || C == 0 || D == 0 )
			return 0;

		double N = A + B + C + D;

		double part1 = ( A * Math.log10( (A/(A+B)) / ((A+C)/N) ) );
		double part2 = ( B * Math.log10( (B/(A+B)) / ((B+D)/N) ) );
		double part3 = ( C * Math.log10( (C/(C+D)) / ((A+C)/N) ) );
		double part4 = ( D * Math.log10( (D/(C+D)) / ((B+D)/N) ) );

		//		ratio = ( A * Math.log10( (A/(A+B)) / ((A+C)/N) ) ) +
		//				( B * Math.log10( (B/(A+B)) / ((B+D)/N) ) ) +
		//				( C * Math.log10( (C/(C+D)) / ((A+C)/N) ) ) +
		//				( D * Math.log10( (D/(C+D)) / ((B+D)/N) ) );

		//		System.out.println(part1);
		//		System.out.println(part2);
		//		System.out.println(part3);
		//		System.out.println(part4);

		ratio = part1 + part2 + part3 + part4;


		return ratio;
	}

	public static void MMethod() {
		String p = "0,0,0,0,0,1,1,0,0,0,1,1,0,0,0,1,0,1,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0";
		//String p = "0,0,0,1,0,0,0,0";
		//String p = "0,0,1,1,0,0,1,0,1,1,1,1,1,1,1,0,0,0,0,0,1,0,0,0,1";
		int[] vector = getVector(p, ",");
		int distance = 0;
		int lastSeen = 0;
		double average = 0;
		double delta = 0.99;
		double alpha = 0.25;
		boolean isFirstTime = true;
		double threshold = 0;
		System.out.println("Vector length: " + vector.length);

		for ( int i = 0; i < vector.length; i++ ) {
			System.out.print( "T" + i + "\t" );
		}

		System.out.println();

		for ( int i = 0; i < vector.length; i++ ) {
			System.out.print( vector[i] + "\t" );
		}

		System.out.println();

		for ( int t = 0; t < vector.length; t++ ) {
			if ( vector[t] == 1 ) {
				if ( isFirstTime ) {
					lastSeen = t;
					average = t;
					isFirstTime = false;
				}
				distance = t - lastSeen;
				average = ( average * delta ) + ( (distance) * (1.0 - delta) );
				threshold = alpha * average;
				System.out.printf("Time: [%d], Item: [%d], Last Seen: [%d], Distance: [%d], average: [%f]," +
						" Threshold: [%f]\n",t, vector[t] , lastSeen, distance, average, threshold);
				lastSeen = t;
				if ( distance < threshold ) {
					System.out.printf("Trending at time [%d].\n", t);
				}
			}

		}

	}

	public static void myMethod() {
		String p = "2,2,2,2,2,4,4,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7";
		int[] vector = getVector(p, ",");
		ClusterStats clusterStats = new ClusterStats(2, 0.5);

		for ( int unit = 0; unit < vector.length; unit++ ) {
			int x = vector[unit];
			clusterStats.update(x, vector.length, unit);
//			System.out.printf( "Unit: [%d], Cluster size: [%d], average: [%f], std: [%f], " +
//					"threshold: [%f], isTrending: [%b].\n", unit, x, clusterStats.getAverage(),
//					clusterStats.getStd(), clusterStats.getThreshold(), clusterStats.isTrending());
		}
	}

	public static int[] getVector( String p, String sep ) {
		String pArray[] = p.split(sep);
		int[] values = new int[pArray.length];

		for ( int i = 0; i < values.length; i++ ) {
			values[i] = Integer.parseInt( pArray[i] );
		}
		return values;
	}
	
	public static double getAverage( int[] vector ) {
		double average = 0;
		
		double total = 0;
		
		for ( int x : vector ) {
			total += x;
		}
		
		average = (double) total / vector.length;
		
		return average;
	}
	
	public static double getStd( int[] vector, double average ) {
		double std = 0;
		double total = 0;
		
		for ( int x : vector ) {
			total += (x - average) * (x - average);
		}
		
		std = ( (double) 1/(vector.length-1)) * total;
		
		std = Math.sqrt(std);
		
		return std;
	}

}
