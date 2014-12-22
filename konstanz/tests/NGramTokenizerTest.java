package de.uni.konstanz.tests;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.ngram.NGramTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class NGramTokenizerTest {
	public static void main(String[] args) throws IOException {
		StringReader reader = new StringReader("This is a test man");
		NGramTokenizer tokenizer = new NGramTokenizer(reader, 1, 3);
		CharTermAttribute charTermAttribute = tokenizer.addAttribute(CharTermAttribute.class);
		tokenizer.reset();
		while (tokenizer.incrementToken()) {
		    String token = charTermAttribute.toString();
		    System.out.println(token);
		}
	}
}
