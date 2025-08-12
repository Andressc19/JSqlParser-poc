package org.sqlparser.domain;

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
	private boolean foreignKey;
	
	public Column(
		String name,
		DataType type,
		Long length,
		boolean nullable,
		boolean primaryKey,
		boolean unique,
		boolean foreignKey
	) {
		this.name = name;
		this.type = type;
		this.length = length;
		this.nullable = nullable;
		this.primaryKey = primaryKey;
		this.unique = unique;
		this.foreignKey = foreignKey;
	}
}
