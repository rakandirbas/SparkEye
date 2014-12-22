package de.uni.konstanz.tests;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

public class LoggingTest {
	private static Logger logger = Logger.getLogger(LoggingTest.class);
	
	public static void main(String[] args) {
		logger.info("Something to log!!!");
		logger.fatal("Something FATAL to log!!!");
		File ff = new File("/asdfasdf");
		try {
			 IndexReader reader = DirectoryReader.open(FSDirectory.open(ff));
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Hell yeah", e);
		}
		logger.warn("MMMMMM2222MM.");
	}
}
