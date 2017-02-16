package com.mcreations.symmetricds.configurator.util;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author Reza Rahimi <rahimi@m-creations.com>
 *
 */
public class TopologicalTableSorterTest {
	protected static TopologicalTableSorter tsort = new TopologicalTableSorter();
	protected static Table tables;

	@BeforeClass
	public static void init() {
		Table c1 = new Table("c1", new Table[0]);
		Table b1 = new Table("b1", new Table[] { c1 });
		Table b2 = new Table("b2", Table.EMPTY_DEPENDENCIES);
		Table a1 = new Table("a1", new Table[] { b1, b2 });
		Table a2 = new Table("a2", new Table[] { b1 });
		tables = new Table("a", new Table[] { a1, a2, b1 });
	}

	@Test
	public void testSort() {
		ArrayList<Table> sorted = new ArrayList<Table>();

		tsort.sort(tables, sorted);
		String sortedTablesCVS = String.join(", ", sorted.stream().map(tb -> tb.getName()).collect(Collectors.toList()));
		System.out.println(sortedTablesCVS);
		Assert.assertEquals("c1, b1, b2, a1, a2, a", sortedTablesCVS);
	}
}
