package de.uni.konstanz.tests;

import java.util.List;

import de.uni.konstanz.analysis.Twokenize;


public class TwitterTokenizerTest {
	public static void main(String[] args) {
		Twokenize tokenizer = new Twokenize();
		String text = "A New York #revolution http://hi.com in 5s in new york rakan@rakblog.com software technology has 4";
		List<String> tokens =  tokenizer.tokenize(text);
		
		for ( String token : tokens ) {
			System.out.println( token );
		}
		
	}
}
