package de.uni.konstanz.analysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class FastLangDetector {

	public static final String fileEnglishWords = 		"/languageDetection/en3000.txt";
	public static final String filePortuguesWords = 	"/languageDetection/por3000.txt";
	public static final String fileIndonesianWords = 	"/languageDetection/ind3000.txt";
	public static final String fileSpanishWords = 		"/languageDetection/spa3000.txt";
	public static final String fileDutchWords = 		"/languageDetection/nl3000.txt";
	public static final String fileFrenchWords = 		"/languageDetection/fr3000.txt";
	public static final String fileGermanWords = 		"/languageDetection/de3000.txt";
	public static final String fileMalayWords = 		"/languageDetection/msa3000.txt";

	public final Map<String, Double> englishProbs;
	public final Map<String, Double> nonEnglishProbs;

	// a pattern for to decide whether a string contains only latin characters or not
	private final Pattern latinPattern;

	public FastLangDetector(){

		latinPattern = Pattern.compile("\\p{InBasic_Latin}*"); //only Latin characters. see: http://www.regular-expressions.info/unicode.html

		englishProbs = new HashMap<String, Double>();
		nonEnglishProbs = new HashMap<String, Double>();

		try {

			String line;
			String[] arr;

			double value;

			double minEnglish = + 1000000000d;
			double maxEnglish = - 1.0d;

			double minNonEnglish = + 1000000000d;
			double maxNonEnglish = - 1.0d;

			//import english words
			InputStream inStream = FastLangDetector.class.getResourceAsStream(fileEnglishWords);
			InputStreamReader inReader = new InputStreamReader(inStream,Charset.forName("UTF8"));
			BufferedReader reader = new BufferedReader(inReader);

			//read the data
			while((line=reader.readLine())!=null){
				if(!line.startsWith("//")){
					arr = line.split("\t");
					value = Double.parseDouble(arr[1]);
					if(value > maxEnglish) maxEnglish = value;
					if(value < minEnglish) minEnglish = value;
					englishProbs.put(arr[0].toLowerCase(), value);
				}
			}

			reader.close();
			inReader.close();
			inStream.close();

			//normalize to probabilities:
			for(String s : englishProbs.keySet()){
				englishProbs.put(s, 0.1 + englishProbs.get(s) * ((1-0.1) / (maxEnglish-minEnglish)));
			}
			//			
			//			//import non-english words
			String[] files = new String[]{
					filePortuguesWords,
					fileIndonesianWords,
					fileSpanishWords,
					fileDutchWords,
					fileFrenchWords,
					fileGermanWords,
					fileMalayWords
			};


			for(String file : files){
				inStream = FastLangDetector.class.getResourceAsStream(file);
				inReader = new InputStreamReader(inStream,Charset.forName("UTF8"));
				reader = new BufferedReader(inReader);

				while((line=reader.readLine())!=null){
					if(!line.startsWith("//")){
						arr = line.split("\t");
						value = Double.parseDouble(arr[1]);
						if(value > maxNonEnglish) maxNonEnglish = value;
						if(value < minNonEnglish) minNonEnglish = value;
						nonEnglishProbs.put(arr[0].toLowerCase(), value);
					}
				}

				reader.close();
				inReader.close();
				inStream.close();
			}

			//normalize to probabilities:
			for(String s : nonEnglishProbs.keySet()){
				nonEnglishProbs.put(s, 0.1 + nonEnglishProbs.get(s) * ((1-0.1) / (maxNonEnglish-minNonEnglish)));
			}


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public boolean isEnglishSentence(String[] sentenceTokens, String sentence) {

		//check if only latin characters are usede
		if(!latinPattern.matcher(sentence).matches())
			return false; 

		return getEnglishProbability(sentenceTokens) > getNonEnglishProbability(sentenceTokens);

	}

	private double getEnglishProbability(String[] sentenceTokens){

		double prob = 1.0;

		for(String token : sentenceTokens){
			if(englishProbs.containsKey(token)){
				prob *= englishProbs.get(token);
			}else{
				prob *= 0.01;
			}
		}

		return prob;
	}

	private double getNonEnglishProbability(String[] sentenceTokens){

		double prob = 1.0;

		for(String token : sentenceTokens){
			if(nonEnglishProbs.containsKey(token)){
				prob *= nonEnglishProbs.get(token);
			}else{
				prob *= 0.01;
			}
		}

		return prob;
	}

	public boolean isGermanSentence(String[] sentenceTokens, String sentence) {

		//check if only latin characters are used
		throw new UnsupportedOperationException("Not supported at the moment.");
	}

	public boolean containsNonLatinCharacters(String sentence) {

		return !latinPattern.matcher(sentence).matches();
	}

	public static void main(String[] args) {
		FastLangDetector detector = new FastLangDetector();
		//String text = "This is an English text";
		String text = "Ich will in mein Haus gehen"; 
		System.out.println( detector.isEnglishSentence(text.split(" "), text) );
	}
	
}
