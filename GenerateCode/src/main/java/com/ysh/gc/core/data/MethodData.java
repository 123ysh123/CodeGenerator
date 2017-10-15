package com.ysh.gc.core.data;

import java.util.Map;

import com.ysh.gc.core.MethodType;

public class MethodData {
	private String name;
	private String fullEntityName;
	private String tableName;
	private MethodType type;
	private Map<String, Column> returns;
	private Map<String, Column> params;
	private boolean allColomn;
	
	public boolean isAllColomn() {
		return allColomn;
	}
	public void setAllColomn(boolean allColomn) {
		this.allColomn = allColomn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFullEntityName() {
		return fullEntityName;
	}
	public void setFullEntityName(String fullEntityName) {
		this.fullEntityName = fullEntityName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public MethodType getType() {
		return type;
	}
	public void setType(MethodType type) {
		this.type = type;
	}
	public Map<String, Column> getReturns() {
		return returns;
	}
	public void setReturns(Map<String, Column> returns) {
		this.returns = returns;
	}
	public Map<String, Column> getParams() {
		return params;
	}
	public void setParams(Map<String, Column> params) {
		this.params = params;
	}
	
}
