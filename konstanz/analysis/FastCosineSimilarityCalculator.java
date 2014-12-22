package de.uni.konstanz.analysis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uni.konstanz.models.TermFreq;
import de.uni.konstanz.models.Token;

public class FastCosineSimilarityCalculator {
	
	//This class is tested and the calculations are correct.
	
	public static final String NORMALIZATION_ID = "NORMALIZATION_VECTOR_ID"; 
	
	public Map<String, Double> getWeightsMap( List<Token> tokens,
			int totalNumOfDocs, Map<String, TermFreq> terms ) {
		Map<String, Integer> termFrequencies = new HashMap<>();
		Map<String, Double> tfIdfWeightsMap = new 
				HashMap<String, Double>();
		double normalization = 0;
		
		for ( Token token : tokens ) {
			String term = token.getToken();
			if ( termFrequencies.containsKey(term) ) {
				int freq = termFrequencies.get(term);
				freq++;
				termFrequencies.put(term, freq);
			}
			else {
				termFrequencies.put( term, 1);
			}
		}
		
		for ( String term : termFrequencies.keySet() ) {
			int tf = termFrequencies.get(term);
			int df = terms.get(term).getDocFrequency();
			double idf = ( 1 + Math.log10( totalNumOfDocs ) - Math.log10(df) );
			double w = tf * idf;
			normalization += w * w;
			tfIdfWeightsMap.put(term, w);
		}
		tfIdfWeightsMap.put(NORMALIZATION_ID, normalization);
		return tfIdfWeightsMap;
		
	}
	
	public double getCosineSimilarity( Map<String, Double> weightsMap1,
			Map<String, Double> weightsMap2 ) {
		double similarity = 0;
		double dotProduct = 0;
		double normalization = 0;
		
		Map<String, Double> shortMap = null;
		Map<String, Double> largeMap = null;
		
		if ( weightsMap1.size() < weightsMap2.size() ) {
			shortMap = weightsMap1;
			largeMap = weightsMap2;
		}
		else {
			shortMap = weightsMap2;
			largeMap = weightsMap1;
		}
		
		for ( String term : shortMap.keySet() ) {
			if ( term.equals(NORMALIZATION_ID) )
				continue;
			if ( largeMap.containsKey(term) ) {
				double d_i = shortMap.get(term);
				double b_i = largeMap.get(term);
				dotProduct += d_i * b_i;
			}
		}
		
		double map1Norm  = Math.sqrt( shortMap.get(NORMALIZATION_ID) );
		double map2Norm = Math.sqrt( largeMap.get(NORMALIZATION_ID) );
		normalization = map1Norm * map2Norm;
		similarity = dotProduct / normalization;
		//System.out.println( "Fast calculation:" );
		//System.out.println( "dotProduct: " + dotProduct );
		//System.out.println( "normalization: " + normalization );
		//System.out.println( "similarity: " + similarity );
		return similarity;
	}

}
