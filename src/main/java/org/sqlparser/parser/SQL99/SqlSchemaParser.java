package org.sqlparser.parser.SQL99;

import org.sqlparser.domain.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.*;

public class SqlSchemaParser {

    /**
     * Reads a SQL file containing CREATE TABLE statements, removes single-line comments,
     * and parses the content using JSqlParser to produce a {@link Schema} containing the parsed tables.
     * The actual parsing of each table is delegated to {@link TableParser#parseTable(CreateTable)}.
     *
     * @param filePath   the path to the SQL file containing CREATE TABLE statements
     * @param schemaName the name to assign to the resulting {@link Schema}
     * @return a {@link Schema} object populated with tables parsed from the SQL file
     * @throws IOException if an I/O error occurs reading the file
     * @throws Exception   if an error occurs during SQL parsing
     */
    public static Schema parseSchemaFromFile(String filePath, String schemaName) throws IOException, Exception {
        String sqlContent = Files.readString(Paths.get(filePath));
        Schema schema = new Schema(schemaName);
        String cleanSqlContent = cleanComments(sqlContent);

        List<Statement> statements = CCJSqlParserUtil.parseStatements(cleanSqlContent);

        for (Statement stmt : statements) {
            if (stmt instanceof CreateTable createTable) {
                Table table = TableParser.parseTable(createTable);
                schema.addTable(table);
            }
        }
        return schema;
    }

    private static String cleanComments(String sqlContent) {
        return sqlContent.replaceAll("(?m)^\\s*--.*(?:\\r?\\n)?", "");
    }
}