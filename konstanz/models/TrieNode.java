package de.uni.konstanz.models;

import java.util.ArrayList;
import java.util.List;

public class TrieNode {
	char letter;
	List<TrieNode> links;
	boolean isWord;
	
	public TrieNode() {
		letter = '\0';
		links = new ArrayList<TrieNode>();
	}
}
