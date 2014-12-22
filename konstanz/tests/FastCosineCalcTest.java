package de.uni.konstanz.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uni.konstanz.analysis.FastCosineSimilarityCalculator;
import de.uni.konstanz.models.TermFreq;
import de.uni.konstanz.models.Token;

public class FastCosineCalcTest {

	public static void main(String[] args) {

		String s1 = "Rakan";
		String s2 = "Rakan Dirbas";
		int totalNumberOfDocs = 2;
		FastCosineSimilarityCalculator cosCal = 
				new FastCosineSimilarityCalculator();

		Map<String, TermFreq> terms = new HashMap<String, TermFreq>();
		updateTermsList(s1.split(" "), terms);
		updateTermsList(s2.split(" "), terms);
		List<Token> tokens1 = getTokensList(s1.split(" "));
		List<Token> tokens2 = getTokensList(s2.split(" "));

		Map<String, Double> weightsMap1 = 
				cosCal.getWeightsMap(tokens1, totalNumberOfDocs, terms);
		
		System.out.println("Printing the weights of the first sentence: ");
		for ( String term : weightsMap1.keySet() ) {
			double weight = weightsMap1.get(term);
			System.out.println( "Term: " + term + ", weight: " + weight );
		}
		
		Map<String, Double> weightsMap2 = 
				cosCal.getWeightsMap(tokens2, totalNumberOfDocs, terms);
		
		System.out.println("Printing the weights of the second sentence: ");
		for ( String term : weightsMap2.keySet() ) {
			double weight = weightsMap2.get(term);
			System.out.println( "Term: " + term + ", weight: " + weight );
		}

		double smiliarity = cosCal.getCosineSimilarity(weightsMap1, weightsMap2);
		System.out.println("Similarity: " + smiliarity);
		System.out.println("Done.");

	}

	/**
	 * updates the terms list with the terms/tokens of a document
	 * @param tokens
	 * @param terms
	 */
	public static void updateTermsList( String[] tokens, 
			Map<String, TermFreq> terms  ) {
		Set<String> termsSet = new HashSet<String>();
		for ( String token : tokens ) {
			if ( terms.containsKey(token) ) {
				TermFreq tF = terms.get(token);
				int termFreq = tF.getTotalFrequency();
				termFreq++;
				tF.setTotalFrequency(termFreq);
				terms.put(token, tF);
			}
			else {
				TermFreq tF = new TermFreq();
				tF.setTotalFrequency(1);
				terms.put(token, tF);
			}
			termsSet.add(token);
		}

		for ( String token : termsSet ) {
			TermFreq tF = terms.get( token );
			int docFreq = tF.getDocFrequency();
			docFreq++;
			tF.setDocFrequency(docFreq);
			terms.put(token, tF);
		}

	}

	public static List<Token> getTokensList( String[] terms ) {
		List<Token> tokens = new ArrayList<Token>();

		for ( String term : terms ) {
			Token token = new Token(term, "NOUN");
			tokens.add(token);
		}

		return tokens;
	}

}
