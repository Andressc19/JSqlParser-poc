package org.sqlparser.domain.sql;

import lombok.Data;
import org.sqlparser.enums.DataType;

@Data
public class Column {
	private String name;
	private DataType type;
	private Long length;
	private boolean nullable;
	private boolean primaryKey;
	private boolean unique;
	
	public Column(
		String name,
		DataType type,
		Long length,
		boolean nullable,
		boolean primaryKey,
		boolean unique
	) {
		this.name = name;
		this.type = type;
		this.length = length;
		this.nullable = nullable;
		this.primaryKey = primaryKey;
		this.unique = unique;
	}
}
