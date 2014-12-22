package de.uni.konstanz.analysis;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

/**
 * This analyzer just removes stop words and apply stemming
 * @author rockyrock
 *
 */

public class SparkEyeSimpleAnalyzer extends Analyzer {

	@Override
	protected TokenStreamComponents createComponents(String fieldName,
			Reader reader) {
		Tokenizer tokenizer = new KeywordTokenizer(reader);
		
		TokenStream stream = 
				new StopFilter( Version.LUCENE_43, tokenizer, StandardAnalyzer.STOP_WORDS_SET );
		stream = new PorterStemFilter( stream );

		return  new TokenStreamComponents(tokenizer, stream);
	}

}
