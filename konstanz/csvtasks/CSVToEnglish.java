package de.uni.konstanz.csvtasks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import de.uni.konstanz.analysis.Preprocessor;
import de.uni.konstanz.dao.CSVDao;
import de.uni.konstanz.utils.FileUtils;

/**
 * This class has a helper static method to filter a CSV file to English
 * tweets only. The output file will be in the same location as the original
 * but has _filtered appended to its name.
 * @author rockyrock
 *
 */

public class CSVToEnglish implements CSVTask {
	
	private static Logger logger = Logger.getLogger(CSVToEnglish.class);
	
	/**
	 * Filters a csv file from Andreas to English tweets only.
	 * @param path
	 * @return the path of the fitlered file.
	 */
	
	
	/**
	 * Filters a csv file from Andreas to English tweets only.
	 * @param path
	 * @param outputDir if null, file will be put in the same dir
	 * @return the path of the fitlered file.
	 */
	public static File filterToEnglish( File path, File outputDir ) {
		boolean isGood = false;
		BufferedReader reader = FileUtils.getFileReader( path );
		String outputFileName = "";
		
		if ( outputDir == null ) {
			outputFileName = 
					FilenameUtils.getFullPath( path.getAbsolutePath() );
		}
		else {
			outputFileName = outputDir.getAbsolutePath() + "/";
		}
		
		outputFileName += FilenameUtils.getBaseName(path.getAbsolutePath());
		outputFileName += "_filtered.csv";
		File outputFile = new File( outputFileName );
		BufferedWriter writer = FileUtils.getFileWriter(outputFile);
		String line = "";
		
		try {
			for ( int i = 0; ((line = reader.readLine()) != null); i++ ) {

				if ( i == 0) {
					writer.write(line + "\n");
					continue;
				}
					
				String[] columns = line.split("\t");
				if ( columns.length == 25 ) {
					String text = columns[2];
					//OLD way
//					List<Token> tokensList = 
//							Preprocessor.preprocessor.getTokensList( text );
//					if ( tokensList.size() > 0 ) {
//						String tokenedText = 
//								Preprocessor.preprocessor.getTokensAsText(tokensList);
//						boolean pass = 
//								Preprocessor.preprocessor.pass( tokenedText );
//						
//						if ( pass ) {
//							writer.write(line+"\n");
//						}
//					}
					
					//NEW WAY
					boolean pass = 
							Preprocessor.preprocessor.pass( text );
					if ( pass ) {
						writer.write(line+"\n");
					}
				}
			}
		} catch (IOException e) {
			logger.error("Error while reading a line " +
					"during filtering to English.", e);
			e.printStackTrace();
		}
		finally{
			try {
				reader.close();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error( "Error while closing the reader or writer" +
						"after filtering the CSV file to English" );
			}
			
		}
		
		return outputFile;
		
	}
	
	public static void main(String[] args) {
		File csvFilePath = new 
				File("/Users/rockyrock/Desktop/testCSV/2013_04_15_22.csv");
		
		System.out.println( "Computing original file size..." );
		System.out.printf( "Original size: %d\n", 
				CSVDao.getNumberOfLines(csvFilePath));
		
		System.out.println( "Started to filter the file..." );
		long startTime = System.currentTimeMillis();
		File outputFile = CSVToEnglish.filterToEnglish(csvFilePath, null);
		long elapsedTime = System.currentTimeMillis() - startTime;
		elapsedTime /= 1000;
		System.out.println( "Filtering took: " + elapsedTime + " seconds." );
		
		System.out.printf( "Filtered size: %d\n", 
				CSVDao.getNumberOfLines(outputFile));
		
	}

	@Override
	public File runTask(File path, File outputDir) {
		return filterToEnglish(path, outputDir);
	}
	
	@Override
	public String toString() {
		return "CSVToEnglish";
	}

}
