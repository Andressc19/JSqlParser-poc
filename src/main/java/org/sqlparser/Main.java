package org.sqlparser;

import org.sqlparser.bfs.SchemaGraph;
import org.sqlparser.domain.graph.Graph;
import org.sqlparser.domain.sql.Schema;
import org.sqlparser.domain.sql.Table;
import org.sqlparser.parser.SQL99.SqlSchemaParser;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        // System.out.print("❔ Please provide the URL of the file to read: ");
        // String filePath = sc.nextLine();
        // System.out.print("❔ Please provide the name for this schema: ");
        // String schemaName = sc.nextLine();
        
        String filePath = "C:\\Users\\joseb\\Desktop\\JSqlParser-poc\\src\\main\\resources\\HR.sql";
        String schemaName = "HR";
        
        Schema schema = SqlSchemaParser.parseSchemaFromFile(filePath, schemaName);
        
        List<String> identifiedTables = schema.getTablesValues().stream()
            .map(Table::getName)
            .toList();
        
        System.out.println("🔎 Founded tables: ");
        identifiedTables.forEach( tableName -> System.out.println( "    📃 " + tableName) );
        
        String selected = "";

        while (!Objects.equals(selected, "q")) {
            System.out.print("Enter the table names to parse, separated by commas (e.g., employees, products): ");
            selected = sc.nextLine();
            
            List<String> tablesSelected = Arrays.stream(selected.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .toList();
            
            List<Table> filteredTables = schema.getTablesValues().stream()
                .filter(table -> tablesSelected.contains(table.getName().toLowerCase()))
                .toList();
            
            filteredTables.forEach(table -> System.out.println(table.getAsciiTable()));
            
            SchemaGraph schemaGraph = new SchemaGraph();
            Graph graph = schemaGraph.buildGraphFromSchema(schema);
            List<String> endpointsNodes = schemaGraph.findFullRoutes(graph, tablesSelected);
            endpointsNodes.forEach(System.out::println);
        }
        
        sc.close();
    }
}