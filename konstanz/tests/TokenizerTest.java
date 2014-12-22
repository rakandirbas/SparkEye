package de.uni.konstanz.tests;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.core.TypeTokenFilter;
import org.apache.lucene.analysis.en.EnglishPossessiveFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.UAX29URLEmailTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;

public class TokenizerTest {
	public static void main(String[] args) throws IOException {
		//String text = "A @rakan http://t.co/SFcTH3nWb1 #revolution @ $ ! happened rt RT happens http://hi.com in test@test.com software technology has 4";
		String text = " ??? 86.54s.[http://orangenose.appspot.com/h/],http://twitpic.com/c9a4fv";
		Tokenizer tokenizer = new UAX29URLEmailTokenizer(Version.LUCENE_43,
				new StringReader(text));
		
		Set<String> stopTypes = new HashSet<String>();
		stopTypes.add("<URL>");
		stopTypes.add("<NUM>");
		stopTypes.add("<EMAIL>");
		stopTypes.add("<ALPHANUM>");
		stopTypes.add("<SOUTHEAST_ASIAN>");
		stopTypes.add("<IDEOGRAPHIC>");
		stopTypes.add("<HIRAGANA>");
		
		TokenStream	stream = new TypeTokenFilter(true, tokenizer, stopTypes);
		stream = new StandardFilter( Version.LUCENE_43, stream );
		stream = new EnglishPossessiveFilter( Version.LUCENE_43, stream );
		stream = new LowerCaseFilter( Version.LUCENE_43, stream );
		CharArraySet DEFAULT_STOP_SET = StandardAnalyzer.STOP_WORDS_SET;
		
		Iterator iter = DEFAULT_STOP_SET.iterator();
		Set<String> stopWords = new HashSet<String>();
		while(iter.hasNext()) {
		    char[] stopWord = (char[]) iter.next();
		    stopWords.add(new String (stopWord));
		}
		stopWords.add( "rt" );
		CharArraySet stopList = new CharArraySet( Version.LUCENE_43, stopWords, false  );
		stream = new StopFilter( Version.LUCENE_43, stream, stopList );
		stream = new PorterStemFilter( stream );
		
		
		CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);
		stream.reset();
		while( stream.incrementToken() ) {
			System.out.println( term );
		}

	}
}
