package org.sqlparser.domain.constraints;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ForeignKey {

    private List<String> columnNames;
    private String referencedTable;
    private List<String> referencedColumns;

}
