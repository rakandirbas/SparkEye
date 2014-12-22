package de.uni.konstanz.langdetect;

import java.util.ArrayList;
import java.util.List;

import de.uni.konstanz.analysis.Preprocessor;
import de.uni.konstanz.models.Token;

/**
 * Probability based language detector
 * @author rockyrock
 *
 */

public class PBLanguageDetector implements LanguageDetector {
	
	public static LanguageDetector detector = new PBLanguageDetector();
	
	public static void main(String[] args) {
		String x = "RT @JustForLOLz1 My name is Bond, James Bond. My name is Please, Bitch Please. < LOOOOOOL";
		LanguageDetector detector = new PBLanguageDetector();
		System.out.println( detector.detect(x, "") );
	}

	@Override
	public boolean detect(String text, String languageCode) {
		List<String> regexList = new ArrayList<String>();
		regexList.add(Preprocessor.hashtagRegex);
		regexList.add(Preprocessor.urlRegex);
		regexList.add(Preprocessor.mentionRegex);
		List<Token> tokens = Preprocessor.getTokensListWithoutPOS(text, regexList);
		return detect(tokens, languageCode);
	}

	@Override
	public boolean detect(List<Token> tokens, String languageCode) {
		double confidence = detectionConfidence(tokens, languageCode);
		
		if ( confidence > 0.5 )
			return true;
		
		return false;
	}

	//Only arabic is supported
	@Override
	public double detectionConfidence(List<Token> tokens, String languageCode) {
		int arabicTokens = 0;
		int totalTokens = tokens.size();
		double confidence = 0;
		
		for ( Token token : tokens ) {
			String tokenText = token.getToken();
			
			if ( tokenText.matches("\\p{InArabic}+") )
				arabicTokens++;
		}
		
		confidence = (double) arabicTokens / totalTokens;
		
		return confidence;
	}
	
	

}
