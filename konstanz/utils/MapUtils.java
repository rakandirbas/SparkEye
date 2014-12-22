package de.uni.konstanz.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MapUtils {

	public static void main(String[] args) {
		HashMap<String,Integer> map = new LinkedHashMap<String,Integer>();
		MapUtils sorter = new MapUtils();
		map.put("A",99);
		map.put("B",6);
		map.put("C",53);
		map.put("D",62);
		System.out.println("unsorted map: "+map);

		System.out.println("results: " + sorter.sortByValue(map, true) );
	}


	public static Map<String, Integer> sort( Map<String,Integer> map ) {
		ValueComparator bvc =  new ValueComparator(map);
		TreeMap<String,Integer> sortedMap = new TreeMap<String,Integer>(bvc);
		sortedMap.putAll(map);
		return sortedMap;
	}
	
	/**
	 * Sort a key-value map. The boolean parameter decides if ascending or decending.
	 * @param map
	 * @param descending
	 * @return
	 */
	public static Map sortByValue(Map map, boolean descending) {
	     List list = new LinkedList(map.entrySet());
	     Comparator comparator = null;
	     
	     if ( descending ) {
	    	 comparator = new Comparator() {
		          public int compare(Object o1, Object o2) {
		               return ((Comparable) ((Map.Entry) (o2)).getValue())
		              .compareTo(((Map.Entry) (o1)).getValue());
		          }
		     };

	     }
	     else {
	    	 comparator = new Comparator() {
		          public int compare(Object o1, Object o2) {
		               return ((Comparable) ((Map.Entry) (o1)).getValue())
		              .compareTo(((Map.Entry) (o2)).getValue());
		          }
		     };
	     }
	     
	     Collections.sort(list, comparator);

	    Map result = new LinkedHashMap();
	    for (Iterator it = list.iterator(); it.hasNext();) {
	        Map.Entry entry = (Map.Entry)it.next();
	        result.put(entry.getKey(), entry.getValue());
	    }
	    return result;
	} 

}


class ValueComparator implements Comparator<String> {

	Map<String, Integer> base;
	public ValueComparator(Map<String, Integer> base) {
		this.base = base;
	}

	// Note: this comparator imposes orderings that are inconsistent with equals.    
	public int compare(String a, String b) {
		if (base.get(a) >= base.get(b)) {
			return -1;
		} else {
			return 1;
		} // returning 0 would merge keys
	}
}