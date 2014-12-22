package de.uni.konstanz.preprocessing;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import de.uni.konstanz.models.VoltTweet;

/**
 * A boolean keyword filter that accepts boolean queries:
 * E.g.: +rakan +dirbas -bad good ok
 * The tweet then MUST have 'rakan' AND 'dirbas'
 * MUST NOT have 'bad' 
 * Should have at least 'good' OR 'ok'
 * 
 * @author rockyrock
 *
 */
public class KeywordFilter implements Filter {
	
	private Set<String> filteringKeywords;
	Set<String> mustHaveWords = new LinkedHashSet<String>();
	Set<String> shouldHaveWords = new LinkedHashSet<String>();
	Set<String> NOTHaveWords = new LinkedHashSet<String>();
	
	public static void main(String[] args) {
		Set<String> set = new HashSet<String>();
//		set.add("+rakan");
//		set.add("ok");
//		set.add("b");
//		set.add("+dirbas");
//		set.add("-man");
		Filter filter = new KeywordFilter("+rakan");
		VoltTweet tweet = new VoltTweet();
		tweet.setTokens(new String[] {"rakan", "dirbas", "good", "bad"});
		System.out.println(filter.pass(tweet));
	}
	
	/**
	 * The passed quereies should be of the following type:
	 * (queryFlag)word. So +word, -word, word.
	 * [+] is MUST have.
	 * [-] MUST NOT have.
	 * []  should "at least" have.
	 * @param filteringKeywords
	 */
	public KeywordFilter(Set<String> filteringKeywords) {
		this.filteringKeywords = filteringKeywords;
		mustHaveWords = new LinkedHashSet<String>();
		shouldHaveWords = new LinkedHashSet<String>();
		NOTHaveWords = new LinkedHashSet<String>();
		parseKeywordQuery();
	}
	
	public KeywordFilter(String keyword) {
		this( makeFilterKeywords(keyword) );
	}

	@Override
	public boolean pass(VoltTweet tweet) {
		Set<String> tokens = new LinkedHashSet<>();
		for ( String token : tweet.getTokens() ) {
			tokens.add(token);
		}
		
		for ( String word : mustHaveWords ) {
			if ( !tokens.contains(word) )
				return false;
		}
		
		for( String word : NOTHaveWords ) {
			if ( tokens.contains(word) )
				return false;
		}
		
		boolean atLeast = false;
		for ( String word : shouldHaveWords ) {
			if ( tokens.contains(word) )
				atLeast = true;
		}
		
		if ( !atLeast && !shouldHaveWords.isEmpty() )
			return false;
		
		return true;
	}
	
	private void parseKeywordQuery() {
		for ( String query : filteringKeywords ) {
			char flag = query.charAt(0);
			if ( flag == '+' ) {
				mustHaveWords.add(query.substring(1));
			}
			else if ( flag == '-' ) {
				NOTHaveWords.add(query.substring(1));
			}
			else {
				shouldHaveWords.add(query);
			}
		}
	}
	
	public static Set<String> makeFilterKeywords(String keyword) {
		Set<String> set = new LinkedHashSet<String>();
		set.add(keyword);
		return set;
	}

}















