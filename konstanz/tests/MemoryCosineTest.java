package de.uni.konstanz.tests;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.linear.RealVector;

import de.uni.konstanz.analysis.MemoryCosineSimilarityCalculator;
import de.uni.konstanz.models.TermFreq;

public class MemoryCosineTest {
	public static void main(String[] args) {
		String s1 = "America is a really good google place";
		String s2 = "I love America";
		int totalNumberOfDocs = 2;
		MemoryCosineSimilarityCalculator cosCal = new
				MemoryCosineSimilarityCalculator();
		
		Map<String, TermFreq> terms = new HashMap<String, TermFreq>();
		updateTermsList(s1.split(" "), terms);
		updateTermsList(s2.split(" "), terms);
		
		RealVector v1 = cosCal.getWeightsVector(s1.split(" "), totalNumberOfDocs, terms);
		RealVector v2 = cosCal.getWeightsVector(s2.split(" "), totalNumberOfDocs, terms);
		//System.out.println( "V1: " + v1 );
		//System.out.println( "V2: " + v2 );
		System.out.println( "Similarity: " + cosCal.getCosineSimilarity(v1, v2) );
		
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
}


