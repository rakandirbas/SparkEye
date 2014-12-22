package de.uni.konstanz.analysis;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import cmu.arktweetnlp.Tagger;
import cmu.arktweetnlp.Tagger.TaggedToken;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;

import de.uni.konstanz.Parameters;
import de.uni.konstanz.locationextractors.TableBasedLocationExtractor;
import de.uni.konstanz.models.Token;
import de.uni.konstanz.models.Tweet;
import de.uni.konstanz.utils.AnalyzerUtils;

public class Preprocessor {

	public static Preprocessor preprocessor = 
			new Preprocessor();

	public static Analyzer sparkEyeAnalyzer =
			new SparkEyeAnalyzer();
	public static Analyzer simpleSparkEyeAnalyzer = 
			new SparkEyeSimpleAnalyzer();
	public static Analyzer stopWordsAnalyzer =
			new TwitterStopWordsAnalyzer();
	public static Analyzer stemmingAnlyzer = 
			new StemmingAnalyzer();

	public static String hashtagRegex = "^#\\w+|\\s#\\w+";
	public static Pattern hashtagPattern = Pattern.compile(hashtagRegex);

	public static String urlRegex = "http+://[\\S]+|https+://[\\S]+";
	public static Pattern urlPattern = Pattern.compile(urlRegex);

	public static String mentionRegex = "^@\\w+|\\s@\\w+";
	public static Pattern mentionPattern = Pattern.compile(mentionRegex);

	public static final Pattern latinPattern = 
			Pattern.compile("\\p{InBasic_Latin}*");
	public Tagger posTagger;
	public static Twokenize tokenizer = new Twokenize();

	private static Logger logger = Logger.getLogger(Preprocessor.class);

	public Preprocessor() {
		try {
			DetectorFactory.loadProfile( System.getProperty("user.dir") + "/profiles" );
		} catch (LangDetectException e) {
			e.printStackTrace();
		}

		posTagger = new Tagger();
		try {
			posTagger.loadModel(Parameters.POSTaggerModelFileName);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Error while loading the POS tagger model file.", e);
		}
	}

	public LinkedList<Tweet> filter( List<Tweet> tweets ) {
		LinkedList<Tweet> filteredList = new LinkedList<Tweet>();
		for ( Tweet tweet : tweets ) {
			if ( pass( tweet.getTokenedText() ) )
				filteredList.add(tweet);
		}

		return filteredList;
	}

	public boolean pass( String text ) {
		boolean pass = false;
		if ( !text.equals("") ) {

			//check if only latin characters are used
			if(!latinPattern.matcher(text).matches())
				return false;

			try {
				Detector langDetector = DetectorFactory.create();
				langDetector.append(text);
				if ( langDetector.detect().equals("en") ) {
					//System.out.println("DETECTED: " + text);
					pass = true;
				}
				else
					pass = false;
			} catch (LangDetectException e) {
				//e.printStackTrace();
				//System.err.println( "Text: " + text );
			}
		}
		return pass;
	}
	
	public List<Token> getFastTokensList(String text) {
		List<Token> tokensList = new ArrayList<Token>();
		
		for (String token : Twokenize.tokenize(text.toLowerCase()) ) {
			tokensList.add(new Token(token, "I"));
		}
		
		return tokensList;
	}

	public List<Token> getTokensList( String text ) throws IOException {
		List<Token> tokensList = new ArrayList<Token>();

		////////////THE OLD WAY
		//		text = AnalyzerUtils.getTokenedText(getAnalyzer(), text);
		//		List<String> proccessedTokens = getTokens(getAnalyzer(), text);
		//		//Apply POS Tagging for each token
		//
		//		for ( String pToken : proccessedTokens ) {
		//			Token token = new Token( pToken, Token.NOUN );
		//			tokensList.add(token);
		//		}
		///////////

		//The new way
		text = text.toLowerCase();
		List<TaggedToken> taggedTokens = posTagger.tokenizeAndTag(text);
		for (TaggedToken token : taggedTokens) {
			if ( isInterestingToken(token) ) {
				String tokenTxt = token.token;
				tokenTxt = tokenTxt.replaceAll("[^a-zA-Z]", "");
				tokenTxt = AnalyzerUtils.filterKeyword(simpleSparkEyeAnalyzer, tokenTxt);
				tokenTxt = tokenTxt.trim();
				if ( !tokenTxt.isEmpty() ) {
					Token iToken = new Token(tokenTxt, token.tag);
					tokensList.add(iToken);
				}
			}
		}

		Matcher m = hashtagPattern.matcher(text);
		List<String> hashtagMatches = new ArrayList<String>();
		while(m.find()){
			String tag = m.group().replace(" ", "");
			tag = tag.replace("#", "");
			hashtagMatches.add(tag);
		}

		for ( String hashTag : hashtagMatches ) {
			Token iToken1 = new Token( hashTag, "#" );
			tokensList.add(iToken1);
			Token iToken2 = new Token( hashTag, "#" );
			tokensList.add(iToken2);
		}

		return tokensList;
	}

	public List<Token> getTokensListWithoutDoubleHashTag( String text ) throws IOException {
		List<Token> tokensList = new ArrayList<Token>();

		text = text.toLowerCase();
		List<TaggedToken> taggedTokens = posTagger.tokenizeAndTag(text);
		for (TaggedToken token : taggedTokens) {
			if ( isInterestingToken(token) ) {
				String tokenTxt = token.token;
				tokenTxt = tokenTxt.replaceAll("[^a-zA-Z]", "");
				tokenTxt = AnalyzerUtils.filterKeyword(simpleSparkEyeAnalyzer, tokenTxt);
				Token iToken = new Token(tokenTxt, token.tag);
				tokensList.add(iToken);
			}
		}

		Matcher m = hashtagPattern.matcher(text);
		List<String> hashtagMatches = new ArrayList<String>();
		while(m.find()){
			String tag = m.group().replace(" ", "");
			tag = tag.replace("#", "");
			hashtagMatches.add(tag);
		}

		for ( String hashTag : hashtagMatches ) {
			Token iToken1 = new Token( hashTag, "#" );
			tokensList.add(iToken1);
		}

		return tokensList;
	}

	public String getTokensAsText( List<Token> tokensList ) {
		String text = "";
		for ( Token token : tokensList ) {
			text = text + " " + token.getToken();

		}
		return text.trim();
	}

	public Analyzer getAnalyzer() {
		return sparkEyeAnalyzer;
	}

	public static List<String> getTokens( Analyzer analyzer, String text )
			throws IOException {
		List<String> tokens = new ArrayList<String>();
		TokenStream stream = 
				analyzer.tokenStream("content", new StringReader(text));

		CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);
		stream.reset();
		//System.out.println( "Text: " + text );
		while(stream.incrementToken()) {
			tokens.add(term.toString());
			//System.out.print( " " +  term.toString() + " ");
		}
		//System.out.println();
		return tokens;
	}

	public static boolean isInterestingToken( TaggedToken token ) {
		boolean state = false;

		if ( token.tag.equals("N") ) //Noun
			state = true;
		else if ( token.tag.equals("^") ) //Proper Noun
			state = true;
		//		else if ( token.tag.equals("#") ) //Hashtag
		//			state = true;
		//		else if ( token.tag.equals("@") ) //Mention
		//			state = true;

		return state;
	}

	/**
	 * Returns the list of tokens in the text. 
	 * The tokens shouldn't match the regexes in the regex list, so if you
	 * pass a URL regex, then a token that's a url will be filtered out. It also
	 * removes any non-letter character! So #text will be  text.
	 * @param text
	 * @param regexList specifies which regex a token shouldn't match.
	 * @return
	 */
	public static List<Token> getTokensListWithoutPOS( String text, List<String> regexList ) {
		text = text.toLowerCase();

		List<Token> tokensList = new ArrayList<Token>();

		for ( String token : tokenizer.tokenize(text) ) {
			boolean shouldBeFiltered = false;
			for ( String filterRegex : regexList ) {
				if ( token.matches(filterRegex) ) {
					shouldBeFiltered = true;
				}
			}

			if ( shouldBeFiltered ) 
				continue;

			token = token.replaceAll("\\P{L}+", " ");
			token = token.trim();

			if ( token.split(" ").length > 1 ) {
				for ( String splittedToken : token.split(" ") ) {
					splittedToken = splittedToken.trim();
					if ( splittedToken.length() > 1 ) {
						Token iToken = new Token(splittedToken, "I");
						tokensList.add(iToken);
					}
				}
			}
			else if ( token.length() > 1 ) {
				Token iToken = new Token(token, "I");
				tokensList.add(iToken);
			}
		}
		return tokensList;
	}

	/**
	 * Takes a string and return a list of tokens where each token
	 * is stemmed. Also removes urls and weired chars, stopwords & mentions.
	 * Hashtags are stripped down from their hash char.
	 * @param text
	 * @return
	 */
	public static List<Token> getClassicTokensList(String text) {
		List<Token> tokensList = TableBasedLocationExtractor.getTokensList(text);
		List<Token> updatedTokensList = new LinkedList<Token>();
		try {
			for ( Token token : tokensList ) {
				String tok = token.getToken();

				tok = AnalyzerUtils.filterKeyword(stemmingAnlyzer, tok);
				if ( tok.length() > 0 ) {
					updatedTokensList.add(new Token(tok, "I"));
				}

			}
		} catch (IOException e) {
			logger.error("Exception while stemming tokens in getClassicTokensList", e);
			e.printStackTrace();
		}

		return updatedTokensList;

	}
	
	/**
	 * Return the text tokened by the Twitter tokenizer
	 * @param text
	 * @return
	 */
	public List<String> getSimpleTokensList(String text) {
		return Twokenize.tokenize(text.toLowerCase());
	}

}



















