package de.uni.konstanz.tests;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;

import de.uni.konstanz.analysis.SparkEyeAnalyzer;
import de.uni.konstanz.utils.AnalyzerUtils;

public class StandardAnalyzerTest {
	
	public static void main(String[] args) throws IOException {
		Analyzer analyzer = new SparkEyeAnalyzer();
		//String text = "A #revolution http://hi.com in rakan@rakblog.com software technology has 4 ??? 86.54s.[http://orangenose.appspot.com/h/],http://twitpic.com/c9a4fv";
		String text = "#tweet_4_syrian_child #tweet4SyrianChild";
		AnalyzerUtils.displayTokens(analyzer,  AnalyzerUtils.getTokenedText(analyzer, text) );
		System.out.println("Done.");
	}

}
