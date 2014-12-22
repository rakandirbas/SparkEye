package de.uni.konstanz.analysis;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.util.BytesRef;

import de.uni.konstanz.models.TermFreq;

public class LuceneCosineSimilarityCalculator {
	
	//IMPORTANT: This class is not updated as of MemoryCosineSimilarity!

	public Map<String, Double> getWeightsMap(IndexReader reader, int docId, 
			int totalNumberOfDocs, String FIELD_NAME )
			throws IOException {
		Terms vector = reader.getTermVector(docId, FIELD_NAME);
		Map<String, Integer> docFrequencies = new HashMap<>();
		Map<String, Integer> termFrequencies = new HashMap<>();
		Map<String, Double> tf_Idf_Weights = new HashMap<>();
		TermsEnum termsEnum = null;
		DocsEnum docsEnum = null;


		termsEnum = vector.iterator(termsEnum);
		BytesRef text = null;
		while ((text = termsEnum.next()) != null) {
			String term = text.utf8ToString();
			int docFreq = reader.docFreq( new Term( FIELD_NAME, term ) );
			docFrequencies.put(term, docFreq);

			docsEnum = termsEnum.docs(null, null);
			while (docsEnum.nextDoc() != DocIdSetIterator.NO_MORE_DOCS) {
				termFrequencies.put(term, docsEnum.freq());
			}

		}

		for ( String term : docFrequencies.keySet() ) {
			int tf = termFrequencies.get(term);
			int df = docFrequencies.get(term);
			double idf = ( 1 + Math.log10( totalNumberOfDocs ) - Math.log10(df) );
			double w = tf * idf;
			tf_Idf_Weights.put(term, w);
		}

		return tf_Idf_Weights;
	}
	
	public RealVector getWeightsVector(IndexReader reader, int docId, 
			int totalNumberOfDocs, String FIELD_NAME, Map<String, TermFreq> terms )
			throws IOException {
		
		Map<String, Double> tf_Idf_Weights = 
				getWeightsMap(reader, docId, totalNumberOfDocs, FIELD_NAME);
		
		return toRealVector(tf_Idf_Weights, terms);
	}
	
	RealVector toRealVector(Map<String, Double> tfIdf_weightsMap, 
			Map<String, TermFreq> terms) {
        RealVector vector = new ArrayRealVector(terms.size());
        int i = 0;
        double value = 0;
        for (String term : terms.keySet()) {
        	
        	if ( tfIdf_weightsMap.containsKey(term) ) {
        		value = tfIdf_weightsMap.get(term);
        	}
        	else {
        		value = 0;
        	}
            vector.setEntry(i++, value);
        }
        return vector;
    }

	public double getCosineSimilarity( RealVector v1, RealVector v2 ) {
		double dotProduct = v1.dotProduct(v2);
		double normalization = (v1.getNorm() * v2.getNorm());
		return dotProduct / normalization;
	}

}
