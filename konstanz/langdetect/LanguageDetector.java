package de.uni.konstanz.langdetect;

import java.util.List;

import de.uni.konstanz.models.Token;

public interface LanguageDetector {
	
	/**
	 * detect if the language of the text belongs to the specified language code.
	 * @param text
	 * @param languageCode
	 * @return
	 */
	public boolean detect( String text, String languageCode );
	/**
	 * detect if the language of the tokens belongs to the specified language code.
	 * @param tokens
	 * @param languageCode
	 * @return
	 */
	public boolean detect( List<Token> tokens, String languageCode );
	/**
	 * returns how confident the method is that the tokens belong to the language
	 * @param tokens
	 * @param languageCode
	 * @return
	 */
	public double detectionConfidence( List<Token> tokens, String languageCode );

}
