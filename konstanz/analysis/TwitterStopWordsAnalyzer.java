package de.uni.konstanz.analysis;

import java.io.Reader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;

/**
 * This analyzer only removes stopwords 
 * @author rockyrock
 *
 */

public class TwitterStopWordsAnalyzer extends Analyzer {
	
	public static final CharArraySet stopList;
	static {
		
		CharArraySet DEFAULT_STOP_SET = StandardAnalyzer.STOP_WORDS_SET;
		Iterator iter = DEFAULT_STOP_SET.iterator();
		Set<String> stopWords = new HashSet<String>();
		while(iter.hasNext()) {
		    char[] stopWord = (char[]) iter.next();
		    stopWords.add(new String (stopWord));
		}
		stopWords.add( "rt" );
		stopList = new CharArraySet( Version.LUCENE_43, stopWords, false );
	}
	
	@Override
	protected TokenStreamComponents createComponents(String fieldName,
			Reader reader) {
		Tokenizer tokenizer = new KeywordTokenizer(reader);
		
		TokenStream stream = 
				new StopFilter( Version.LUCENE_43, tokenizer, stopList );

		return  new TokenStreamComponents(tokenizer, stream);
	}
}
