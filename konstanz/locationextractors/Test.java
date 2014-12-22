package de.uni.konstanz.locationextractors;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

public class Test {
	public static void main(String[] args) {
		String serializedClassifier = "classifiers/english.all.3class.caseless.distsim.crf.ser.gz";
		AbstractSequenceClassifier<CoreLabel> classifier =
				CRFClassifier.getClassifierNoExceptions(serializedClassifier);
	}
}
