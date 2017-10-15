package com.ysh.gc.core;

import static com.ysh.gc.core.SqlType.getJavaType;
import static com.ysh.gc.deal.StringUtils.matches;
import static com.ysh.gc.deal.Utils.toFieldName;
import static com.ysh.gc.deal.Utils.toShortClassName;

import java.util.Map;
import java.util.stream.Collectors;

import com.ysh.gc.core.data.Column;
import com.ysh.gc.core.data.MethodData;

public class MapperMethod {
	private String return_;
	private String name;
	private String commont;
	private String param;
	
	private MethodType type;
	
	public MapperMethod(MethodData data) {
		this.name = data.getName();
		this.type = MethodType.toMethodType(name);
		this.return_ = toReturn_(data);
		this.commont = toCommont(data);
		this.param = toParam(data);
	}
	
	public String getReturn_() {
		return return_;
	}
	public String getName() {
		return name;
	}
	public String getCommont() {
		return commont;
	}
	public String getParam() {
		return param;
	}
	
	private String toReturn_(MethodData data) {
		if (type == MethodType.SELECT) {

			if (name.equals("selectById")) {
				return toShortClassName(data.getFullEntityName());
			}
			if (matches(name, "select\\w+ById")) {
				if (data.getReturns().size() == 1) {
					for (Map.Entry<String, Column> entry : data.getReturns().entrySet()) {
						return getJavaType(entry.getValue().getType());
					}
				}
				return "Map<String,Object>";
			}
			
			if (name.startsWith("selectCount")) {
				return "int";
			}
			
			if (name.equals("select") || name.startsWith("selectBy")) {
				return "List<" + toShortClassName(data.getFullEntityName()) + ">";
			}
			if (data.getReturns().size() == 1) {
				for (Map.Entry<String, Column> entry : data.getReturns().entrySet()) {
					return "List<" + getJavaType(entry.getValue().getType()) + ">";
				}
			}
			if (data.getReturns().size() == 2) {
				return "List<Map<String,Object>>";
			}
		}
		
		return "void";
	}
	
	private String toCommont(MethodData data) {
		String prefix = data.getParams().entrySet().stream()
				.map(item -> item.getValue().getComment())
				.collect(Collectors.joining(","));
		String after = data.getReturns().entrySet().stream()
				.map(item -> item.getValue().getComment())
				.collect(Collectors.joining(","));
		switch (type) {
			case INSERT:
				return name.equals("insert")?"插入一条记录"
						:name.equals("insertBatch")?"批量插入":"插入并返回key(id)";
			case SELECT:
				if (data.isAllColomn()) {
					return "根据" + prefix + "查询记录";
				}
				return "根据" + prefix + "查询" + after;
			case UPDATE:
				if (data.isAllColomn()) {
					return "根据" + prefix + "更改记录";
				}
				return "根据" + prefix + "更改" + after;
			case DELETE:
				return "根据" + prefix + "删除记录";
		}
		return "";
	}
	
	private String toParam(MethodData data) {
		int paramSize = data.getParams().size();
		String methodName = data.getName();
		String params = null;
		
		if (methodName.startsWith("insert") || methodName.equals("select") || methodName.equals("update")) {
			String clazz = toShortClassName(data.getFullEntityName());
			String parmName = toFieldName(clazz);
			
			if (methodName.equals("insertBatch")) {
				return "List<" + clazz + "> " + parmName + "s" ;
			}
			return clazz + " " + parmName;
		}
		
		params = data.getParams().entrySet().stream().map(item -> {
				String result = "";
				if (paramSize >=2) {
					result = "@Param(\"" + item.getKey() + "\") ";
				}
				if (item.getKey().endsWith("list")) {
					result += "List<" + getJavaType(item.getValue().getType()) + "> " + item.getKey();
				} else {
					result += getJavaType(item.getValue().getType()) + " " + item.getKey();
				}
				return result;
			}).collect(Collectors.joining(", "));
		return params;
	}
}
