package de.uni.konstanz.tests;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.uni.konstanz.utils.MathUtils;

public class JustTest2 {
	public static void main(String[] args) {
		int sum = 0;
		double mean = 0;
		double std = 0;
		int max = 0;
		List<Integer> sizes = new LinkedList<Integer>();
		
		sizes.add(1);
		sizes.add(6);
		sizes.add(22);
		sizes.add(65);
		sizes.add(34);
		sizes.add(23);
		
		
		sum = MathUtils.getSum(sizes);
		mean = MathUtils.getAverage(sizes);
		std = MathUtils.getStd(sizes, mean);
		max = Collections.max(sizes);
		
		System.out.println( sum );
		System.out.println( mean );
		System.out.println( std );
		System.out.println( max );
		System.out.println( getScore(65, max) );
		
	}
	
	public static double getScore( int size, int max ) {
		return (double) size/max;
	}
}
