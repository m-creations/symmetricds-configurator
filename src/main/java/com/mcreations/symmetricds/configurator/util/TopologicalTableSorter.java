package com.mcreations.symmetricds.configurator.util;

import static java.text.MessageFormat.format;

import java.util.ArrayList;

public class TopologicalTableSorter {

	public void sort(Table table, ArrayList<Table> sorted) {
		if(table.isVisiting())
			throw new RuntimeException(
			      format("Visiting {0} for the second time! This graph is NOT acyclic!", table.getName()));
		if(table.isVisited())
			return;
		table.setVisiting(true);
		for(Table dep : table.getDependencies()) {
			sort(dep, sorted);
		}
		table.setVisited(true);
		table.setVisiting(false);
		sorted.add(table);
	}

}
