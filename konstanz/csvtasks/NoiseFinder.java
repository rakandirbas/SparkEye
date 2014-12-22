package de.uni.konstanz.csvtasks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import de.uni.konstanz.utils.FileUtils;
import de.uni.konstanz.utils.MapUtils;

public class NoiseFinder {

	private static Logger logger = Logger.getLogger(NoiseFinder.class);

	public static void main(String[] args) {
		File inputdir = new File("/Volumes/Passport/Twitter_Data/Tests/out");
		File outputdir = new File("/Volumes/Passport/Twitter_Data/Tests/out/n");
		findNoise(inputdir, outputdir);
	}

	public static void findNoise( File inputDirectory, File outputDir ) {
		Map<String, Integer> globalMap = new LinkedHashMap<>();
		BufferedWriter writer = FileUtils.getFileWriter(new File(outputDir + "/noise.csv"));

		if ( !inputDirectory.isDirectory() ) {
			System.err.println("The specified path is not a directory");
			return;
		}
		else {
			int i = 0;
			for ( File fileInDir : inputDirectory.listFiles() ) {
				if ( fileInDir.isFile() && 
						FilenameUtils.getExtension(fileInDir.getAbsolutePath()).equals("csv") ) {

					if ( i == 0 ) {
						globalMap = getFreqsMap(fileInDir);
						i++;
					}
					else {
						Map<String, Integer> secondMap = getFreqsMap(fileInDir);
						Map<String, Integer> intersectionMap = 
								new LinkedHashMap<String, Integer>();
						for ( String term1 : globalMap.keySet() ) {
							int freq1 = globalMap.get(term1);
							if ( secondMap.containsKey(term1) ) {
								int freq2 = secondMap.get(term1);

								int sum = freq1 + freq2;
								intersectionMap.put(term1, sum);
							}
						}
						globalMap = intersectionMap;
					}
					

				}
			}

			globalMap = MapUtils.sortByValue(globalMap, true);
			try {
				i = 1;
				for ( String term : globalMap.keySet() ) {
					int freq = globalMap.get(term);
					writer.write(String.format( "%d,%s,%d\n", i, term, freq ));
					i++;
				}
			}
			catch (IOException e ) {
				e.printStackTrace();
			}
			
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}

	public static Map<String, Integer> getFreqsMap(File path) {
		Map<String, Integer> map = new LinkedHashMap< String, Integer >();
		BufferedReader reader = FileUtils.getFileReader( path );

		String line = "";

		try {
			for ( int i = 0; ((line = reader.readLine()) != null); i++ ) {
				String[] columns = line.split(",");
				if ( columns.length == 3 ) {
					String term = columns[1];
					int freq = Integer.parseInt(columns[2]);
					map.put(term, freq);
				}
			}
		} catch (IOException e) {
			logger.error("Error while reading a line " +
					"finding noise.", e);
			e.printStackTrace();
		}
		finally{
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error( "Error while closing the reader" +
						"after finding noise." );
			}

		}
		
//		Map<String, Integer> topMap = 
//				new LinkedHashMap<String, Integer>();
//		int i = 0;
//		for ( String t : map.keySet() ) {
//			int f = map.get(t);
//			topMap.put(t, f);
//			i++;
//			if ( i == 3000 ) {
//				//break;
//			}
//		}
		return map;
	}
}
