package org.sqlparser.parser.SQL99;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.*;
import org.sqlparser.domain.sql.Schema;
import org.sqlparser.domain.sql.Table;

public class SqlSchemaParser {
	
	/**
	 * Parses a SQL schema from a file and returns a Schema object.
	 * It reads the SQL file, removes comments, parses the statements,
	 * and builds the schema with tables and foreign keys.
	 *
	 * @param filePath   Path to the SQL file.
	 * @param schemaName Name for the schema.
	 * @return Schema object representing the parsed schema.
	 * @throws IOException If the file cannot be read.
	 * @throws Exception   If parsing fails.
	 */
	public static Schema parseSchemaFromFile(String filePath, String schemaName) throws IOException, Exception {
		String sqlContent = Files.readString(Paths.get(filePath));
		Schema schema = new Schema(schemaName);
		String cleanSqlContent = cleanComments(sqlContent);
		
		List<Statement> statements = CCJSqlParserUtil.parseStatements(cleanSqlContent);
		
		for (Statement stmt : statements) {
			if (stmt instanceof CreateTable createTable) {
				Table table = TableParser.parseTableWithoutForeignKeys(createTable);
				schema.addTable(table);
			}
		}
		
		for (Statement stmt : statements) {
			if (stmt instanceof CreateTable createTable) {
				Table table = schema.getTables().get(createTable.getTable().getName());
				TableParser.addForeignKeys(table, createTable, schema);
			}
		}
		
		return schema;
	}
	
	/**
	 * Removes SQL comments from the provided SQL content.
	 *
	 * @param sqlContent SQL content as a string.
	 * @return SQL content without comments.
	 */
	private static String cleanComments(String sqlContent) {
		return sqlContent.replaceAll("(?m)^\\s*--.*(?:\\r?\\n)?", "");
	}
}