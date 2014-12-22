package de.uni.konstanz.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import de.uni.konstanz.analysis.Preprocessor;
import de.uni.konstanz.models.Token;

public class LookupTable {
	private static Logger logger = Logger.getLogger(Preprocessor.class);
	public final File locationsFile;
	
	private final SetMultimap<String, String> 
	locations_1PartToNameMap = HashMultimap.create();

	private final Map<String, Integer> locations = 
			new HashMap<String, Integer>();

	private final Set<String> _1PartLocs = 
			new HashSet<String>();
	private final Set<String> _2PartsLocs = 
			new HashSet<String>();
	private final Set<String> _3PartsLocs = 
			new HashSet<String>();
	private final Set<String> _4PartsLocs = 
			new HashSet<String>();
	private final Set<String> _5PartsLocs = 
			new HashSet<String>();
	private final Set<String> _6PartsLocs = 
			new HashSet<String>();
	private final Set<String> _7PartsLocs = 
			new HashSet<String>();
	
	public LookupTable(File fileToRead) {
		locationsFile = fileToRead;
		loadLocationsTables();
	}
	
	
	
	public void loadLocationsTables() {
		BufferedReader reader = FileUtils.getFileReader(locationsFile);
		String line = "";
		try {
			while( (line = reader.readLine()) != null ) {
				String name = line;
				name = name.toLowerCase();
				String[] nameParts = name.split(" ");
				locations.put(name, nameParts.length);
				locations_1PartToNameMap.put( nameParts[0], name );

				if ( nameParts.length == 1 ) {
					_1PartLocs.add( nameParts[0] );
				}
				else if ( nameParts.length == 2 ) {
					_1PartLocs.add( nameParts[0] );
					_2PartsLocs.add(nameParts[1]);
				}
				else if ( nameParts.length == 3 ) {
					_1PartLocs.add( nameParts[0] );
					_2PartsLocs.add(nameParts[1]);
					_3PartsLocs.add(nameParts[2]);
				}
				else if ( nameParts.length == 4 ) {
					_1PartLocs.add( nameParts[0] );
					_2PartsLocs.add( nameParts[1] );
					_3PartsLocs.add( nameParts[2] );
					_4PartsLocs.add( nameParts[3] );
				}
				else if ( nameParts.length == 5 ) {
					_1PartLocs.add( nameParts[0] );
					_2PartsLocs.add( nameParts[1] );
					_3PartsLocs.add( nameParts[2] );
					_4PartsLocs.add( nameParts[3] );
					_5PartsLocs.add( nameParts[4] );
				}
				else if ( nameParts.length == 6 ) {
					_1PartLocs.add( nameParts[0] );
					_2PartsLocs.add( nameParts[1] );
					_3PartsLocs.add( nameParts[2] );
					_4PartsLocs.add( nameParts[3] );
					_5PartsLocs.add( nameParts[4] );
					_6PartsLocs.add( nameParts[5] );
				}
				else if ( nameParts.length == 7 ) {
					_1PartLocs.add( nameParts[0] );
					_2PartsLocs.add( nameParts[1] );
					_3PartsLocs.add( nameParts[2] );
					_4PartsLocs.add( nameParts[3] );
					_5PartsLocs.add( nameParts[4] );
					_6PartsLocs.add( nameParts[5] );
					_7PartsLocs.add( nameParts[6] );
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<String> getTermsFromText( String rawText ) {
		List<String> tokens = new LinkedList<String>();
		List<Token> tokensList = getTokensList(rawText);

		for ( Token token : tokensList ) {
			tokens.add(token.getToken());
		}

		return getTermsFromText(tokens);
	}
	
	public List<String> getTermsFromText( List<String> tokens ) {
		String location = "";
		List<String> detectedLocations = new LinkedList<String>();
		String detectedLocation = "";
		List<String> allLocationsFound = new LinkedList<String>();

		for ( int i = 0; i < tokens.size(); i++ ) {
			String token = tokens.get(i);
			int remaningTokens = tokens.size() - (i+1);
			Set<String> possibleLocations = locations_1PartToNameMap.get(token);

			if ( !possibleLocations.isEmpty() ) {
				int maxParts = 0;
				int minParts = 8;

				for ( String possibleLoc : possibleLocations ) {
					int partsCount = locations.get(possibleLoc);
					if ( partsCount > maxParts )
						maxParts = partsCount;
					if ( partsCount < minParts )
						minParts = partsCount;
				}

				if ( maxParts == 1 ) {
					detectedLocation = token;
				}
				else {
					for ( String possibleLoc : possibleLocations ) {
						//System.err.println("FF: " + possibleLoc);
						location += (token + " ");
						int partsCount = locations.get(possibleLoc);
						/* The following if condition will check if the
						 * tweet contains enough tokens to check for the parts
						 * of the possible location.
						 * We add 1 cuz the place name =
						 * the remaining tokens + the current token
						 * */
						if ( (remaningTokens+1) >= partsCount ) {
							/* Now we start checking the tokens in the tweet
							 * that follows the first part token.
							 */
							int partsCounter = 2;//Start checking from the second part
							boolean allFollowingLocPartsExist = true;
							/*
							 * The loop ends either when it checks all following tokens in the
							 * tweet that has the same number as the possible location parts OR
							 * when a following token is not found in a locationPartTable.
							 */
							/*
							 * بشرط الفور لوب انا عم افحص فقط التوكينات يلي بعد التوكين الاولى بنفس عدد التوكينات التي تشكل البوسيبيل لوكيشن
							 */
							for ( int j = i+1; (j < (i+partsCount)) & allFollowingLocPartsExist; j++ ) {
								String nextToken = tokens.get(j);
								if ( partsCounter == 2 ) {
									if ( _2PartsLocs.contains(nextToken) ) {
										location += (nextToken + " ");
									}
									else {
										allFollowingLocPartsExist = false;
									}
								}
								else if ( partsCounter == 3 ) {
									if ( _3PartsLocs.contains(nextToken) ) {
										location += (nextToken + " ");
									}
									else {
										allFollowingLocPartsExist = false;
									}
								}
								else if ( partsCounter == 4 ) {
									if ( _4PartsLocs.contains(nextToken) ) {
										location += (nextToken + " ");
									}
									else {
										allFollowingLocPartsExist = false;
									}
								}
								else if ( partsCounter == 5 ) {
									if ( _5PartsLocs.contains(nextToken) ) {
										location += (nextToken + " ");
									}
									else {
										allFollowingLocPartsExist = false;
									}
								}
								else if ( partsCounter == 6 ) {
									if ( _6PartsLocs.contains(nextToken) ) {
										location += (nextToken + " ");
									}
									else {
										allFollowingLocPartsExist = false;
									}
								}
								else if ( partsCounter == 7 ) {
									if ( _7PartsLocs.contains(nextToken) ) {
										location += (nextToken + " ");
									}
									else {
										allFollowingLocPartsExist = false;
									}
								}
								partsCounter++;
							}
							location = location.trim();
							if ( !allFollowingLocPartsExist ) {
								location = "";
							}
							else if ( possibleLoc.equals(location) ) {
								/* put only locations that match textually the 
								 * possible location because locations could match
								 * because of a chance that all parts were in the
								 * tables, but they don't necessary compose a location.
								 * e.g. (love new cover). New could be of new york,
								 * which means two parts. Cover could be from Van Cover
								 * which is also two parts, so Cover is already in the 2nd table
								 * but the possible location is New York and this way I get New Cover
								 * as a detected location! Thus I checked before that the detected location
								 * mathes that of the possible location.
								 */
								detectedLocations.add(location);
								location = "";
							}
							else {
								/*
								 * I'm not sure why it would reach this level,
								 * but I put it anyway!
								 */
								//System.err.println("I reached this point!");
								location = "";
							}
						}

					}//end of the possible locations loop
					/*
					 * This is to get the detected location with the largest parts.
					 */
					int max = 0;
					for ( String s : detectedLocations ) {
						if ( s.length() > max ) {
							//System.out.println(s);
							max = s.length();
							detectedLocation = s;
						}
					}
					/* Shift i so it doesn't detect sub locations from large locations
					 * e.g. Love San Jose Van a lot.
					 * [San Jose Van] is one detected location. 
					 * then if I start by checking Jose, there might be a location
					 * whose name is Jose itself! Or even Jose Van. That's why 
					 * I shift the tokens after the detected locations. 
					 */

					int shift_i_by = detectedLocation.split(" ").length;
					i += shift_i_by;
					i--;//because it will be increased by 1 in the loop increment part now.
				}//end of the else
			}//end of if condition that there are possible locations
			if ( !detectedLocation.isEmpty() )
				allLocationsFound.add(detectedLocation);
			detectedLocation = "";
			detectedLocations.clear();
		}//end of the tokens iteration loop

		return allLocationsFound;
	}
	
	/**
	 * This method removes all URLs, mentions, stopwrods and
	 * non-letter characters from tweet text. Hashtags are stripped down from their hash char.
	 * @param text
	 * @return
	 */
	public static List<Token> getTokensList( String text ) {
		List<Token> tokensList = new LinkedList<Token>();

		List<String> regexList = new ArrayList<String>();
		regexList.add(Preprocessor.urlRegex);
		regexList.add(Preprocessor.mentionRegex);

		List<Token> tempTokensList = Preprocessor.getTokensListWithoutPOS(text, regexList);
		try {
			String keyword = "";
			for ( Token token : tempTokensList ) {
				keyword = token.getToken();

				keyword = AnalyzerUtils.filterKeyword(Preprocessor.stopWordsAnalyzer, keyword);

				if ( !keyword.isEmpty() ) {
					tokensList.add(token);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Error while checking if the token is a stopword",e);
		}

		return tokensList;
	}
}
