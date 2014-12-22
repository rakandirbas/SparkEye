package de.uni.konstanz.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

public class VoltLookUpTable {
	private File file;
	private Set<String> dictionary;
	
	public VoltLookUpTable(File file) {
		this.file = file;
		dictionary = new LinkedHashSet<String>();
		BufferedReader reader = FileUtils.getFileReader(this.file);
		String line = "";
		try {
			while( (line = reader.readLine()) != null ) {
				dictionary.add(line.toLowerCase());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean contains(String term) {
		return dictionary.contains(term);
	}
}






