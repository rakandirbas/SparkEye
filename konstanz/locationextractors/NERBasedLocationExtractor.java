package de.uni.konstanz.locationextractors;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.uni.konstanz.models.Tweet;
import de.uni.konstanz.utils.UnicodeConverter;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

public class NERBasedLocationExtractor implements LocationExtractor {
	
	public static String serializedClassifier = "classifiers/english.all.3class.caseless.distsim.crf.ser.gz";
	public static AbstractSequenceClassifier<CoreLabel> classifier =
			CRFClassifier.getClassifierNoExceptions(serializedClassifier);
	
	public static List<String> extractLocations(String text) {
		List<String> locations = new LinkedList<String>();
		text = UnicodeConverter.convert(text);
		for ( List<CoreLabel> lcl : classifier.classify(text) ) {
			
			for (CoreLabel cl : lcl) {
				String eTag = cl.get(CoreAnnotations.AnswerAnnotation.class);
				if ( eTag.equals("LOCATION") ) {
					String location = cl.toString();
					location = location.toLowerCase();
					locations.add(location);
				}
	          }
		}
		
		return locations;
	}
 
	@Override
	public List<String> getLocations(Tweet tweet) {
		return getLocations( tweet.getText() );
	}

	@Override
	public List<String> getLocations(String text) {
		return extractLocations(text);
	}

	@Override
	public Set<String> getUniqueLocations(Tweet tweet) {
		return getUniqueLocations(tweet.getText());
	}

	@Override
	public Set<String> getUniqueLocations(String text) {
		Set<String> uniqueLocations = new LinkedHashSet<String>();
		List<String> locations = getLocations(text);
		
		for ( String loc : locations ) {
			uniqueLocations.add(loc);
		}
		
		return uniqueLocations;
	}
	
	

}
