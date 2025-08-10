package org.sqlparser.domain;

import lombok.Data;
import org.sqlparser.enums.DataType;

@Data
public class Column {
  private String name;
  private DataType type;
  private Long length;
  private boolean nullable;

  public Column (String name, DataType type, Long length, boolean nullable){
      this.name = name;
      this.type = type;
      this.length = length;
      this.nullable = nullable;
  }
}
