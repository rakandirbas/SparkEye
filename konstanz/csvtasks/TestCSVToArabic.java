package de.uni.konstanz.csvtasks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import de.uni.konstanz.dao.CSVDao;
import de.uni.konstanz.langdetect.LanguageDetector;
import de.uni.konstanz.langdetect.PBLanguageDetector;
import de.uni.konstanz.utils.FileUtils;

/**
 * This class has a helper static method to filter a CSV file to Arabic
 * tweets only. The output file will be in the same location as the original
 * but has _filtered appended to its name.
 * @author rockyrock
 *
 */

public class TestCSVToArabic implements CSVTask {
	
	private static Logger logger = Logger.getLogger(CSVToArabic.class);
	private static LanguageDetector lDetector = new PBLanguageDetector();
	
	/**
	 * Filters a csv file from Andreas to Arabic tweets only.
	 * @param path of the file
	 * @param outputDir if null, file will be put in the same dir
	 * @return the path of the fitlered file.
	 */
	public static File filterToArabic( File path, File outputDir ) {
		boolean isGood = false;
		BufferedReader reader = FileUtils.getFileReaderIgnoreEncoding( path );
		String outputFileName = "";
		
		if ( outputDir == null ) {
			outputFileName = 
					FilenameUtils.getFullPath( path.getAbsolutePath() );
		}
		else {
			outputFileName = outputDir.getAbsolutePath() + "/";
		}
		
		outputFileName += FilenameUtils.getBaseName(path.getAbsolutePath());
		outputFileName += "_Arabic_filtered.csv";
		File outputFile = new File( outputFileName );
		BufferedWriter writer = FileUtils.getFileWriterIgnoreEncoding(outputFile);
		String line = "";
		
		try {
			for ( int i = 0; ((line = reader.readLine()) != null); i++ ) {

				if ( i == 0) {
					continue;
				}
					
				String[] columns = line.split("\t");
				if ( columns.length == 25 ) {
					String text = columns[2];
					boolean pass = lDetector.detect(text, "ar");
					if ( pass ) {
						writer.write(line+"\n");
					}
				}
			}
		} catch (IOException e) {
			logger.error("Error while reading a line " +
					"during filtering to Arabic.", e);
			e.printStackTrace();
		}
		finally{
			try {
				reader.close();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error( "Error while closing the reader or writer" +
						"after filtering the CSV file to Arabic" );
			}
			
		}
		
		return outputFile;
		
	}
	
	public static void main(String[] args) {
		File csvFilePath = new 
				File("/Volumes/Passport/Twitter_Data/Tests/inputdata/old_in/2012_10_23_06.csv");
		
		System.out.println( "Started to filter the file..." );
		long startTime = System.currentTimeMillis();
		File f = new File("/Volumes/Passport/Twitter_Data/Tests/out");
		File outputFile = TestCSVToArabic.filterToArabic(csvFilePath, f);
		long elapsedTime = System.currentTimeMillis() - startTime;
		elapsedTime /= 1000;
		System.out.println( "Filtering took: " + elapsedTime + " seconds." );
		
		System.out.printf( "Filtered size: %d\n", 
				CSVDao.getNumberOfLines(outputFile));
		
	}

	@Override
	public File runTask(File path, File outputDir) {
		return filterToArabic(path, outputDir);
	}
	
	@Override
	public String toString() {
		return "CSVToArabic";
	}

}
