package de.uni.konstanz.utils;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.commons.io.FilenameUtils;

import de.uni.konstanz.dao.CSVDao;
import de.uni.konstanz.dao.LuceneTweetDao;
import de.uni.konstanz.models.Tweet;

/*
 * This application is to index the contents of a csv into a lucene index.
 */

public class CSVToLuceneIndexer {
	public static void main(String[] args) {
		if ( args.length < 3 ) {
			System.out.println( "Usage: java indexPath csvDir numberOfLines(-1 entire file)" );
			System.out.println( "Please specify the index location, csv file location" +
					" AND maximum number of lines to index [-1 index entire file]" );
			System.exit(0);
		}
		else {
			File indexLocation = new File( args[0] );
			File csvFileLocation = new File( args[1] );
			
			if ( !csvFileLocation.isDirectory() | !indexLocation.isDirectory() ) {
				System.out.println( "csvFileLocation OR indexLocation " +
						"is not a directory!" );
				System.exit(0);
			}
			else {
				File[] filesList = csvFileLocation.listFiles();
				for ( File f : filesList ) {
					if ( FilenameUtils.getExtension( 
							f.getPath() ).equals("csv") ) {
						System.out.println( "Indexing file: " + f);
						indexCSVFile(f, indexLocation, Integer.parseInt( args[2] ));
						System.out.println( "Finished indexing the file.");
					}
				}
			}

		}
	}
	
	public static void indexCSVFile( File csvFileLocation, File indexLocation, int param ) {
		System.out.println( "Starting time: " + (new Timestamp( System.currentTimeMillis() )) );
		LuceneTweetDao tweetDao = new LuceneTweetDao(indexLocation);
		LinkedList<Tweet> list = null;
		int max = 0;
		int chunkSize = 500000;
		int all = 0;

		if ( param == -1 ) {
			max = CSVDao.getNumberOfLines(csvFileLocation);
		}
		else {
			max = param;
		}

		System.out.println( "Total files: " + max );

		if ( max <= chunkSize ) {
			list = CSVDao.read( csvFileLocation, max );
			System.out.println( "Done reading into list." );
			all += list.size();
			index( list, tweetDao );
			System.out.println( "Done indexing the list." );
			tweetDao.closeWriter();
			System.out.println( "1 - ALL: " + all );
		}
		else {
			int from = 1;
			int to = chunkSize;
			for ( int i = from; i <= max; i = from ) {
				System.out.println( "From: " + from + ", To: " + to );
				list = CSVDao.read( csvFileLocation, from, to );
				System.out.println( "Done reading into list." );
				all += list.size();
				index( list, tweetDao );
				System.out.println( "Done indexing the list." );
				from = to + 1;
				if ( (to + chunkSize) > max ) {
					to = max;
				}
				else {
					to = to + chunkSize;
				}
			}
			tweetDao.closeWriter();
			System.out.println( "2 - ALL: " + all );

		}
		
		System.out.println( "Ending time: " + (new Timestamp( System.currentTimeMillis() )) );
	}

	public static void index( LinkedList<Tweet> list, LuceneTweetDao tweetDao ) {
//		for( Tweet tweet : list ) {
//			tweetDao.put(tweet);
//		}
		
		while( !list.isEmpty() ) {
			tweetDao.put( list.poll() );
		}
		
	}
}
