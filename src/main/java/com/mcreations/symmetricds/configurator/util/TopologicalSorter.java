package com.mcreations.symmetricds.configurator.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * 
 * @author Reza Rahimi <rahimi@m-creations.com>
 *
 */
public class TopologicalSorter<T> {
	private static final Logger LOG = LoggerFactory.getLogger(TopologicalSorter.class);

	public List<T> sort(MultiValuedMap<T, T> graphMultiMap) {
		List<T> result = new ArrayList<T>();
		List<T> toSort = new ArrayList<T>(graphMultiMap.keySet());
		try {
			int i = 0;
			outer: while(!toSort.isEmpty()) {
				for(T r : toSort) {
					if(!hasDependency(graphMultiMap, r, toSort)) {
						toSort.remove(r);
						result.add(r);
						continue outer;
					}
				}
				throw new Exception("Graph has cycles");
			}
		} catch(Exception e) {
			System.out.println(e);
			return Lists.newArrayList();
		}
		return Lists.reverse(result);
	}

	private boolean hasDependency(MultiValuedMap<T, T> graphMultiMap, T r, List<T> toSort) {
		Collection<T> list = graphMultiMap.get(r);
		for(T c : toSort) {
			if(list.contains(c))
				return true;
		}
		return false;
	}
}
