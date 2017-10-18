package com.ysh.gc.file;

import static com.ysh.gc.core.SqlType.getFullJavaType;
import static com.ysh.gc.deal.StringUtils.matches;
import static com.ysh.gc.deal.Utils.toFieldName;
import static com.ysh.gc.deal.Utils.toImportPath;
import static com.ysh.gc.file.MapperConstants.CONDITION;
import static com.ysh.gc.file.MapperConstants.DELETE;
import static com.ysh.gc.file.MapperConstants.INSERT;
import static com.ysh.gc.file.MapperConstants.INSERT_FOREACH;
import static com.ysh.gc.file.MapperConstants.MAPPER;
import static com.ysh.gc.file.MapperConstants.RESULT;
import static com.ysh.gc.file.MapperConstants.RETURN_ID;
import static com.ysh.gc.file.MapperConstants.SELECT;
import static com.ysh.gc.file.MapperConstants.UPDATE;
import static com.ysh.gc.file.MapperConstants.FOREACH;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.util.List;

import com.ysh.gc.core.data.Column;
import com.ysh.gc.core.data.MapperData;
import com.ysh.gc.core.data.MethodData;

public class MapperXmlModel implements Model{

	private MapperData data;
	
	public MapperXmlModel(MapperData data) {
		this.data = data;
	}

	@Override
	public String parse() {
		
		String filepath = data.getPath() + "\\" + data.getName() + ".xml";
		File file = new File(filepath);
		if (file.exists()) {
			return getStatement();
		}
		
		String nameSpace = toImportPath(data.getPath()) + "." + data.getName();
		String baseColumn = data.getColumns().stream().map(Column::getName).collect(joining(","));
		
		List<Column> columnExludeId = data.getColumns().stream()
				.filter(item -> !item.getName().equalsIgnoreCase("id"))
				.collect(toList());
		
		String results = columnExludeId.stream()
				.map(item -> RESULT.replace("#{property}", toFieldName(item.getName())).replace("#{column}", item.getName()))
				.collect(joining());
		
		String columnStrExcudeId = columnExludeId.stream().map(Column::getName).collect(joining(","));
		
		String mapper = MAPPER.replace("#{namespace}", nameSpace)
				.replace("#{type}", data.getFullClssName())
				.replace("#{result}", results)
				.replace("#{baseColumn}", baseColumn)
				.replace("#{columnExcludeId}", columnStrExcudeId)
				.replace("#{statement}", getStatement());
		
		return mapper;
	}

	private String getStatement() {
		StringBuilder builder = new StringBuilder();
		for (MethodData methodData : data.getMethodDatas()) {
			switch (methodData.getType()) {
			case INSERT:
				builder.append(getInsert(methodData));
				break;
			case SELECT:
				builder.append(getSelect(methodData));
				break;
			case UPDATE:
				builder.append(getUpdate(methodData));
				break;
			case DELETE:
				builder.append(getDelete(methodData));
				break;
			}
		}
		return builder.toString();
	}
	
	private String getInsert(MethodData methodData) {
		List<String> columns = data.getColumns().stream()
				.filter(item -> !item.getName().equalsIgnoreCase("id"))
				.map(Column::getName)
				.collect(toList());
		
		String methodName = methodData.getName();
		String statement = INSERT.replace("#{id}", methodName)
				.replace("#{table}", methodData.getTableName());
		
		if (methodName.equals("insertBatch")) {
			String params = columns.stream()
					.map(item -> "#{item." + toFieldName(item) + "}")
					.collect(joining(","));
			
			String values = INSERT_FOREACH.replace("#{param}", params);
			statement = statement.replace(" #{returnId}", "")
					.replace("#{values}", values);
			return statement;
		}
		
		String values = columns.stream()
				.map(item -> "#{" + toFieldName(item) + "}")
				.collect(joining(",", "\n\t\t(", ")"));
		
		if (methodName.equals("insert")) {
			statement = statement.replace(" #{returnId}", "")
					.replace("#{values}", values);
			return statement;
		}
		
		statement = statement.replace("#{returnId}", RETURN_ID)
				.replace("#{values}", values);
		return statement;
	}
	
	private String getSelect(MethodData methodData) {
		String methodName = methodData.getName();
		String statement = SELECT.replace("#{id}", methodName)
				.replace("#{table}", methodData.getTableName());
		
		String resultType = "resultMap=\"baseResultMap\"";
		String columns = "<include refid=\"baseColumn\"/>";
		String where = getWhere(methodData);
		
		if (methodName.equals("select")) {
			where = getMutableWhere(methodData);
			statement = statement.replace("WHERE ", "");
		} 
		if (matches(methodName, "select\\w+By\\w+")) {
			if (methodData.getReturns().size() == 1) {
				Column column = methodData.getReturns().entrySet().stream().findFirst().get().getValue();
				resultType = "resultType=\"" + getFullJavaType(column.getType()) + "\"";
				columns = column.getName();
			}
			if (methodData.getReturns().size() == 2) {
				columns = methodData.getReturns().entrySet().stream()
						.map(entry -> entry.getValue().getName())
						.collect(joining(","));
				resultType = "resultType=\"map\"";
			}
		}
		if (matches(methodName, "selectCountBy\\w+")) {
			columns = "COUNT(1)";
			resultType = "resultType=\"int\"";
		}
		
		statement = statement.replace("#{column}", columns)
				.replace("#{resultType}", resultType)
				.replace("#{where}", where);
		return statement;
	}
	
	private String getUpdate(MethodData methodData) {
		String methodName = methodData.getName();
		String statement = UPDATE.replace("#{id}", methodName)
				.replace("#{table}", methodData.getTableName());
		
		String set = null;
		String where = null;
		if (methodName.equals("update")) {
			set = methodData.getParams().entrySet().stream()
					.filter(item -> !item.getKey().equalsIgnoreCase("id"))
					.map(entry -> CONDITION.replace("#{test}", entry.getKey())
							.replace("#{operate}", entry.getValue().getName() + "=#{" + entry.getKey() + "},"))
					.collect(joining("","<set>","\n\t\t</set>"));
			statement = statement.replace("SET ", "");
			where = "id=#{id}";
		}
		
		if (matches(methodName, "update\\w+By\\w+")) {
			where = getWhere(methodData);
			set = methodData.getReturns().entrySet().stream()
					.map(entry -> entry.getValue().getName() + "=#{" + entry.getKey() + "}")
					.collect(joining(","));
		}
		
		statement = statement.replace("#{set}", set)
				.replace("#{where}", where);
		return statement;
	} 
	
	private String getDelete(MethodData methodData) {
		String statement = DELETE.replace("#{id}", methodData.getName())
				.replace("#{table}", methodData.getTableName())
				.replace("#{where}", getWhere(methodData));
		return statement;
	}

	private String getWhere(MethodData methodData) {
		String where = methodData.getParams().entrySet().stream()
				.map(entry -> {
					if (entry.getKey().contains("list")) {
						return entry.getValue().getName() + " IN" + FOREACH.replace("#{collection}", entry.getKey());
					}
					return entry.getValue().getName() + "=#{" + entry.getKey() + "}";})
				.collect(joining(" AND "));
		return where;
	}

	private String getMutableWhere(MethodData methodData) {
		String where = methodData.getParams().entrySet().stream()
				.map(entry -> CONDITION.replace("#{test}", entry.getKey())
				.replace("#{operate}", "AND " + entry.getValue().getName() + "=#{" + entry.getKey() + "}"))
				.collect(joining("", "<where>", "\n\t\t</where>"));
		return where;
	}
	
}
