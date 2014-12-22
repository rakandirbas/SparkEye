package de.uni.konstanz.tests;

import uk.ac.wlv.sentistrength.SentiStrength;



public class SentistrengthTest {
	
	public static void main(String[] args) {
		String inis[] = new String[]{"sentidata",
				"resources/sentistrength/SentStrength_Data_Sept2011/",
				"trinary"};
		SentiStrength sentiStrength = new SentiStrength();
		sentiStrength.initialise(inis);
		
		String result = sentiStrength.computeSentimentScores("BAD magazine ever. RT @NewYorker: Early look at next week's cover, \"Shadow Over Boston,\" by Eric Drooker: ");
		
		System.out.println( result );
		
		int classRes = Integer.parseInt( result.split(" ")[2] );
				
		System.out.println("Classi result: " + classRes);
		
		
		//SentiStrength sentiStrengths = new SentiStrength(new String[]{"help"});
		
	}

}
