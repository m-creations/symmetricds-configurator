package com.mcreations.symmetricds.configurator;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mcreations.renderer.velocity.VelocityRenderer;
import com.mcreations.symmetricds.configurator.util.TopologicalSorter;

/**
 * 
 * @author Reza Rahimi <rahimi@m-creations.com>
 *
 */
public class SymmetricConfigurator {
	private static final Logger LOG = LoggerFactory.getLogger(SymmetricConfigurator.class);
	private static final String APPLICATION_NAME = "SymmetricConfigurator";
	private static final String[][] OPTIONS = new String[][] {
	      VelocityRenderer.OPTIONS[0],
	      VelocityRenderer.OPTIONS[1],
	      VelocityRenderer.OPTIONS[2],
	      { "u", "database", "database-url", "Jdbc url to connect to database. Supported databases are MySQL and PostgreSQL" },
	      { "n", "user", "database-user", "The databases user." },
	      { "p", "password", "database-password", "The password of database user" }
	};

	static MultiValuedMap<String, String> tables = new ArrayListValuedHashMap<String, String>();
	static TopologicalSorter<String> topologicalSorter = new TopologicalSorter<String>();

	public static void main(String[] args) {
		SymmetricConfigurator symmetricConfigurator = new SymmetricConfigurator();
		VelocityRenderer renderer = new VelocityRenderer();
		String sourcePath = null;
		String destPath = null;
		String filePattern = null;
		String jdbcUrl = null;
		String jdbcUser = null;
		String jdbcPass = null;
		/*
		 * create the command line parser
		 */
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		// create the Options
		Options options = new Options();
		for(String[] option : OPTIONS) {
			options.addOption(Option.builder(option[0]).argName(option[1]).type(String.class).longOpt(option[2]).hasArg(true).desc(option[3]).build());
		}
		/*
		 * Parse the command line options
		 */

		try {
			CommandLine line = parser.parse(options, args);
			/*
			 * Validate command lines options
			 */
			for(String[] option : OPTIONS) {
				if(!line.hasOption(option[2])) {
					formatter.printHelp(APPLICATION_NAME, options);
					System.exit(-1);
				}
			}

			sourcePath = line.getOptionValue("s");
			destPath = line.getOptionValue("d");
			filePattern = line.getOptionValue("f");
			jdbcUrl = line.getOptionValue("u");
			jdbcUser = line.getOptionValue("n");
			jdbcPass = line.getOptionValue("p");
		} catch(ParseException e) {
			formatter.printHelp(APPLICATION_NAME, options);
		}

		/*
		 * Logging application arguments
		 */
		LOG.info("sourcePath=" + sourcePath);
		LOG.info("destPath=" + destPath);
		LOG.info("filePattern=" + filePattern);
		LOG.info("jdbcUrl=" + jdbcUrl);
		LOG.info("jdbcUser=" + jdbcUser);
		LOG.info("jdbcPass=" + StringUtils.repeat("*", jdbcPass.length()));

		/*
		 * Rendering configs
		 */
		try {
			Map<String, Object> additionalContextParameters;
			additionalContextParameters = symmetricConfigurator.extractTopologialSortedTables(jdbcUrl, jdbcUser, jdbcPass);
			renderer.render(sourcePath, destPath, filePattern, additionalContextParameters);
		} catch(Exception e) {
			LOG.error(e.getMessage(), e);
			System.exit(-2);
		}
	}

	public Map<String, Object> extractTopologialSortedTables(String jdbcUrl, String jdbcUser, String jdbcPass) throws Exception {
		try {
			String jdbcDriver = null;

			if(StringUtils.containsIgnoreCase(jdbcUrl, "jdbc:mysql"))
				jdbcDriver = "com.mysql.jdbc.Driver";
			else if(StringUtils.containsIgnoreCase(jdbcUrl, "jdbc:mysql"))
				jdbcDriver = "org.postgresql.Driver";
			else {
				LOG.error("Unsupported jdbc driver for this connection url" + jdbcUrl);
				System.exit(-2);
			}

			Class.forName(jdbcDriver).newInstance();

			Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPass);

			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getTables(null, null, "%", null);
			while(rs.next()) {
				String tableName = rs.getString(3);
				tables.put(tableName, null);
				ResultSet rsEk = md.getExportedKeys(null, null, tableName);
				while(rsEk.next()) {
					String childTableName = rsEk.getString(7);
					if(!tableName.equalsIgnoreCase(childTableName))
						tables.put(tableName, childTableName);
				}
			}

		} catch(SQLException ex) {
			LOG.error("SQLException: ", ex.getMessage());
			LOG.error("SQLState: ", ex.getSQLState());
			LOG.error("VendorError: ", ex.getErrorCode());
			LOG.error("SQLException Stacktrace:", ex);
			throw ex;
		} catch(Exception ex) {
			LOG.error(ex.getMessage(), ex);
			throw ex;
		}
		/*
		 * Topological sort
		 */
		List<String> topologicalSortedTables = topologicalSorter.sort(tables);
		List<String> quotedTables = topologicalSortedTables.stream().map(p -> "'".concat(p).concat("'")).collect(Collectors.toList());
		/*
		 * Prepare velocity context
		 */
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tables", topologicalSortedTables);
		params.put("quotedTableNames", quotedTables);
		params.put("tableNamesCVS", String.join(",", topologicalSortedTables));
		params.put("quotedTableNamesCVS", String.join(",", quotedTables));
		return params;
	}

}
