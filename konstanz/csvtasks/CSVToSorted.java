package de.uni.konstanz.csvtasks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.FilenameUtils;

import de.uni.konstanz.dao.CSVDao;
import de.uni.konstanz.models.Tweet;
import de.uni.konstanz.utils.FileUtils;

/**
 * This class is used to termporally sort a csv file from Andreas.
 * @author rockyrock
 *
 */
public class CSVToSorted implements CSVTask {

	public static void sort( File csvFileLocation ) {
		BufferedReader reader = FileUtils.getFileReader(csvFileLocation);
		String path = FilenameUtils.getFullPath(csvFileLocation.getAbsolutePath());
		path += FilenameUtils.getBaseName(csvFileLocation.getAbsolutePath());
		path += "_sorted.csv";
		File outputFile = new File(path);
		BufferedWriter writer = FileUtils.getFileWriter(outputFile);

		class IndexMember implements Comparable<IndexMember> {
			int index;
			Timestamp createdAt;

			public IndexMember(int index, Timestamp createdAt) {
				this.index = index;
				this.createdAt = createdAt;
			}

			@Override
			public int compareTo(IndexMember o) {
				if ( createdAt.getTime() > o.createdAt.getTime() )
					return 1;
				else if (createdAt.getTime() < o.createdAt.getTime())
					return -1;
				else return 0;
			}

		}

		List<IndexMember> indexMembers = new 
				LinkedList<IndexMember>();
		String line = "";
		String header = "";
		try {
			for ( int i = 0; ((line = reader.readLine()) != null); i++ ) {
				if ( i == 0 )
					header = line;
				else {

					if ( line.split("\t").length == 25 ) {
						Tweet tweet = CSVDao.fromCSVLineToTweet(line);
						indexMembers.add( new IndexMember(i, tweet.getCreatedAt()) );
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println( "Sorting the index members ..." );
		Collections.sort(indexMembers);
		System.out.println( "Done sorting." );
		try {
			writer.write(header + "\n");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		int i = 1;
		long t0 = System.currentTimeMillis();
		for ( IndexMember indexMember : indexMembers ) {

			try {
				String csvLine = getLine( csvFileLocation, indexMember.index );
				writer.write( csvLine + "\n" );
				if ( i%10000 == 0 )
					System.out.println( "Wrote " + i );
				i++;
				
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		
		try {
			writer.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		long t1 = System.currentTimeMillis();
		long elapsed = t1 - t0;
		elapsed /= 1000;
		System.out.println( "Writing to file took: " + elapsed + " seconds.");

	}

	public static String getLine( File csvFileLocation, int index ) {
		BufferedReader reader = FileUtils.getFileReader(csvFileLocation);
		String line = "";

		try {
			for ( int i = 0; ((line = reader.readLine()) != null); i++ ) {
				if ( i == index ) {
					return line;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return line;
	}

	/**
	 * Creates a sorted csv file temporally
	 * @param csvFileLocation
	 * @param outputDir if null, file will be put in the same dir
	 * @return
	 */
	public static File sortUsingRAM( File csvFileLocation, File outputDir ) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "EEE MMM dd HH:mm:ss Z yyyy", 
				Locale.ENGLISH );
		BufferedReader reader = FileUtils.getFileReader(csvFileLocation);
		String path = "";
		
		if ( outputDir == null ) {
			path = FilenameUtils.getFullPath(csvFileLocation.getAbsolutePath());
		}
		else {
			path = outputDir.getAbsolutePath() + "/";
		}
		
		path += FilenameUtils.getBaseName(csvFileLocation.getAbsolutePath());
		path += "_sorted.csv";
		File outputFile = new File(path);
		BufferedWriter writer = FileUtils.getFileWriter(outputFile);

		class IndexMember implements Comparable<IndexMember> {
			int index;
			String line;
			Timestamp createdAt;

			public IndexMember(int index, String line, Timestamp createdAt) {
				this.index = index;
				this.line = line;
				this.createdAt = createdAt;
			}

			@Override
			public int compareTo(IndexMember o) {
				if ( createdAt.getTime() > o.createdAt.getTime() )
					return 1;
				else if (createdAt.getTime() < o.createdAt.getTime())
					return -1;
				else return 0;
			}

		}

		List<IndexMember> indexMembers = new 
				LinkedList<IndexMember>();
		String line = "";
		String header = "";
		try {
			for ( int i = 0; ((line = reader.readLine()) != null); i++ ) {
				if ( i == 0 )
					header = line;
				else {

					if ( line.split("\t").length == 25 ) {
						Date createdAt = simpleDateFormat.parse( line.split("\t")[1] );
						Timestamp timestamp = new Timestamp(createdAt.getTime());
						indexMembers.add( new IndexMember(i, line, timestamp) );
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//System.out.println( "Sorting the index members ..." );
		Collections.sort(indexMembers);
		//System.out.println( "Done sorting." );
		
		try {
			writer.write(header + "\n");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		int i = 1;
		long t0 = System.currentTimeMillis();
		for ( IndexMember indexMember : indexMembers ) {

			try {
				String csvLine = indexMember.line;
				writer.write( csvLine + "\n" );
				if ( i%10000 == 0 )
					System.out.println( "Wrote " + i );
				i++;
				
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		
		try {
			writer.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		long t1 = System.currentTimeMillis();
		long elapsed = t1 - t0;
		elapsed /= 1000;
		//System.out.println( "Writing to file took: " + elapsed + " seconds.");
		
		return outputFile;
		
	}

	public static void main(String[] args) {
		File csvFileLocation = new 
				File("/Users/rockyrock/Desktop/testCSV/2013_04_15_22_filtered.csv");
		System.out.println( "Started to sort csv file ..." );
		sortUsingRAM(csvFileLocation, null);
		System.out.println( "Done sorting." );
		
	}

	@Override
	public File runTask(File path, File outputDir) {
		return sortUsingRAM(path, outputDir);
	}
	
	@Override
	public String toString() {
		return "CSVToSorted";
	}
}
