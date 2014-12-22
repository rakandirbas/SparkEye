package de.uni.konstanz.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.apache.log4j.Logger;

public class FileUtils {
	
	private static Logger logger = Logger.getLogger(FileUtils.class);
	
	public static BufferedWriter getFileWriter( File file ) {
		Charset charset = Charset.forName("UTF-8");
		Path path = Paths.get(file.getAbsolutePath());
		BufferedWriter bw = null;
		try {
			bw = Files.newBufferedWriter(path, charset);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error( "Error while opening a file to write", e );
		}
		
		return bw;
		
	}
	
	public static BufferedWriter getFileWriter( File file, boolean append ) {
		Charset charset = Charset.forName("UTF-8");
		Path path = Paths.get(file.getAbsolutePath());
		BufferedWriter bw = null;
		
		if ( append ) {
			try {
				bw = Files.newBufferedWriter(path, charset, StandardOpenOption.APPEND);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error( "Error while opening a file to write", e );
			}
			
			return bw;
		}
		else {
			return getFileWriter(file);
		}
		
	}
	
	public static BufferedReader getFileReader( File file ) {
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(file.getAbsoluteFile());
			br = new BufferedReader(fr);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error( "Error while opening a file to read", e );
		}
		
		return br;
	}
	
	public static BufferedReader getFileReaderIgnoreEncoding( File file ) {
		FileInputStream input = null;
		BufferedReader br = null;
		
		try {
			input = new FileInputStream(file);
			CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
	        decoder.onMalformedInput(CodingErrorAction.IGNORE);
	        InputStreamReader reader = new InputStreamReader(input, decoder);
	        br = new BufferedReader( reader );;

		} catch (IOException e) {
			e.printStackTrace();
			logger.error( "Error while opening a file to read", e );
		}
		
		return br;
	}
	
	public static BufferedWriter getFileWriterIgnoreEncoding( File file ) {
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error( "Error while opening a file to write", e );
		}
		
		return bw;
	}

}
