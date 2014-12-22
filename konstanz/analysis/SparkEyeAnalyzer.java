package de.uni.konstanz.analysis;

import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.core.TypeTokenFilter;
import org.apache.lucene.analysis.en.EnglishPossessiveFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.UAX29URLEmailTokenizer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;



public class SparkEyeAnalyzer extends Analyzer {

	public static final  Set<String> stopTypes = new HashSet<String>();
	public static final CharArraySet stopList;
	static {
		stopTypes.add("<URL>");
		stopTypes.add("<NUM>");
		stopTypes.add("<EMAIL>");
		//stopTypes.add("<ALPHANUM>");
		stopTypes.add("<SOUTHEAST_ASIAN>");
		stopTypes.add("<IDEOGRAPHIC>");
		stopTypes.add("<HIRAGANA>");
		
		CharArraySet DEFAULT_STOP_SET = StandardAnalyzer.STOP_WORDS_SET;
		Iterator iter = DEFAULT_STOP_SET.iterator();
		Set<String> stopWords = new HashSet<String>();
		while(iter.hasNext()) {
		    char[] stopWord = (char[]) iter.next();
		    stopWords.add(new String (stopWord));
		}
		stopWords.add( "rt" );
		stopWords.add( "" );
		stopWords.add( "you" );
		stopWords.add( "me" );
		stopWords.add( "he" );
		stopWords.add( "she" );
		stopWords.add( "it" );
		stopWords.add( "him" );
		stopWords.add( "her" );
		stopWords.add( "they" );
		stopWords.add( "your" );
		stopWords.add( "his" );
		stopWords.add( "a" );
		stopWords.add( "b" );
		stopWords.add( "c" );
		stopWords.add( "d" );
		stopWords.add( "e" );
		stopWords.add( "f" );
		stopWords.add( "g" );
		stopWords.add( "h" );
		stopWords.add( "i" );
		stopWords.add( "j" );
		stopWords.add( "k" );
		stopWords.add( "l" );
		stopWords.add( "m" );
		stopWords.add( "n" );
		stopWords.add( "o" );
		stopWords.add( "p" );
		stopWords.add( "q" );
		stopWords.add( "r" );
		stopWords.add( "s" );
		stopWords.add( "t" );
		stopWords.add( "u" );
		stopWords.add( "w" );
		stopWords.add( "x" );
		stopWords.add( "y" );
		stopWords.add( "z" );
		stopList = new CharArraySet( Version.LUCENE_43, stopWords, false );
	}
	
	@Override
	protected TokenStreamComponents createComponents(String fieldName,
			Reader reader) {
		Tokenizer tokenizer = new UAX29URLEmailTokenizer(Version.LUCENE_43,
				reader);
		
		TokenStream	stream = new TypeTokenFilter(true, tokenizer, stopTypes);
		stream = new StandardFilter( Version.LUCENE_43, stream );
		stream = new EnglishPossessiveFilter( Version.LUCENE_43, stream );
		stream = new LowerCaseFilter( Version.LUCENE_43, stream );
		stream = new StopFilter( Version.LUCENE_43, stream, stopList );
		stream = new PorterStemFilter( stream );
		
		return  new TokenStreamComponents(tokenizer, stream);
	}

}
