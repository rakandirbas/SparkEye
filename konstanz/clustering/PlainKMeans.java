package de.uni.konstanz.clustering;

import java.util.LinkedList;

public class PlainKMeans {

	public double[][] calculateClusterCenters(int k, int dim, int maxIteration,
			double[][] data ) throws Exception {

		// initialize matrix of double (nr clusters * input dimension)
		double[][] clusters = new double[k][];
		for (int c = 0; c < k; c++) {
			clusters[c] = new double[dim];
		}

		// initialize cluster centers with values of first rows in table
		for ( int i = 0; i < k; i++ ) {
			clusters[i] = data[i];
		}

		for ( int i = 0; i < maxIteration; i++ ) {

			//Step1: Data points assignment:

			double [][] distanceMatrix = new 
					double[ k ][ data.length ];
			//Calculates the distance between each point and centroids
			for ( int x = 0; x < k; x++ ) {
				for ( int j = 0; j < distanceMatrix[x].length; j++ ) {
					distanceMatrix[x][j] = 
							euclideanDistance(clusters[x], data[j]);
				}
			}

			//Step2: Cluster centroids update:
			//Each list is the set of points that belong to a cluster
			LinkedList<LinkedList<Double[]>> pointsAssignments = 
					new LinkedList<LinkedList<Double[]>>();
			for ( int n = 0; n < k; n++ ) {
				pointsAssignments.add( new LinkedList<Double[]>() );
			}

			for ( int j = 0; j < data.length; j++ ) {
				//Array that holds the distance values between a specific point and all clusters
				double[] m = new double[k];
				for ( int x = 0; x < k; x++ ) {
					//Get the distance values between a specific point and all clusters
					m[x] = distanceMatrix[x][j];
				}
				int id = smallest(m);//id of the cluster that the point belongs to.
				//Assign a point to a cluster
				pointsAssignments.get(id).add( convertPrimitiveArrayToGeneric(data[j]) );
			}

			for ( int x = 0; x < k; x++ ) {
				//Gets a 2D array for each list that belong to a cluster
				clusters[x] = calculateNewMean( convertListTo2DArray(pointsAssignments.get(x)), dim );
			}

		}

		return clusters;// return clusters;
	}

	public int findClosestPrototypeFor(double[] row, double[][] clusters) {
		double[] distanceVector = new double[clusters.length];

		for ( int i = 0; i < distanceVector.length; i++ ) {
			distanceVector[i] = euclideanDistance(row, clusters[i]);
		}


		return smallest(distanceVector);// return the winning id of clusters;
	}

	public static double euclideanDistance(double[] p, double[] q) {
		double distance = 0;

		if ( p.length == q.length ) {
			for ( int i = 0; i < p.length; i++ ) {
				distance += Math.pow( ( p[i] - q[i] ), 2);
			}
			distance = Math.sqrt( distance );
		}
		else {
			throw new RuntimeException();
		}

		return distance;
	}

	//Return the id of the smallest item in an array
	public static int smallest( double[] array ) {
		int id = 0;
		double temp = array[0];
		for ( int i = 0; i < array.length; i++ ) {
			if ( array[i] < temp ) {
				id = i;
				temp = array[i];
			}
		}

		return id;
	}

	public double[] calculateNewMean(double[][] points, int dim) {
		double [] mean = new double[dim];
		for (int i = 0; i < points.length; i++) {
			for ( int j = 0; j < dim; j++ ) {
				mean[j] += points[i][j];
			}
		}
		for ( int i = 0; i < mean.length; i++ ) {
			mean[i] = mean[i] / (double) points.length;
		}
		return mean;
	}

	public Double[] convertPrimitiveArrayToGeneric(double [] a) {
		Double x[] = new Double[a.length];
		for (int i = 0; i < a.length; i++) {
			x[i] = a[i];
		}
		return x;
	}
	public double[] convertGenericArraytoPrimitive(Double [] a) {
		double x[] = new double[a.length];
		for (int i = 0; i < a.length; i++) {
			x[i] = a[i];
		}
		return x;
	}
	public double[][] convertListTo2DArray(LinkedList<Double[]> list) {
		double[][] array = new double[list.size()][];
		for ( int i = 0; i < array.length; i++ ) {
			array[i] = convertGenericArraytoPrimitive(list.get(i));
		}
		return array;
	}

}
