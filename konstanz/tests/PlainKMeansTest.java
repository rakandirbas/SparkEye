package de.uni.konstanz.tests;

import de.uni.konstanz.clustering.PlainKMeans;

public class PlainKMeansTest {
	
	public static void main(String[] args) throws Exception {
		PlainKMeans kmeans = new PlainKMeans();
		double[][]data = new double[4][];
		double point1[] = {1};
		double point2[] = {3};
		double point3[] = {8};
		double point4[] = {9};
		data[0] = point1;
		data[1] = point2;
		data[2] = point3;
		data[3] = point4;
 		
		double[][] centers = kmeans.calculateClusterCenters(2, 1, 1000, data);
		for ( double[] x : centers ) {
			System.out.println( "Printing array:" );
			printArray(x);
		}
		
	}
	
	public static void printArray( double[] array ) {
		for ( double x : array ) {
			System.out.println( x );
		}
	}

}
