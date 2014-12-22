package de.uni.konstanz.analysis;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.en.PorterStemFilter;

public class StemmingAnalyzer extends Analyzer {

	@Override
	protected TokenStreamComponents createComponents(String fieldName,
			Reader reader) {
		Tokenizer tokenizer = new KeywordTokenizer(reader);
		
		TokenStream stream = 
				new PorterStemFilter( tokenizer );

		return  new TokenStreamComponents(tokenizer, stream);
	}

}
