package de.uni.konstanz.tests;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.uni.konstanz.dao.CSVDao;
import de.uni.konstanz.models.Tweet;

public class CSVReadingTest {

	public static void main(String[] args) throws IOException {
		File path = new File("/Users/rockyrock/Desktop/arora/2012_07_20_09.csv");
		File path2 = new File("/Users/rockyrock/Desktop/testCSV/2013_04_15_22_filtered.csv");
		List<Tweet> tweets = CSVDao.read(path, 5);

//		BufferedReader reader =  FileUtils.getFileReader(path);
//
//		String line = "";
//
//		for( int i = 0;  (line = reader.readLine() ) != null && i < 3; i++ ) {
//
//			String values[] = line.split("\t");
//			if ( values.length == 25 ) {
//				for (int j = 0; j < values.length; j++) {
//					System.out.println(j + "-:" + values[j]);
//				}
//			}
//
//		}

	}

}







