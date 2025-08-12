package org.sqlparser.parser.SQL99;

import net.sf.jsqlparser.statement.create.table.CreateTable;
import org.sqlparser.domain.Column;
import org.sqlparser.domain.Table;
import org.sqlparser.domain.constraints.PrimaryKey;

/**
 * Provides functionality to parse a JSqlParser {@link CreateTable} statement into a domain {@link Table} object.
 */
public class TableParser {

    public static Table parseTable(CreateTable createTable) {
        String tableName = createTable.getTable().getName();
        PrimaryKey pk = new PrimaryKey();
        Table table = new Table(tableName);
        

        if (createTable.getColumnDefinitions() != null) {
            createTable.getColumnDefinitions()
                  .forEach(cd ->
                      {
                          Column cd_parsed = ColumnParser.parse(cd);
                          if (cd_parsed.isPrimaryKey()) pk.addColumn(cd_parsed);
                          table.addColumn(ColumnParser.parse(cd));
                      });
        }
        table.setPrimaryKey(pk);
        
        System.out.println(table.getAsciiTable());
        
        return table;
    }
}