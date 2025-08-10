package org.sqlparser.parser.SQL99;

import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import org.sqlparser.domain.Column;
import org.sqlparser.enums.DataType;

import java.util.List;

public final class ColumnParser {

    public static Column parse(ColumnDefinition colDef) {
        String name = colDef.getColumnName();
        DataType dataType = parseDataType(colDef);
        Long length = parseLength(colDef, dataType);
        boolean nullable = parseNullable(colDef);
        boolean unique = parseUnique(colDef);

        return new Column(name, dataType, length, nullable);
    }

    /** Removes length/precision from type and returns only base type */
    private static DataType parseDataType(ColumnDefinition colDef) {
        ColDataType colDataType = colDef.getColDataType();
        String colType = colDataType.toString().replaceAll("\\(.*\\)", "");
        colType = colType.trim();
        return DataType.valueOf(colType);
    }

    /** Extracts the column length if present, otherwise returns default from SQL99 enum */
    private static Long parseLength(ColumnDefinition colDef, DataType dataType) {
        String typeStr = colDef.getColDataType().toString();
        String match = typeStr.replaceAll(".*\\((.*?)\\).*", "$1");

        if (!match.equals(typeStr)) {
            try {
                return Long.parseLong(match.trim());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return dataType.getDefaultLength() != null ? dataType.getDefaultLength().longValue() : null;
    }

    /** Determines if the column is nullable */
    private static boolean parseNullable(ColumnDefinition colDef) {
        List<String> specs = colDef.getColumnSpecs();
        return specs == null || !specs.contains("NOT NULL");
    }

    /** Determines if the column has UNIQUE constraint */
    private static boolean parseUnique(ColumnDefinition colDef) {
        List<String> specs = colDef.getColumnSpecs();
        return specs != null && specs.contains("UNIQUE");
    }
}