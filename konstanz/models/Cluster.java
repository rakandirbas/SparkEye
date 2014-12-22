package de.uni.konstanz.models;

import java.util.ArrayList;
import java.util.List;

import de.uni.konstanz.utils.IDGenerator;

public class Cluster implements Comparable<Cluster> {
	//Holds the documents/tweets IDs that belong to this cluster.
	protected List<Long> documentsList;
	protected Long id;
	
	public Cluster() {
		documentsList = new ArrayList<Long>();
		id = IDGenerator.getID();
	}
	
	public void addDocumentToCluster( Tweet tweet ) {
		getDocumentsList().add( tweet.getId() );
	}
	
	public int getClusterSize() {
		return getDocumentsList().size();
	}
	
	public List<Long> getDocumentsList() {
		return documentsList;
	}

	public void setDocumentsList(List<Long> documentsList) {
		this.documentsList = documentsList;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public int compareTo(Cluster o) {
		if ( getClusterSize() > o.getClusterSize() )
			return 1;
		else if ( getClusterSize() < o.getClusterSize() )
			return -1;
		else return 0;
	}
	
	@Override
	public String toString() {
		String dump = "############### Printing Cluster Dump ###############\n";
		dump += "Cluster ID: " + getId() + "\n";
		dump += "Cluster size: " + getClusterSize() + "\n";
		
		return dump;
	}
}
