package org.sqlparser;

import org.sqlparser.domain.*;
import org.sqlparser.parser.SQL99.SqlSchemaParser;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        
        Scanner sc = new Scanner(System.in);
        System.out.print("❔ Please provide the URL of the file to read: ");
        String filePath = sc.nextLine();
        
        System.out.print("❔ Please provide the name for this schema: ");
        String schemaName = sc.nextLine();
        
        Schema schema = SqlSchemaParser.parseSchemaFromFile(filePath, schemaName);
        
        List<String> identifiedTables = schema.getTables().stream()
            .map(Table::getName)
            .toList();
        
        System.out.println("🔎 Founded tables: ");
        identifiedTables.forEach( tableName -> System.out.println( "    📃 " + tableName) );
        
        System.out.print("Enter the table names to parse, separated by commas (e.g., employees, products): ");
        
        List<String> tablesSelected = Arrays.stream(sc.nextLine().split(","))
            .map(String::trim)
            .map(String::toLowerCase)
            .toList();
        
        List<Table> filteredTables = schema.getTables().stream()
            .filter(table -> tablesSelected.contains(table.getName().toLowerCase()))
            .toList();
        
        filteredTables.forEach(table -> System.out.println(table.getAsciiTable()));
        sc.close();
    }
}