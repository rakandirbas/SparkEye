package de.uni.konstanz.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

public class AnalyzerUtils {
	
	public static Analyzer analyzer = new EnglishAnalyzer( Version.LUCENE_43 );

	public static void displayTokens( Analyzer analyzer, String text )
			throws IOException {
		displayTokens( analyzer.tokenStream("content", new StringReader(text)) );

	}

	public static void displayTokens( TokenStream stream ) 
			throws IOException {
		CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);
		stream.reset();
		while(stream.incrementToken()) {
			System.out.print("[" + term.toString() + "] ");
		}
	}

	public static List<String> getTokens( Analyzer analyzer, String text )
			throws IOException {
		List<String> tokens = new ArrayList<String>();
		TokenStream stream = 
				analyzer.tokenStream("content", new StringReader(text));

		CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);
		stream.reset();
		while(stream.incrementToken()) {
			tokens.add(term.toString());
		}

		return tokens;
	}
	
	public static String getTokenedText( Analyzer analyzer, String text )
			throws IOException {
		String tokenedText = "";
		TokenStream stream = 
				analyzer.tokenStream("content", new StringReader(text));

		CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);
		stream.reset();
		while(stream.incrementToken()) {
			tokenedText = tokenedText + " " + term.toString();
		}
		return tokenedText.trim();
	}
	
	public static String filterKeyword( Analyzer analyzer, String keyword )
			throws IOException {
		String result = "";
		TokenStream stream = 
				analyzer.tokenStream("content", new StringReader(keyword));
		
		CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);
		stream.reset();
		while(stream.incrementToken()) {
			result = term.toString();
		}
		
		return result;
	}

}
