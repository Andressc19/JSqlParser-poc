package org.sqlparser.domain;

import lombok.Data;
import org.sqlparser.domain.constraints.ForeignKey;
import org.sqlparser.domain.constraints.PrimaryKey;
import org.sqlparser.domain.constraints.Unique;

import java.util.ArrayList;
import java.util.List;

@Data
public class Table {

    private String name;
    private List<Column> columns = new ArrayList<>();
    private PrimaryKey primaryKey;
    private List<ForeignKey> foreignKeys = new ArrayList<>();
    private List<Unique> uniqueConstraints = new ArrayList<>();

    public Table(String name) {
        this.name = name;
    }

    public void addColumn(Column column) {
        columns.add(column);
    }

    public void addForeignKey(ForeignKey fk) {
        foreignKeys.add(fk);
    }

    public void addUniqueConstraint(Unique uc) {
        uniqueConstraints.add(uc);
    }

    public boolean isColumnUnique(String columnName) {
        for (Unique uc : uniqueConstraints) {
            if (uc.getColumnNames().contains(columnName)) {
                return true;
            }
        }
        return false;
    }

    public String getAsciiTable() {
        int colWidthName = "Column".length();
        int colWidthType = "Type".length();
        int colWidthLength = "Length".length();
        int colWidthNullable = "Nullable".length();
        int colWidthUnique = "Unique".length();

        for (Column col : columns) {
            colWidthName = Math.max(colWidthName, col.getName().length());
            String cleanType = col.getType().toString();
            colWidthType = Math.max(colWidthType, cleanType.length());
            String lengthStr = col.getLength() != null ? col.getLength().toString() : "";
            colWidthLength = Math.max(colWidthLength, lengthStr.length());
            colWidthNullable = Math.max(colWidthNullable, 1); // 'X' o espacio
            colWidthUnique = Math.max(colWidthUnique, 1); // 'X' o espacio
        }

        StringBuilder sb = new StringBuilder();

        String lineSeparator = "+"
              + "-".repeat(colWidthName + 2) + "+"
              + "-".repeat(colWidthType + 2) + "+"
              + "-".repeat(colWidthLength + 2) + "+"
              + "-".repeat(colWidthNullable + 2) + "+"
              + "-".repeat(colWidthUnique + 2) + "+\n";

        // Calculate total width for the full-width box (exclude newline)
        int totalWidth = lineSeparator.length() - 1;

        // Top border of the box
        sb.append("+").append("-".repeat(totalWidth - 2)).append("+\n");

        // Centered uppercase table name line
        String upperName = name.toUpperCase();
        int paddingTotal = totalWidth - 2 - upperName.length();
        int paddingLeft = paddingTotal / 2;
        int paddingRight = paddingTotal - paddingLeft;
        sb.append("|").append(" ".repeat(paddingLeft)).append(upperName).append(" ".repeat(paddingRight)).append("|\n");


        sb.append(lineSeparator);
        sb.append(String.format("| %-" + colWidthName + "s | %-" + colWidthType + "s | %-" + colWidthLength + "s | %-" + colWidthNullable + "s | %-" + colWidthUnique + "s |\n",
              "Column", "Type", "Length", "Nullable", "Unique"));
        sb.append(lineSeparator);

        for (Column column : columns) {
            String cleanType = column.getType().toString();
            String lengthStr = column.getLength() != null ? column.getLength().toString() : "";
            String nullableMark = column.isNullable() ? "X" : " ";
            String uniqueMark = isColumnUnique(column.getName()) ? "X" : " ";
            sb.append(String.format("| %-" + colWidthName + "s | %-" + colWidthType + "s | %-" + colWidthLength + "s | %-" + colWidthNullable + "s | %-" + colWidthUnique + "s |\n",
                  column.getName(), cleanType, lengthStr, nullableMark, uniqueMark));
        }

        sb.append(lineSeparator);
        return sb.toString();
    }

}
