package de.uni.konstanz.classifiers;

import uk.ac.wlv.sentistrength.SentiStrength;

public class SentimentClassifier {

	public static String inis[] = new String[]{"sentidata",
		"resources/sentistrength/SentStrength_Data_Sept2011/",
	"trinary"};
	
	public static String inis2[] = new String[]{"sentidata",
		"resources/sentistrength/SentStrength_Data_Sept2011/"};
	
	public static SentiStrength sentiStrengthForClassification = new SentiStrength();
	public static SentiStrength sentiStrength = new SentiStrength();

	static {
		sentiStrengthForClassification.initialise(inis);
		sentiStrength.initialise(inis2);
	}

	/**
	 * Classify a text as either positive, negative or neutral text.
	 * @param tweet
	 * @return +1 positive, -1 negative, 0 neutral. 
	 */
	public static int classify( String text ) {

		String result = sentiStrengthForClassification.computeSentimentScores(text);
		int classRes = Integer.parseInt( result.split(" ")[2] );

		if ( classRes == 1 )
			return 1;
		else if ( classRes == -1 )
			return -1;
		else 
			return 0;

	}
	
	/**
	 * Returns the positive and negative sentiment scores in an array.
	 * First index of the array is for positive sentiment and the second is
	 * for negative sentiment.
	 * @param text
	 * @return First index of the array is for positive sentiment score and the second is
	 * for negative sentiment score.
	 */
	public static int[] getStrengths( String text ) {
		int scores[] = new int[2];

		String result = sentiStrength.computeSentimentScores(text);
		scores[0] = Integer.parseInt( result.split(" ")[0].trim() );
		scores[1] = Integer.parseInt( result.split(" ")[1].trim() );
		
		return scores;
	}
	
	public static void main(String[] args) {
		String text = "I really love you but dislike your cold sister";
		String result = sentiStrength.computeSentimentScores(text);
		System.out.println(result);
		
		int scores[] = getStrengths(text);
		for ( int i : scores ) {
			System.out.println(i);
		}
		
	}

}
