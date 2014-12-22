package de.uni.konstanz.dao;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import de.uni.konstanz.analysis.Preprocessor;
import de.uni.konstanz.models.GeoLocation;
import de.uni.konstanz.models.Place;
import de.uni.konstanz.models.Tweet;
import de.uni.konstanz.models.UserMentionEntity;
import de.uni.konstanz.utils.AnalyzerUtils;

public class LuceneTweetDao implements TweetDao {

	private Directory dir;
	private IndexWriter writer;
	private DirectoryReader reader;
	private IndexSearcher searcher;
	Analyzer analyzer;
	private static Logger logger = Logger.getLogger(LuceneTweetDao.class);

	/**
	 * This constructor creates a writer to the
	 * index path specified in the file parameter
	 * @param file
	 */
	public LuceneTweetDao( File file ) {
		try {
			if (file.isDirectory()) {
				dir = FSDirectory.open(file);
				analyzer = AnalyzerUtils.analyzer;
				IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_40, analyzer);
				iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
				iwc.setRAMBufferSizeMB(100);
				this.writer = new IndexWriter(dir, iwc);

			}
			else {
				throw new IOException( "The provided index path is not a directory." );
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e);
		}
	}

	public LuceneTweetDao(IndexWriter writer) {
		this.writer = writer;
	}



	@Override
	public void put(Tweet tweet) {
		IndexWriter writer =  getIndexWriter();
		Document doc = new Document();
		doc.add( new StringField( "id", String.format("%d", tweet.id), Field.Store.YES ) );
		doc.add( new LongField( "status_id", tweet.status_id, Field.Store.YES ) );
		doc.add( new LongField( "createdAt", tweet.createdAt.getTime(), Field.Store.YES ) );
		doc.add( new TextField( "text", tweet.text, Field.Store.YES ) );
		//doc.add( new TextField( "tokenedText", tweet.tokenedText, Field.Store.YES ) );
		if ( tweet.inReplyToScreenName != null ) {
			doc.add( new StringField( "inReplyToScreenName", 
					tweet.inReplyToScreenName, Field.Store.YES ) );
		}
		else {
			doc.add( new StringField( "inReplyToScreenName", 
					"", Field.Store.YES ) );
		}
		doc.add( new LongField("inReplyToStatusId", tweet.inReplyToStatusId, Field.Store.YES) );
		doc.add( new LongField( "inReplyToUserId", tweet.inReplyToUserId, Field.Store.YES ) );
		doc.add( new LongField( "retweetCount", tweet.retweetCount, Field.Store.YES) );
		doc.add( new LongField("currentUserRetweetId", tweet.currentUserRetweetId, Field.Store.YES) );
		doc.add( new StringField( "isFavorited", tweet.isFavorited?"true":"false", Field.Store.YES ) );
		doc.add( new StringField( "isPossiblySensitive", tweet.isPossiblySensitive?"true":"false", Field.Store.YES ) );
		doc.add( new StringField( "isRetweet", tweet.isRetweet?"true":"false" , Field.Store.YES) );
		doc.add( new StringField( "isRetweetedByMe", tweet.isRetweetedByMe?"true":"false", Field.Store.YES ) );
		doc.add( new StringField( "isTruncated", tweet.isTruncated?"true":"false", Field.Store.YES ) );

		doc.add( new StringField( "sourceName", tweet.source.getName(),Field.Store.YES ) );
		if (tweet.source.getUrl() != null) {
			doc.add( new StringField( "sourceUrl", tweet.source.getUrl(),Field.Store.YES ) );
		}
		else {
			doc.add( new StringField( "sourceUrl", "",Field.Store.YES ) );
		}

		if (tweet.hashTags != null) {
			for (String h : tweet.hashTags) {
				doc.add( new StringField( "hashtags", h, Field.Store.YES ) );
			}
		}

		//Skipped indexing MediaEntities cuz csv files don't have them
		/**If the tweets coming from StreaminAPI then the URLs here
		 * are only URLs except the pictures URL, this is how the API is working.
		 * But from the csv files, the URLs are any url in the tweet, including 
		 * pictures. The urls will all be in the t.co domain.
		 **/
		if (tweet.URLs != null) {
			for ( String s : tweet.URLs ) {
				doc.add( new StringField("url", s, Field.Store.YES) );
			}
		}

		if ( tweet.userMentionEntites != null ) {
			for ( UserMentionEntity mE : tweet.userMentionEntites ) {
				doc.add( new StringField( "mentionEntity", mE.getScreenName(), Field.Store.YES ) );
			}
		}

		if ( tweet.geoLocation != null ) {
			doc.add( new DoubleField( "tweetLatitude", 
					tweet.geoLocation.getLatitude(), Field.Store.YES ) );
			doc.add( new DoubleField( "tweetLongitude", 
					tweet.geoLocation.getLongitude(), Field.Store.YES ) );
		}

		if ( tweet.place != null ) {
			doc.add( new StringField( "tweetPlaceCountry", tweet.place.getCountry(), Field.Store.YES ) );
			doc.add( new StringField( "tweetPlaceName", tweet.place.getName(), Field.Store.YES ) );
			doc.add( new StringField( "tweetPlaceType", tweet.place.getPlaceType(), Field.Store.YES ) );
		}

		doc.add( new LongField( "user_id", tweet.user.getId(), Field.Store.YES ) );
		doc.add( new StringField("user_name", tweet.user.getName(), Field.Store.YES) );
		doc.add( new StringField( "user_screenName", tweet.user.getScreenName(), Field.Store.YES) );
		if ( tweet.user.getDescription() != null ) {
			doc.add( new TextField( "user_description", tweet.user.getDescription(), Field.Store.YES ) );
		}
		else {
			doc.add( new TextField( "user_description", "", Field.Store.YES ) );
		}

		doc.add( new StringField( "user_lang", tweet.user.getLang(), Field.Store.YES ) );

		if ( tweet.user.getLocation() != null ) {
			doc.add( new TextField( "user_location", tweet.user.getLocation(), Field.Store.YES ) );
		}
		else {
			doc.add( new TextField( "user_location", "", Field.Store.YES ) );
		}

		if ( tweet.user.getTimeZone() != null ) {
			doc.add( new StringField( "user_timeZone", tweet.user.getTimeZone(), Field.Store.YES ) );
		}
		else {
			doc.add( new StringField( "user_timeZone", "", Field.Store.YES ) );
		}

		doc.add( new LongField( "user_registrationDate", 
				tweet.user.getRegistrationDate().getTime(), Field.Store.YES ) );
		doc.add( new IntField( "user_followersCount", 
				tweet.user.getFollowersCount(), Field.Store.YES ) );
		doc.add( new IntField( "user_friendsCount", 
				tweet.user.getFriendsCount(), Field.Store.YES ) );
		doc.add( new IntField( "user_tweetsCount", 
				tweet.user.getTweetsCount(), Field.Store.YES ) );
		doc.add( new IntField( "user_favouritesCount", 
				tweet.user.getFavouritesCount(), Field.Store.YES ) );
		doc.add( new IntField( "user_listedCount", 
				tweet.user.getListedCount(), Field.Store.YES ) );
		doc.add( new StringField( "user_isVerified", tweet.user.isVerified()?"true":"false", Field.Store.YES ) );
		doc.add( new StringField( "user_isTranslator", tweet.user.isTranslator()?"true":"false", Field.Store.YES ) );
		try {
			writer.addDocument(doc);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Failed to index the tweet!", e);
		}
	}

	@Override
	public Tweet get() {

		return null;
	}

	public LinkedList<Tweet> get( Query q, int hitsCount  ) {
		LinkedList<Tweet> list = new LinkedList<Tweet>();
		try {
			TopDocs topDocs = searcher.search(q, hitsCount);
			ScoreDoc[] hits = topDocs.scoreDocs;
			for ( ScoreDoc hit : hits ) {
				Document sDoc = searcher.doc(hit.doc);
				list.add(fromDocToTweet(sDoc));
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			logger.error("Error while searching i.e. with calling searcher.search()", e);
		}
		return list;
	}

	public Tweet get( Query query ) {
		Tweet tweet = new Tweet();
		LinkedList<Tweet> list  = get(query, 1);
		if ( list.size() > 0 )
			tweet = list.get(0);
		return tweet;
	}

	public Tweet get( String query ) {
		Tweet tweet = new Tweet();
		try {
			QueryParser parser = new QueryParser( Version.LUCENE_43, "id", 
					new WhitespaceAnalyzer(Version.LUCENE_43));
			Query q = parser.parse(query);
			System.out.println("QUUQUUQUQU:"+q);
			tweet = get( q );
		}

		catch (ParseException e) {
			e.printStackTrace();
			logger.error("Error while parsing the query!",e);
		}
		return tweet;
	}

	public void openReader() {
		try {
			reader = DirectoryReader.open(dir);
			searcher = new IndexSearcher( reader );
		} catch (IOException e) {
			e.printStackTrace();
			logger.error( "Error while trying to open an index reader.", e );
		}

	}

	public IndexWriter getIndexWriter() {
		return writer;
	}

	public void closeWriter() {
		try {
			getIndexWriter().close();
			logger.info( "Closed the index writer." );
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Error while closing the index writer.", e);
		}
	}

	public void closeReader() {
		try {
			getIndexReader().close();
			logger.info( "Closed the index reader." );
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Error while closing the index reader.", e);
		}
	}

	public IndexReader getIndexReader() {
		return reader;
	}


	public void reopenReader() {
		DirectoryReader freshReader;
		try {
			freshReader = DirectoryReader.openIfChanged(reader);
			if ( freshReader != null ) {
				reader = freshReader;
				freshReader = null;
				searcher = new IndexSearcher(reader);
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Error while reopening the index reader", e);
		}

	}

	public void close() {
		closeWriter();
		closeReader();
	}

	public Tweet fromDocToTweet(Document doc) {
		Tweet tweet = new Tweet();
		tweet.id = Long.parseLong(doc.get("id"));
		tweet.harvestingDate = new Timestamp(tweet.id);
		tweet.status_id = Long.parseLong(doc.get("status_id"));
		tweet.createdAt = new Timestamp(Long.parseLong(doc.get("createdAt")));
		tweet.text = doc.get("text");
		//tweet.tokenedText = doc.get("tokenedText");
		try {
			tweet.tokensList = 
					Preprocessor.preprocessor.getTokensList( tweet.text );
		} catch (IOException e) {
			e.printStackTrace();
			logger.error( "Error while getting the tokensList in fromDocToTweet", e );
		}
		tweet.inReplyToScreenName = doc.get( "inReplyToScreenName" );
		tweet.inReplyToStatusId = Long.parseLong( doc.get("inReplyToStatusId") );
		tweet.inReplyToUserId = Long.parseLong(doc.get("inReplyToUserId"));
		tweet.retweetCount = Long.parseLong(doc.get("retweetCount"));
		tweet.currentUserRetweetId = Long.parseLong(doc.get("currentUserRetweetId"));
		tweet.isFavorited = Boolean.parseBoolean(doc.get("isFavorited"));
		tweet.isPossiblySensitive = Boolean.parseBoolean( doc.get("isPossiblySensitive") );
		tweet.isRetweet = Boolean.parseBoolean( doc.get("isRetweet") );
		tweet.isRetweetedByMe = Boolean.parseBoolean( doc.get("isRetweetedByMe") );
		tweet.isTruncated = Boolean.parseBoolean( doc.get("isTruncated") );
		tweet.source.setName( doc.get("sourceName") );
		tweet.source.setUrl("sourceUrl");
		tweet.hashTags = doc.getValues("hashtags");
		tweet.URLs = doc.getValues("url");

		//NULL watchout! maybe there's no entities!!
		String[] mentionEntities = doc.getValues("mentionEntity");
		UserMentionEntity[] userMentionEntites = 
				new UserMentionEntity[ mentionEntities.length ];
		for ( int i = 0; i < userMentionEntites.length; i++ ) {
			userMentionEntites[i] = new UserMentionEntity();
			userMentionEntites[i].setScreenName( mentionEntities[i] );
		}
		tweet.userMentionEntites = userMentionEntites;

		String sLat = doc.get( "tweetLatitude" );
		if ( sLat != null ) {
			tweet.geoLocation = new GeoLocation();
			double latitude = Double.parseDouble(sLat);
			tweet.geoLocation.setLatitude( latitude );
			tweet.geoLocation.setLongitude( Double.parseDouble( doc.get( "tweetLongitude" ) ) ); 
		}

		String tweetPlaceCountry = doc.get( "tweetPlaceCountry" );
		if ( tweetPlaceCountry != null ) {
			tweet.place = new Place();
			tweet.place.setCountry(tweetPlaceCountry);
			tweet.place.setName( doc.get("tweetPlaceName") );
			tweet.place.setPlaceType( doc.get("tweetPlaceType") );

		}

		tweet.user.setId( Long.parseLong(doc.get("user_id")) );
		tweet.user.setName( doc.get( "user_name" ) );
		tweet.user.setScreenName( doc.get("user_screenName") );
		tweet.user.setDescription( doc.get("user_description") );
		tweet.user.setLang( doc.get("user_lang") );
		tweet.user.setLocation(doc.get("user_location"));
		tweet.user.setTimeZone( doc.get("user_timeZone") );
		tweet.user.setRegistrationDate( 
				new Timestamp( Long.parseLong( doc.get("user_registrationDate") ) ) );
		tweet.user.setFollowersCount( Integer.parseInt( doc.get("user_followersCount") ) );
		tweet.user.setFriendsCount( Integer.parseInt( doc.get("user_friendsCount") ) );
		tweet.user.setTweetsCount( Integer.parseInt( doc.get("user_tweetsCount") ) );
		tweet.user.setFavouritesCount( Integer.parseInt( doc.get("user_favouritesCount") ) );
		tweet.user.setListedCount( Integer.parseInt( doc.get("user_listedCount") ) );
		tweet.user.setVerified( Boolean.parseBoolean( doc.get("user_isVerified") ) );
		tweet.user.setTranslator( Boolean.parseBoolean( doc.get("user_isTranslator") ) );

		return tweet;
	}

	public Directory getDir() {
		return dir;
	}

	public void setDir(Directory dir) {
		this.dir = dir;
	}



}
















