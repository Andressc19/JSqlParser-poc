package org.sqlparser.parser.SQL99;

import net.sf.jsqlparser.statement.create.table.CreateTable;
import org.sqlparser.domain.Table;

/**
 * Provides functionality to parse a JSqlParser {@link CreateTable} statement into a domain {@link Table} object.
 * <p>
 * This class delegates column parsing to {@link ColumnParser} and constraint parsing to {@link ConstraintParser}.
 */
public class TableParser {

    public static Table parseTable(CreateTable createTable) {
        String tableName = createTable.getTable().getName();
        Table table = new Table(tableName);

        if (createTable.getColumnDefinitions() != null) {
            createTable.getColumnDefinitions()
                  .forEach(cd -> table.addColumn(ColumnParser.parse(cd)));
        }

        if (createTable.getIndexes() != null) {
            ConstraintParser.parseConstraints(createTable.getIndexes(), table);
        }

        return table;
    }
}