package de.uni.konstanz.analysis;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import de.uni.konstanz.models.TermFreq;

public class MemoryCosineSimilarityCalculator {

	/**
	 * Calculates the tf/idf weights vector of a given documents' tokens.
	 * Calculations are tested and works as CosineSimeTest
	 * 
	 * @param documentText
	 * @param totalNumberOfDocs
	 * @return
	 * @throws IOException
	 */
	public Map<String, Double> getWeightsMap(String[] documentTokens, 
			int totalNumberOfDocs, Map<String, TermFreq> terms ) {
		Map<String, Integer> termFrequencies = new HashMap<>();
		Map<String, Double> tf_Idf_Weights = new HashMap<>();

		for ( String term : documentTokens ) {
			if ( termFrequencies.containsKey(term) ) {
				int freq = termFrequencies.get(term);
				freq++;
				termFrequencies.put(term, freq);
			}
			else {
				termFrequencies.put( term , 1);
			}
		}

		for ( String term : termFrequencies.keySet() ) {
			int tf = termFrequencies.get(term);
			int df = terms.get(term).getDocFrequency();
			double idf = ( 1 + Math.log10( totalNumberOfDocs ) - Math.log10(df) );
			double w = tf * idf;
			tf_Idf_Weights.put(term, w);
		}
		tf_Idf_Weights = updateWeightsMap( tf_Idf_Weights, terms );
		return tf_Idf_Weights;
	}

	public Map<String, Double> updateWeightsMap(Map<String, Double> weightsMap, 
			Map<String, TermFreq> terms) {
		Map<String, Double> map = new HashMap<String, Double>();
		for (String term : terms.keySet()) {

			if ( weightsMap.containsKey(term) ) {
				map.put( term, weightsMap.get(term) );
			}
			else {
				map.put( term, 0.0 );
			}
		}
		return map;
	}

	public RealVector getWeightsVector( String[] documentTokens, 
			int totalNumberOfDocs, Map<String, TermFreq> terms ) {
		Map<String, Double> tf_Idf_Weights = 
				getWeightsMap(documentTokens, totalNumberOfDocs, terms);
		return toRealVector(tf_Idf_Weights);
	}

	public RealVector toRealVector(Map<String, Double> tfIdf_weightsMap) {
		RealVector vector = new ArrayRealVector(tfIdf_weightsMap.size());
		int i = 0;
		double value = 0;
		for (String term : tfIdf_weightsMap.keySet()) {
			value = tfIdf_weightsMap.get(term);
			vector.setEntry(i++, value);
		}
		return vector;
	}

	//	public RealVector toRealVector(Map<String, Double> tfIdf_weightsMap, 
	//			Map<String, TermFreq> terms) {
	//		RealVector vector = new ArrayRealVector(terms.size());
	//		int i = 0;
	//		double value = 0;
	//		for (String term : terms.keySet()) {
	//
	//			if ( tfIdf_weightsMap.containsKey(term) ) {
	//				value = tfIdf_weightsMap.get(term);
	//			}
	//			else {
	//				value = 0;
	//			}
	//			vector.setEntry(i++, value);
	//		}
	//		return vector;
	//	}

	public double getCosineSimilarity( RealVector v1, RealVector v2 ) {
		//System.out.println(v1);
		//System.out.println(v2);
		double dotProduct = v1.dotProduct(v2);
		//System.out.println( "dotProduct: " + dotProduct );
		double normalization = (v1.getNorm() * v2.getNorm());
		//System.out.println( "normalization: " + normalization );
		return dotProduct / normalization;
	}

}
