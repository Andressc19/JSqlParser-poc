package org.sqlparser.domain.constraints;

import lombok.Data;

import java.util.List;


@Data
public class PrimaryKey {

    private List<String> columnNames;

    public PrimaryKey(List<String> columnNames) {
        this.columnNames = columnNames;
    }

}