package de.uni.konstanz.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.List;

import de.uni.konstanz.analysis.Preprocessor;
import de.uni.konstanz.models.Token;
import de.uni.konstanz.models.Tweet;
import de.uni.konstanz.utils.FileUtils;

public class CSVStream {

	private CSVListener csvListener;
	private File pathToCSVFile;
	private boolean isStreamFinished = false;

	public CSVStream( CSVListener csvListener, File pathToCSVFile ) {
		this.csvListener = csvListener;
		this.pathToCSVFile = pathToCSVFile;
	}

	public void startStream() {
		BufferedReader reader = FileUtils.getFileReader(pathToCSVFile);
		String line = "";
		try {
			for ( int i = 0; ((line = reader.readLine()) != null); i++ ) {

				if ( i == 0)
					continue;
				
				String values[] = line.split( "\t" );
				
				if ( values.length == 25 ) {
					
					//Do temp stuff here ... clean after done
					boolean cont = false;
					List<Token> tokens = Preprocessor.preprocessor.getFastTokensList( values[2].toLowerCase() );
					for ( Token token : tokens ) {
						if( token.getToken().equals("chemical") ) {
							Tweet tweet = CSVDao.fromCSVLineToTweet(line);
							if(tweet == null)
								System.out.println("Boo");
							csvListener.onReceiving(tweet);
							cont = true;
						}
						
						if(cont)
							break;
					}
					
//					if ( !values[5].equals("null") ) {
//						String[] cords = values[5].split(",");
//						double lat = Double.parseDouble(cords[0]);
//						double lont = Double.parseDouble(cords[1]);
//						double distance = GeoUtils.distance(42.358056, -71.063611, 
//								lat, lont, "K");
//						if ( distance < 300 ) {
//							Tweet tweet = CSVDao.fromCSVLineToTweet(line);
//							if ( tweet != null ) {
//								csvListener.onReceiving(tweet);
//							}
//						}
//					}
					
					//End temp stuff here ... end cleaning here and uncomment after
					
					
//					Tweet tweet = CSVDao.fromCSVLineToTweet(line);
//					if ( tweet != null ) {
//						csvListener.onReceiving(tweet);
//					}
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("The stream is over.");
		isStreamFinished = true;
		
		try {
			reader.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isStreamOver() {
		return isStreamFinished;
	}

}




















