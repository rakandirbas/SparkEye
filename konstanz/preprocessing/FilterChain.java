package de.uni.konstanz.preprocessing;

import java.util.List;

import de.uni.konstanz.models.VoltTweet;

public class FilterChain {
	List<Filter> filters;
	
	public FilterChain( List<Filter> filters ) {
		this.filters = filters;
	}
	
	public boolean pass(VoltTweet tweet) {
		for ( Filter filter : filters ) {
			if ( !filter.pass(tweet) )
				return false;
		}
		
		return true;
	}
}
