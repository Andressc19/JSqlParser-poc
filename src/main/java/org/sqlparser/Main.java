package org.sqlparser;

import org.sqlparser.domain.*;
import org.sqlparser.parser.SQL99.SqlSchemaParser;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        String allowedTables = "employees,departments";
        String file = "src/main/resources/hr.sql";
        Schema schema = SqlSchemaParser.parseSchemaFromFile(file, "hr");

        List<Table> filteredTables = schema.getTables().stream()
              .filter( table -> allowedTables.contains(table.getName()))
              .toList();

        filteredTables.forEach(table -> System.out.println(table.getAsciiTable()));
    }
}