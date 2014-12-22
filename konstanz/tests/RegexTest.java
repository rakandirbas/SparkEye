package de.uni.konstanz.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
	public static void main(String[] args) {
		String input = "http://t.co/FQEcrfSZFU RT @KennanShweini: @SmiirMaserati http://t.co/FQEcrfSZFU mdddddr ta https://t.co/FQEcrfSZFU mere on change pas http://t.co/FQEcrfSZFU les bonne habitudes @rocky.http://t.co/FQEcrfSZFU";
		String regex = "http+://[\\S]+|https+://[\\S]+";
		Pattern hashtagPattern = Pattern.compile(regex);
		Matcher m = hashtagPattern.matcher(input);
		List<String> matches = new ArrayList<String>();
		while(m.find()){
		    matches.add( (m.group().replace(" ", "")).replace("#", "") );
		}
		
		for ( String s : matches ) {
			System.out.println(s);
		}
		
		System.out.println( "END." );
		
		String[] list = matches.toArray( new String[ matches.size() ] );
		for( String s : list ) {
			System.out.println(s);
		}
	}
}
