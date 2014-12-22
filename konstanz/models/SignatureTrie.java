package de.uni.konstanz.models;

import java.util.LinkedList;

/**
 * This class is supposed to be tested and works perfectly.
 * @author rockyrock
 *
 */

public class SignatureTrie extends Trie {

	public static void main(String[] args) {
		SignatureTrie trie = new SignatureTrie();

		trie.insert("1111");
		trie.insert("1100");
		trie.insert("1001");

		trie.printTrie();
		
		String s = trie.getNearestSignature("0010");
		
		System.out.println("Printing nearest sig...");
		
		System.out.println(s);

	}

	public String getNearestSignature( String signature ) {
		LinkedList<Character> signatureList = 
				getSignatureAsList(signature);
		LinkedList<Character> nearestSignatureList = new 
				LinkedList<Character>();
		String nearestSig = "";

		for ( int i = 0; i < signatureList.size(); i++ ) {
			nearestSignatureList.add( signatureList.get(i) );
			String tempSig = getSignatureListAsString(nearestSignatureList);

			//If it doesn't contain the prefix, then flip the last bit
			if ( !hasPrefix(tempSig) ) {
				char bit = nearestSignatureList.pollLast();

				if ( bit == '1' )
					bit = '0';
				else
					bit = '1';

				nearestSignatureList.add(bit);
			}
		}

		nearestSig = getSignatureListAsString(nearestSignatureList);
		return nearestSig;
	}

	public boolean hasPrefix( String string ) {
		if ( string.isEmpty() )
			return false;

		boolean exists = false;

		for ( TrieNode link : root.links ) {
			if ( link.letter == string.charAt(0) ) {
				exists = hasPrefix( string, link );
			}
		}

		return exists;
	}

	private boolean hasPrefix(String string, TrieNode node) {
		boolean exists = false;

		if ( node.letter == string.charAt(0) ) {
			if ( string.length() > 1 ) {
				String subString = string.substring(1, string.length());
				for ( TrieNode link : node.links ) {
					if ( link.letter == string.charAt(1) ) {
						exists = hasPrefix(subString, link);
					}
				}
			}
			else {
				//There's only one node that has no links and its letter equals 
				//the string that consists of only one character.
				exists = true;
			}
		}

		return exists;
	}

	private static LinkedList<Character> getSignatureAsList( String s ) {
		LinkedList<Character> sList = new LinkedList<Character>();

		for ( int i = 0; i < s.length(); i++ ) {
			sList.add( s.charAt(i) );
		}

		return sList;
	}

	public static String getSignatureListAsString( LinkedList<Character> list ) {
		String s = "";

		for ( Character c : list ) {
			s += c;
		}

		return s;
	}

}












