package de.uni.konstanz.models;

public class Trie {
	public TrieNode root;

	public static void main(String[] args) {
		Trie t = new Trie();
		t.insert("Rakan");
		t.insert("Ralom");
		t.insert("Rakans");
		t.insert("Dania");
		t.insert("Daria");
		t.insert("Jack");
		t.insert("Jacklin");
		t.insert("Rakol");
		t.insert("Derek");

		if ( t.search("Rakanslom") ) {
			System.out.println( "Inside the tree" );
		}
		else {
			System.out.println( "NOT inside the tree" );
		}
		
		t.printTrie();

	}

	public Trie() {
		root = new TrieNode();
	}

	private void printTrie( TrieNode node ) {
		System.out.println( node.letter );
		if ( node.isWord )
			System.out.println( "_" );
		for ( TrieNode link : node.links ) {
			printTrie(link);
		}
	}

	public void printTrie() {
		printTrie(root);
	}

	public boolean search( String string ) {
		if ( string.isEmpty() )
			return false;

		boolean exists = false;

		for ( TrieNode link : root.links ) {
			if ( link.letter == string.charAt(0) ) {
				exists = search( string, link );
			}
		}

		return exists;
	}

	private boolean search(String string, TrieNode node) {
		boolean exists = false;

		if ( node.letter == string.charAt(0) ) {
			if ( string.length() > 1 ) {
				String subString = string.substring(1, string.length());
				for ( TrieNode link : node.links ) {
					if ( link.letter == string.charAt(1) ) {
						exists = search(subString, link);
					}
				}
			}
			else {
				//There's only one node that has no links and its letter equals 
				//the string that consists of only one character.
				if ( node.isWord ) {
					//Return true only if it's a word not just prefix
					exists = true;
				}
			}
		}

		return exists;
	}

	public void insert(String string) {

		if ( string.length() > 0 ) {
			boolean linkIsPartOfString = false;
			for ( TrieNode link : root.links ) {
				if ( link.letter == string.charAt(0) ) {
					linkIsPartOfString = true;
					insert(string, link);
				}
			}

			if ( !linkIsPartOfString ) {
				createSplit(string, root);
			}

		}

	}

	private void insert(String string, TrieNode node) {
		if ( node.letter == string.charAt(0) ) {

			if ( string.length() > 1 ) {
				String subString = string.substring(1, string.length());
				boolean linkIsPartOfString = false;
				for ( TrieNode link : node.links ) {
					if ( link.letter == subString.charAt(0) ) {
						linkIsPartOfString = true;
						insert(subString, link);
					}
				}
				
				if ( !linkIsPartOfString ) {
					/*Here it will/should create a split because it assumes
					 * that in the next iteration the node.letter and 
					 * subStrin[0] will be different so it will create the
					 * split in the condition below. However when subString[0]
					 * is a repeated character (i.e. the same as string[0]),
					 * then the first condition on top will prevent the creation
					 * of the split.
					 */
					//insert(subString,node);
					createSplit(subString, node);
				}
			}

		}
		else {
			//If it doesn't equal, then create a split
			createSplit(string, node);

		}

	}
	
	private void createSplit( String string, TrieNode node ) {
		if ( string.length() == 1 ) {
			TrieNode link = new TrieNode();
			link.letter = string.charAt(0);
			link.isWord = true;
			node.links.add(link);
		}
		else {
			TrieNode link = new TrieNode();
			link.letter = string.charAt(0);
			node.links.add(link);
			//Continue inserting at the new branch
			String subString = string.substring(1, string.length());
			createSplit(subString, link);
		}
	}
	
}
