package de.uni.konstanz.gui;

import java.util.LinkedHashSet;
import java.util.Set;

public class JustTest {
	public static void main(String[] args) {
		Set<String> dims = new LinkedHashSet<String>();
		Set<String> new_dims = new LinkedHashSet<String>();
		for ( int i = 1; i <= 10; i++ ) {
			dims.add( String.format("%d", i) );
		}
		
		for ( String dim : dims ) {
			System.out.println(dim);
		}
		
		Set<String> hld = new LinkedHashSet<String>(); 
		hld.add("2");
		hld.add("5");
		
		dims.removeAll(hld);
		new_dims.addAll(hld);
		new_dims.addAll(dims);
		
		for ( String dim : new_dims ) {
			System.out.println(dim);
		}
		
	}
}
