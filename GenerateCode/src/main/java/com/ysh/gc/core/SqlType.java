package com.ysh.gc.core;

import java.util.Arrays;
import java.util.Optional;

import com.ysh.gc.exception.UnSupportedTypeException;

public enum SqlType {
	VARCHAR("VARCHAR","String"),
	CHAR("CHAR","String"),
	TEXT("TEXT","String"),
	TINYTEXT("TINYTEXT","String"),
	
	BIT("BIT","Boolean"),
	INT("INT","Integer"),
	TINYINT("TINYINT","Integer"),
	SMALLINT("SMALLINT","Integer"),//整数都转换成Integer
	MEDIUMINT("MEDIUMINT","Integer"),
	BIGINT("BIGINT","Integer"),
	
	FLOAT("FLOAT","Float"),
	DOUBLE("DOUBLE","Double"),
	DECIMAL("DECIMAL","BigDecimal"),
	
	DATE("DATE","Date"),
	DATETIME("DATETIME","Date"),
	TIME("TIME","Date"),
	YEAR("YEAR","Date"),
	TIMESTAMP("TIMESTAMP","Date");
	
	private String sqlType;
	private String javaType;
	
	public String getSqlType() {
		return sqlType;
	}

	public String getJavaType() {
		return javaType;
	}

	private SqlType(String sqlType, String javaType) {
		this.sqlType = sqlType;
		this.javaType = javaType;
	}

	public static String getJavaType(String typeStr) {
		Optional<SqlType> sqlType = Arrays.stream(SqlType.values())
			.filter(item -> item.getSqlType().equals(typeStr))
			.findAny();
		
		SqlType type = sqlType.orElseThrow(() -> new UnSupportedTypeException("unsuported sql type '" + typeStr + "'"));
		return type.getJavaType();
	}
	
	public static String getFullJavaType(String typeStr) {
		String javaType = getJavaType(typeStr);
		switch (javaType) {
		case "String":
			return "java.lang.String";
		case "Boolean":
			return "java.lang.Boolean";
		case "Integer":
			return "java.lang.Integer";
		case "Float":
			return "java.lang.Float";
		case "Double":
			return "java.lang.Double";
		case "BigDecimal":
			return "java.math.BigDecimal";
		case "Date":
			return "java.util.Date";
		}
		throw new UnSupportedTypeException("unsuported sql type '" + typeStr + "'");
	}
}
