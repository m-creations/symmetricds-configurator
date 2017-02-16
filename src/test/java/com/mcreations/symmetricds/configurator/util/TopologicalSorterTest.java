package com.mcreations.symmetricds.configurator.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author Reza Rahimi <rahimi@m-creations.com>
 *
 */
public class TopologicalSorterTest {
	protected static TopologicalSorter<String> tsort = new TopologicalSorter<String>();
	protected static MultiValuedMap<String, String> graphMutimap = new ArrayListValuedHashMap<String, String>();

	@BeforeClass
	public static void init() {
		// Example data from here: https://en.wikipedia.org/w/index.php?title=Topological_sorting&oldid=753542990#Examples
		// 11->[null, 2, 10, 9]
		// 2->[null]
		// 3->[null, 8, 10]
		// 5->[null, 11]
		// 7->[null, 11, 8]
		// 8->[null, 9]
		// 9->[null]
		// 10->[null]
		// Sorted node count = 8
		// 7 5 3 11 10 8 9 2
		graphMutimap.put("2", null);
		graphMutimap.put("3", null);
		graphMutimap.put("5", null);
		graphMutimap.put("7", null);
		graphMutimap.put("8", null);
		graphMutimap.put("9", null);
		graphMutimap.put("10", null);
		graphMutimap.put("11", null);
		graphMutimap.put("7", "11");
		graphMutimap.put("5", "11");
		graphMutimap.put("11", "2");
		graphMutimap.put("11", "10");
		graphMutimap.put("11", "9");
		graphMutimap.put("7", "8");
		graphMutimap.put("8", "9");
		graphMutimap.put("3", "8");
		graphMutimap.put("3", "10");
	}

	@Test
	public void testSort() {
		for(Iterator<String> iterator = graphMutimap.keySet().iterator(); iterator.hasNext();) {
			String item = (String) iterator.next();
			System.out.println(item + "->" + graphMutimap.get(item));
		}
		List<String> topoSorted = tsort.sort(graphMutimap);
		System.out.println("Sorted node count = " + topoSorted.size());
		for(Iterator<String> iterator = topoSorted.iterator(); iterator.hasNext();) {
			String item = (String) iterator.next();
			System.out.print(item + "  ");
		}
		System.out.println();
		List<String> untilNow = new ArrayList<String>();
		int errorCount = 0;
		for(Iterator iterator = topoSorted.iterator(); iterator.hasNext();) {
			String item = (String) iterator.next();
			if(untilNow.contains(item)) {
				System.out.println("Error ===> " + item);
				errorCount++;
			} else {
				untilNow.add(item);
			}
		}
		if(errorCount == 0) {
			System.out.println("✔");
		}
		Assert.assertEquals("✘ Sorted nodes of graph do not meet the dependencies", 0, errorCount);
	}
}
