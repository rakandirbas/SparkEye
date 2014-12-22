package de.uni.konstanz.tests;

import java.io.File;

import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;

import de.uni.konstanz.dao.LuceneTweetDao;
import de.uni.konstanz.models.Tweet;

public class LuceneSearcherTest {
	
	public static void main(String[] args) {
		String path = "/Users/rockyrock/Desktop/CSVToLuceneIndex";
		File indexPath = new File( path );
		LuceneTweetDao tweetDao = new LuceneTweetDao( indexPath );
		tweetDao.openReader();
		String query = "1370854169683";
		//String query = "0 TO 1370465347908";
		//String query = "user_screenName:JepicHassan1";
		//String query = "isRetweet:true";
		//Query nQuery = NumericRangeQuery.newLongRange("id", 1370465347908L, 
		//		1370465347908L, true, true);
		Tweet tweet = tweetDao.get(query);
		System.out.println( tweet );
	}

}
