package org.sqlparser.parser.SQL99;

import net.sf.jsqlparser.statement.create.table.Index;
import org.sqlparser.domain.Table;
import org.sqlparser.domain.constraints.ForeignKey;
import org.sqlparser.domain.constraints.PrimaryKey;
import org.sqlparser.domain.constraints.Unique;

import java.util.Collections;
import java.util.List;

/**
 * Provides utility methods to parse table-level constraints from JSqlParser {@link Index} objects
 * and convert them into the domain model's {@link Table} constraints.
 * <p>
 * This class handles the conversion of PRIMARY KEY, FOREIGN KEY (currently with unknown references),
 * and UNIQUE constraints.
 */
public class ConstraintParser {

    public static void parseConstraints(List<Index> indexes, Table table) {
        for (Index index : indexes) {
            String type = index.getType();
            if ("PRIMARY KEY".equalsIgnoreCase(type)) {
                table.setPrimaryKey(new PrimaryKey(index.getColumnsNames()));
            } else if ("FOREIGN KEY".equalsIgnoreCase(type)) {
                table.addForeignKey(new ForeignKey(
                      index.getColumnsNames(), "unknown", Collections.emptyList()
                ));
            } else if ("UNIQUE".equalsIgnoreCase(type)) {
                table.addUniqueConstraint(new Unique(index.getColumnsNames()));
            }
        }
    }
}
