package de.uni.konstanz.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import de.uni.konstanz.TwitterAPIStream;
import de.uni.konstanz.dao.LuceneTweetDao;
import de.uni.konstanz.models.Tweet;

public class LuceneTweetDaoTest {
	public static void main(String[] args) {
		String path = null; 
		if ( args.length > 0 ) {
			path = args[0];
		}
		if (path == null) {
			path = "/Users/rockyrock/Desktop/LuceneIndex";
		}

		File indexPath = new File( path );
		LuceneTweetDao tweetDao = new LuceneTweetDao( indexPath );
		TwitterAPIStream twitterStream = new TwitterAPIStream( tweetDao );

		try {
			BufferedReader br = new BufferedReader( new InputStreamReader(System.in) );
			String input;
			while( ( input = br.readLine() ) != null) {
				input = input.toLowerCase();
				if ( input.equals("start") ) {
					System.out.println( "Started ..." );
					twitterStream.start();
				}
				else if ( input.equals("stop") ) {
					twitterStream.stop();
					System.out.println( "Total indexed: " + twitterStream.getCounter() );
				}
				else if ( input.equals("print") ) {
					System.out.println( "Indexed so far: " + twitterStream.getCounter() );
				}
				else if ( input.equals("close") ) {
					System.out.println( "Exiting application." );
					System.exit(0);
				}
				else if( input.matches( "find:[\\w]*$" ) ) {
					String query = input.replaceAll("find:", "");
					tweetDao.openReader();
					Tweet tweet = tweetDao.get(query);
					System.out.println( tweet );
				}
			}
		}
		catch(IOException e) {
			e.printStackTrace();

		}

	}
}
