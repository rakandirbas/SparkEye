package de.uni.konstanz.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import twitter4j.Status;
import de.uni.konstanz.utils.FileUtils;

public class CSVWriter {

	public static void main(String[] args) {
	}

	private BufferedWriter writer;

	public CSVWriter( File file, boolean append ) throws IOException {
		if ( append ) 
		{
			writer = FileUtils.getFileWriter(file, true);
		}
		else {
			writer = FileUtils.getFileWriter(file);
		}
	}

	public void writeTweet( Status status ) throws IOException {
		writer.write( String.format("%s\t",  status.getUser().getScreenName()) );//10
		writer.write( String.format("%d\t",  status.getCreatedAt().getTime()) );//1
		
		String txt = status.getText();
		txt = txt.replaceAll("(\\r|\\n)", " ");
		writer.write( String.format("%s\t",  txt) );//2
		
		writer.write( String.format("%d\t",  status.getUser().getFriendsCount()) );//3
		writer.write( String.format("%d\t",  status.getUser().getFollowersCount()) );//4
		writer.write( String.format("%d\t",  status.getUser().getStatusesCount()) );//5
		double lat = 0, longt = 0;
		if ( status.getGeoLocation() != null) {
			lat = status.getGeoLocation().getLatitude();
			longt = status.getGeoLocation().getLongitude();
		}
		writer.write( String.format("%f\t", lat ) );
		writer.write( String.format("%f\n", longt) );
	}
	
	public void close() throws IOException {
		writer.close();
	}

}








