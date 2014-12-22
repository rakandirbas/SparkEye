package de.uni.konstanz.csvtasks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import cmu.arktweetnlp.Tagger.TaggedToken;
import de.uni.konstanz.analysis.Preprocessor;
import de.uni.konstanz.analysis.Twokenize;
import de.uni.konstanz.models.Token;
import de.uni.konstanz.utils.FileUtils;
import de.uni.konstanz.utils.MapUtils;

public class CSVToWordsCount implements CSVTask {

	private static Logger logger = Logger.getLogger(CSVToWordsCount.class);

	private Map<String, Integer> keywordsFreqs = 
			new LinkedHashMap<String, Integer>();

	@Override
	public File runTask(File path, File outputDir) {
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
		outputFileName += "_wordsCount.csv";
		File outputFile = new File( outputFileName );
		BufferedWriter writer = FileUtils.getFileWriter(outputFile);
		String line = "";

		try {
			for ( int i = 0; ((line = reader.readLine()) != null); i++ ) {

				if ( i == 0) {
					continue;
				}

				String[] columns = line.split("\t");
				if ( columns.length == 25 ) {
					String text = columns[2];
					text = text.toLowerCase();
					List<Token> tokensList = getTokensListWithoutPOS(text);
					
					for ( Token token : tokensList ) {
						if ( keywordsFreqs.containsKey(token.getToken()) ) {
							int freq = keywordsFreqs.get(token.getToken());
							freq++;
							keywordsFreqs.put(token.getToken(), freq);
						}
						else {
							keywordsFreqs.put(token.getToken(), 1);
						}
					}
					
				}
			}
			
			keywordsFreqs = MapUtils.sortByValue(keywordsFreqs, true);
			
			int i = 1;
			for ( String term : keywordsFreqs.keySet() ) {
				int freq = keywordsFreqs.get(term);
				writer.write(String.format( "%d,%s,%d\n", i, term, freq ));
				i++;
			}
			
		} catch (IOException e) {
			logger.error("Error while reading a line " +
					"during counting words frequencies in csv file.", e);
			e.printStackTrace();
		}
		finally{
			try {
				reader.close();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error( "Error while closing the reader or writer" +
						"after counting the words frequencies CSV file." );
			}

		}

		return outputFile;
	}
	
	@Override
	public String toString() {
		return "CSVToWordsCount";
	}
	
	public static List<Token> getTokensListWithPOS( String text ) {
		text = text.toLowerCase();
		List<Token> tokensList = new ArrayList<Token>();
		
		
		List<TaggedToken> taggedTokens = Preprocessor.preprocessor.
				posTagger.tokenizeAndTag(text);
		for (TaggedToken token : taggedTokens) {
			if ( Preprocessor.isInterestingToken(token) ) {
				String tokenTxt = token.token;
				tokenTxt = tokenTxt.replaceAll("[^a-zA-Z]", "");
				Token iToken = new Token(tokenTxt, token.tag);
				tokensList.add(iToken);
			}
		}

		Matcher m = Preprocessor.hashtagPattern.matcher(text);
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
	
	public static String urlRegex = "http+://[\\S]+|https+://[\\S]+";
	public static Pattern urlPattern = Pattern.compile(urlRegex);
	public static Twokenize tokenizer = new Twokenize();
	
	public static List<Token> getTokensListWithoutPOS( String text ) {
		text = text.toLowerCase();
		
		List<Token> tokensList = new ArrayList<Token>();
		
		for ( String token : tokenizer.tokenize(text) ) {
			if ( token.matches(urlRegex) ) {
				continue;
			}
			
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

}
