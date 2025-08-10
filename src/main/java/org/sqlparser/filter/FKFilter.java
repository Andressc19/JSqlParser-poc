package org.sqlparser.filter;

import org.sqlparser.domain.constraints.ForeignKey;
import org.sqlparser.domain.Table;

import java.util.*;

public class FKFilter {

    /**
     * Filters foreign keys from the selected tables. This method removes any foreign key constraints
     * from the tables in {@code selectedTables} that reference tables not present in the selected set.
     * It also removes the associated foreign key columns from those tables.
     *
     * @param selectedTables the set of tables to filter, only retaining foreign keys and columns that reference other tables in this set
     */
    public static void filterForeignKeys(Set<Table> selectedTables) {
        Set<String> selectedTableNames = new HashSet<>();
        for (Table t : selectedTables) {
            selectedTableNames.add(t.getName());
        }

        for (Table table : selectedTables) {
            List<ForeignKey> fksFiltered = new ArrayList<>();
            Set<String> fkColumnsToRemove = new HashSet<>();

            for (ForeignKey fk : table.getForeignKeys()) {
                if (selectedTableNames.contains(fk.getReferencedTable())) {
                    fksFiltered.add(fk);
                } else {
                    fkColumnsToRemove.addAll(fk.getColumnNames());
                }
            }

            table.getForeignKeys().clear();
            table.getForeignKeys().addAll(fksFiltered);

            table.getColumns().removeIf(column -> fkColumnsToRemove.contains(column.getName()));
        }
    }
}