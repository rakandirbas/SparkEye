package de.uni.konstanz.utils;

import java.util.List;

public class MathUtils {
	
	public static int getSum(List<Integer> values) {
		int sum = 0;
		
		for ( int value : values ) {
			sum+= value;
		}
		
		return sum;
	}
	
	public static int getSum(int[] values) {
		int sum = 0;
		
		for ( int value : values ) {
			sum += value;
		}
		
		return sum;
	}

	/**
	 * To check if a value is with in a specified number of standard dev.
	 * @param value the value to be tested
	 * @param average the average
	 * @param std the standard dev.
	 * @param stdCount how many std dev. you want to check with.
	 * @return returns true if within, false otherwise.
	 */
	public static boolean isWithInSTD( double value, double average, double std, double stdCount ) {
		boolean state = false;

		double threshold = average + ( stdCount * std );

		if ( value <= threshold )
			state = true;

		return state;
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

	public static double getAverage( List<Integer> vector ) {
		double average = 0;

		double total = 0;

		for ( int x : vector ) {
			total += x;
		}
		average = (double) total / vector.size();

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

	public static double getStd( List<Integer> vector, double average ) {
		double std = 0;
		double total = 0;

		for ( int x : vector ) {
			total += (x - average) * (x - average);
		}

		std = ( (double) 1/(vector.size()-1)) * total;

		std = Math.sqrt(std);

		return std;
	}

	public static double getLogLikelihood( double A, double B, double C, double D ) {
		double ratio = 0;

		if ( A == 0 || B == 0 || C == 0 || D == 0 )
			return 0;

		double N = A + B + C + D;

		double part1 = ( A * Math.log10( (A/(A+B)) / ((A+C)/N) ) );
		double part2 = ( B * Math.log10( (B/(A+B)) / ((B+D)/N) ) );
		double part3 = ( C * Math.log10( (C/(C+D)) / ((A+C)/N) ) );
		double part4 = ( D * Math.log10( (D/(C+D)) / ((B+D)/N) ) );

		ratio = part1 + part2 + part3 + part4;


		return ratio;
	}

	/**
	 * Checks if a number is a prime number
	 * @param x
	 * @return
	 */
	public static boolean isPrime(int x) {
		boolean isPrime = true;
		
		if ( x == 0 || x == 1)
			return false;
		
		for (int i = 2; i < x; i++) {
			if ( x % i == 0 )
				return false;
		}
		
		return isPrime;
	}
}








