package org.sqlparser.domain.constraints;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sqlparser.domain.Column;
import org.sqlparser.domain.Table;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForeignKey {
    
    private Table sourceTable;
    private Table targetTable;
    private List<Column> sourceColumns = new ArrayList<>();
    private List<Column> targetColumns = new ArrayList<>();
    
    
    public void addSourceColumn (Column column) {
        sourceColumns.add(column);
    }
    
    public void addTargetColumn (Column column) {
        targetColumns.add(column);
    }

}
