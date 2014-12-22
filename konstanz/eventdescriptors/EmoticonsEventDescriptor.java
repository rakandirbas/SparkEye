package de.uni.konstanz.eventdescriptors;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import de.uni.konstanz.models.Tweet;

public class EmoticonsEventDescriptor extends EventDescriptor {
	
	public static Set<String> emoticons = 
			new LinkedHashSet<String>();
	
	static {
		String emos = ":-) :) :o) :] :3 :c) :> =] 8) =) :} :^) :っ) :-D :D 8-D" +
				" 8D x-D xD X-D XD =-D =D =-3 =3 B^D :-)) >:[ :-( :( :-c :c :-<" +
				" :っC :< :-[ :[ :{ :-|| :@ >:( :'-( :'( :'-) :') QQ D:< D: D8 D;" +
				" D= DX v.v D-': >:O :-O :O °o° °O° :O o_O o_0 o.O 8-0 :* :^* ;-)" +
				" ;) *-) *) ;-] ;] ;D ;^) :-, >:P :-P :P X-P x-p xp XP :-p :p =p" +
				" :-Þ :Þ :-b :b >:\\ >:/ :-/ :-. :/ :\\ =/ =\\ :L =L :S >.< :|" +
				" :-| :$ :-X :X :-# :# O:-) 0:-3 0:3 0:-) 0:) 0;^) >:) >;) >:-)" +
				" }:-) }:) 3:-) 3:) o/\\o ^5 >_>^ ^<_< |;-) |-O :-& :& #-) %-)" +
				" %) :-###.. :###.. <:-| ಠ_ಠ @>-->-- 5:-) ~:-\\ //0-0\\\\" +
				" *<|:-) =:o] ,:-) 7:^] <3 </3";

		for ( String emo : emos.split(" ") ) {
			emoticons.add(emo);
		}

	}

	public EmoticonsEventDescriptor(List<Tweet> tweets,
			double classificationThreshold) {
		super(tweets, classificationThreshold);
	}

	public double computeScore(List<Tweet> tweets) {
		double score = 0;
		int counter = 0;
		
		for ( Tweet tweet : tweets ) {
			List<String> tokens = tweet.getSimpleTokens();
			for ( String token : tokens ) {
				if ( emoticons.contains(token) ) {
					counter++;
					//System.out.println(token + "|||" + tweet.getText());
					break;
				}
			}
		}
		
		score = (double) counter / tweets.size();
		
		return score;
	}
	public String toString() {
		return "Emoticons";
	}
}
