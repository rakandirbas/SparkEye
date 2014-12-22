package de.uni.konstanz.tests;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.linear.RealVector;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import de.uni.konstanz.analysis.LuceneCosineSimilarityCalculator;
import de.uni.konstanz.models.TermFreq;

public class LuceneCosineTest {
	
	/**
	 * IMPORTANT: Make sure to use the same analyzer while 
	 * putting the terms into the terms list, as also when
	 * indexing the documents into Lucene.
	 */
	
	public static final FieldType TYPE_STORED = new FieldType();
	public static final String FIELD_NAME = "Content";

    static {
        TYPE_STORED.setIndexed(true);
        TYPE_STORED.setTokenized(true);
        TYPE_STORED.setStored(true);
        TYPE_STORED.setStoreTermVectors(true);
        TYPE_STORED.setStoreTermVectorPositions(true);
        TYPE_STORED.freeze();
    }

	public static void main(String[] args) throws IOException {
		String s1 = "This is good and wow and great";
		String s2 = "This is good but hey";
		int totalNumberOfDocs = 2;
		LuceneCosineSimilarityCalculator cosCal = new
				LuceneCosineSimilarityCalculator();
		
		Map<String, TermFreq> terms = new HashMap<String, TermFreq>();
		updateTermsList(s1.split(" "), terms);
		updateTermsList(s2.split(" "), terms);
		
		Directory directory = createIndex(s1, s2);
		IndexReader reader = DirectoryReader.open(directory);
		
		//Make Lucene get docId
		RealVector v1 = cosCal.getWeightsVector(reader, 0, totalNumberOfDocs, FIELD_NAME, terms);
		RealVector v2 = cosCal.getWeightsVector(reader, 1, totalNumberOfDocs, FIELD_NAME, terms);
		
		
		System.out.println( "V1: " + v1 );
		System.out.println( "V2: " + v2 );
		System.out.println( "Similarity: " + cosCal.getCosineSimilarity(v1, v2) );
	}
	
	
	public static Directory createIndex(String s1, String s2) throws IOException {
        Directory directory = new RAMDirectory();
        Analyzer analyzer = new WhitespaceAnalyzer(Version.LUCENE_CURRENT);
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_CURRENT,
                analyzer);
        IndexWriter writer = new IndexWriter(directory, iwc);
        addDocument(writer, s1);
        addDocument(writer, s2);
        writer.close();
        return directory;
    }
	
	public static void addDocument(IndexWriter writer, String content) throws IOException {
        Document doc = new Document();
        Field field = new Field(FIELD_NAME, content, TYPE_STORED);
        doc.add(field);
        writer.addDocument(doc);
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
